package org.voninc.contribot.service;

import org.springframework.stereotype.Service;
import org.voninc.contribot.dto.GitIssue;
import org.voninc.contribot.util.NotificationFormatType;

import java.util.List;

/**
 * Plaintext implementation of {@link IGitIssueFormatterStrategy} that renders issues in a human-readable format.
 */
@Service
public class PlainTextGitIssueFormatterStrategy implements IGitIssueFormatterStrategy {

  /**
   * {@inheritDoc}
   */
  @Override
  public String format(List<GitIssue> gitIssues) {
    StringBuilder formattedGitIssues = new StringBuilder();
    gitIssues.forEach(gitIssue -> formattedGitIssues.append(formatIssue(gitIssue)).append("\n\n"));
    return formattedGitIssues.toString();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean supports(NotificationFormatType notificationFormatType) {
    return NotificationFormatType.PLAINTEXT.equals(notificationFormatType);
  }

  private String formatIssue(GitIssue gitIssue) {
    return String.format(
        "🚀 New Issue in %s: %s%nView here: %s",
        gitIssue.gitRepository().fullRepositoryName(),
        gitIssue.title(),
        gitIssue.url()
    );
  }

}
