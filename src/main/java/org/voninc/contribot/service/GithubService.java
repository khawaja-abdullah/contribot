/*
 * Copyright 2025 Von Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.voninc.contribot.service;

import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.voninc.contribot.dto.GitIssue;
import org.voninc.contribot.dto.GitRepository;
import org.voninc.contribot.exception.ContribotRuntimeException;
import org.voninc.contribot.config.GithubProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * GitHub-specific implementation of the {@link IGitProviderService} interface.
 *
 * <p>This service acts as an adapter between the generic {@link IGitProviderService} contract
 * and the {@link org.kohsuke.github.GitHub} client library, translating generic queries into
 * GitHub API calls and converting GitHub responses into standardized {@link GitIssue} DTOs.</p>
 *
 * <p>Responsibilities:</p>
 * <ul>
 *   <li>Authenticate with GitHub using credentials from {@link GithubProperties}.</li>
 *   <li>Perform issue searches based on GitHub's native search query syntax.</li>
 *   <li>Map native GitHub {@code GHIssue} and {@code GHRepository} objects to
 *       the application's {@link GitIssue} and {@link GitRepository} DTOs.</li>
 *   <li>Handle potential API or network exceptions by wrapping them in
 *       {@link ContribotRuntimeException}.</li>
 * </ul>
 *
 * <p>Example usage:</p>
 * <pre>{@code
 * List<GitIssue> issues = githubService.findIssues("label:\"bug\" is:open");
 * }</pre>
 */
@Service
public class GithubService implements IGitProviderService {

  private static final Logger LOGGER = LoggerFactory.getLogger(GithubService.class);

  private final GithubProperties githubProperties;
  private final GitHub gitHub;

  @Autowired
  public GithubService(GithubProperties githubProperties, GitHub gitHub) {
    this.githubProperties = githubProperties;
    this.gitHub = gitHub;
  }

  /**
   * Searches for issues on GitHub using the provided query string.
   *
   * <p>The query string must follow GitHub's search syntax. Each resulting issue is
   * mapped to a {@link GitIssue} object, which includes basic issue details and
   * associated repository information.</p>
   *
   * @param query GitHub-specific search string (e.g., {@code "label:\"good first issue\" is:open"}).
   * @return List of {@link GitIssue} objects matching the search criteria.
   * @throws ContribotRuntimeException If the GitHub API call fails or results cannot be processed.
   */
  @Override
  public List<GitIssue> findIssues(String query) {
    List<GitIssue> gitIssues = new ArrayList<>();
    try {
      gitHub.searchIssues()
          .q(query)
          .list()
          .withPageSize(githubProperties.getIssueSearch().getPageSize())
          .forEach(ghIssue -> {
                GHRepository ghRepository = ghIssue.getRepository();
                if (ghRepository == null) {
                  LOGGER.warn("Skipping issue with null repository: {}", ghIssue.getTitle());
                  return;
                }
                gitIssues.add(
                    new GitIssue(
                        ghIssue.getTitle(),
                        ghIssue.getBody(),
                        ghIssue.getHtmlUrl(),
                        new GitRepository(
                            ghRepository.getOwnerName(),
                            ghRepository.getName(),
                            ghRepository.getFullName(),
                            ghRepository.getHtmlUrl()
                        )
                    )
                );
              }
          );
    } catch (Exception e) {
      throw new ContribotRuntimeException("Failed to search GitHub issues with query: " + query, e);
    }
    return gitIssues;
  }

}
