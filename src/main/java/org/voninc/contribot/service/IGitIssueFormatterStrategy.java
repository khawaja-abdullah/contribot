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

import org.voninc.contribot.dto.GitIssue;
import org.voninc.contribot.util.NotificationFormatType;

import java.util.List;

/**
 * Strategy interface for formatting a list of {@link GitIssue} objects for outbound notifications.
 *
 * <p>Implementations control the presentation (e.g., plaintext, HTML) and are selected at runtime based on
 * {@link NotificationFormatType}.</p>
 */
public interface IGitIssueFormatterStrategy {

  /**
   * Formats the given issues into a message body suitable for delivery.
   *
   * @param gitIssues A list of issues to render; may be empty but not null.
   * @return A string representation of the issues according to the strategy format.
   */
  String format(List<GitIssue> gitIssues);

  /**
   * Indicates whether this strategy supports the provided notification format type.
   *
   * @param notificationFormatType The desired message format.
   * @return true if supported; otherwise false.
   */
  boolean supports(NotificationFormatType notificationFormatType);

}
