package org.voninc.contribot.service;

import org.kohsuke.github.GitHub;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GitHubIssueSearchService {

  private final GitHub gitHub;

  @Autowired
  public GitHubIssueSearchService(GitHub gitHub) {
    this.gitHub = gitHub;
  }

  public int count() {
    return gitHub.searchIssues()
        .q("is:issue is:open label:\"good first issue\" language:Java")
        .list()
        .getTotalCount();
  }

}