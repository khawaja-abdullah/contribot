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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.voninc.contribot.dto.GitIssue;
import org.voninc.contribot.dto.GitRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class GithubService implements IGitProviderService {

  private final GitHub gitHub;

  @Autowired
  public GithubService(GitHub gitHub) {
    this.gitHub = gitHub;
  }

  @Override
  public List<GitIssue> findIssues(String query) {
    List<GitIssue> gitIssues = new ArrayList<>();
    gitHub.searchIssues()
        .q(query)
        .list()
        .forEach(ghIssue -> {
              GHRepository ghRepository = ghIssue.getRepository();
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
    return gitIssues;
  }

}
