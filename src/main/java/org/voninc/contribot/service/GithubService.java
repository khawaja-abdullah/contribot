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
import org.voninc.contribot.util.ApplicationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * Concrete implementation of the {@link IGitProviderService} interface for the GitHub platform.
 * <p>
 * This class acts as an **Adapter**, translating the generic service contract into specific calls to the
 * {@link org.kohsuke.github.GitHub} client.
 */
@Service
public class GithubService implements IGitProviderService {

  private static final Logger LOGGER = LoggerFactory.getLogger(GithubService.class);

  private final ApplicationProperties applicationProperties;
  private final GitHub gitHub;

  @Autowired
  public GithubService(ApplicationProperties applicationProperties, GitHub gitHub) {
    this.applicationProperties = applicationProperties;
    this.gitHub = gitHub;
  }

  /**
   * Executes a search against the GitHub Issues Search API using the provided query string.
   * <p>
   * It performs the following steps:
   * <ul>
   * <li>Passes the {@code query} directly to the GitHub API.</li>
   * <li>Retrieves the list of matching {@code GHIssue} objects.</li>
   * <li>Iterates through the results, converting each GitHub native object into a
   * standardized {@link GitIssue} DTO.</li>
   * </ul>
   *
   * @param query The GitHub-specific search string (e.g., {@code "label:\"good first issue\" is:open"}).
   * @return A list of standardized {@link GitIssue} DTOs.
   */
  @Override
  public List<GitIssue> findIssues(String query) {
    List<GitIssue> gitIssues = new ArrayList<>();
    try {
      gitHub.searchIssues()
          .q(query)
          .list()
          .withPageSize(applicationProperties.getGithubIssueSearchPageSize())
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
