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
package org.voninc.contribot.util;

/**
 * Enumeration of supported notification message formats.
 *
 * <p>This enum defines the available formats in which notifications can be presented
 * to users. Each format is represented by a corresponding {@link org.voninc.contribot.service.IGitIssueFormatterStrategy}
 * implementation that handles the actual message formatting.</p>
 *
 * <p>New formats can be added by creating a new enum constant and implementing the
 * corresponding {@link org.voninc.contribot.service.IGitIssueFormatterStrategy} interface.</p>
 */
public enum NotificationFormatType {

  /**
   * Plain text format.
   *
   * <p>Messages formatted with this type are presented as simple, unformatted text suitable
   * for email, SMS, or other text-based delivery channels. Each issue is rendered on a
   * single line with the repository name, issue title, and a direct link.
   * Implementation: {@link org.voninc.contribot.service.PlainTextGitIssueFormatterStrategy}</p>
   */
  PLAINTEXT

}
