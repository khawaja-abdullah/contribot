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

import org.springframework.stereotype.Service;
import org.voninc.contribot.dto.GitIssue;
import org.voninc.contribot.util.NotificationFormatType;

import java.util.List;

/**
 * Plain text formatter implementation for Git issues.
 *
 * <p>This strategy converts a list of {@link GitIssue} objects into a human-readable
 * plain-text format suitable for email notifications or other text-based delivery channels.</p>
 *
 * <p>Each issue is formatted on a single line with emoji indicators, repository name, issue title,
 * and a direct link to the issue.</p>
 */
@Service
public class PlainTextGitIssueFormatterStrategy implements IGitIssueFormatterStrategy {

  /**
   * Formats a list of Git issues into plain text format.
   *
   * <p>Each issue is formatted as a single line containing a rocket emoji (🚀),
   * the repository name, issue title, and a clickable URL.</p>
   *
   * @param gitIssues the list of {@link GitIssue} objects to format; may be empty but not null
   * @return a plain-text string with each issue on a separate line, suitable for email notifications
   */
  @Override
  public String format(List<GitIssue> gitIssues) {
    StringBuilder formattedGitIssues = new StringBuilder();
    gitIssues.forEach(gitIssue -> formattedGitIssues.append(formatIssue(gitIssue)).append("\n\n"));
    return formattedGitIssues.toString();
  }

  /**
   * Indicates whether this strategy supports PLAINTEXT format.
   *
   * @param notificationFormatType The format type to check
   * @return true if PLAINTEXT, false otherwise
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
