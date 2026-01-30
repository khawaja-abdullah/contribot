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
