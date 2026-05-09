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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.voninc.contribot.dto.GitIssue;
import org.voninc.contribot.exception.ContribotRuntimeException;
import org.voninc.contribot.util.NotificationChannelType;
import org.voninc.contribot.util.NotificationFormatType;

import java.util.List;

/**
 * Orchestrator that coordinates notification formatting and delivery.
 *
 * <p>This component implements the Strategy pattern, maintaining lists of available
 * formatting and notification strategies. It selects the appropriate strategy implementations
 * at runtime based on the requested {@link NotificationFormatType} and
 * {@link NotificationChannelType}.</p>
 *
 * <p>The orchestrator decouples notification formatting from delivery, allowing new
 * formats and channels to be added without modifying existing code.</p>
 */
@Component
public class GitIssueSearchNotificationOrchestrator {

  private final List<IGitIssueFormatterStrategy> gitIssueFormatterStrategies;
  private final List<INotificationStrategy> notificationStrategies;

  /**
   * Creates a new notification orchestrator with available formatting and delivery strategies.
   *
   * <p>All strategies are autowired from the Spring application context. Strategies
   * register themselves by implementing their respective interfaces.</p>
   *
   * @param gitIssueFormatterStrategies list of available {@link IGitIssueFormatterStrategy} implementations
   * @param notificationStrategies      list of available {@link INotificationStrategy} implementations
   */
  @Autowired
  public GitIssueSearchNotificationOrchestrator(List<IGitIssueFormatterStrategy> gitIssueFormatterStrategies,
                                                List<INotificationStrategy> notificationStrategies) {
    this.gitIssueFormatterStrategies = gitIssueFormatterStrategies;
    this.notificationStrategies = notificationStrategies;
  }

  /**
   * Sends a notification with formatted issues using the specified format type and delivery channel.
   *
   * <p>This method performs the following steps:</p>
   * <ol>
   *   <li>Finds a formatter strategy that supports the requested {@link NotificationFormatType}.</li>
   *   <li>Formats the issues using the selected strategy.</li>
   *   <li>Finds a notification strategy that supports the requested {@link NotificationChannelType}.</li>
   *   <li>Dispatches the formatted message using the selected delivery strategy.</li>
   * </ol>
   *
   * @param notificationFormatType  the desired message format (e.g., {@link NotificationFormatType#PLAINTEXT})
   * @param notificationChannelType the desired delivery channel (e.g., {@link NotificationChannelType#EMAIL_SMTP})
   * @param sender                  the sender identifier (e.g., email address or username)
   * @param recipient               the recipient identifier (e.g., email address or username)
   * @param subject                 the notification subject or title
   * @param gitIssues               the list of issues to include in the notification
   * @throws ContribotRuntimeException if no formatter strategy is found for the requested format type,
   *                                   or if no notification strategy is found for the requested channel type
   */
  public void sendNotification(NotificationFormatType notificationFormatType, NotificationChannelType notificationChannelType,
                               String sender, String recipient, String subject, List<GitIssue> gitIssues) {
    String message = gitIssueFormatterStrategies.stream()
        .filter(strategy -> strategy.supports(notificationFormatType))
        .findFirst()
        .orElseThrow(() -> new ContribotRuntimeException("No git issue formatter strategy supports the specified format type."))
        .format(gitIssues);

    notificationStrategies.stream()
        .filter(notificationStrategy -> notificationStrategy.supports(notificationChannelType))
        .findFirst()
        .orElseThrow(() -> new ContribotRuntimeException("No notification strategy supports the specified format type."))
        .dispatch(sender, recipient, subject, message);
  }

}
