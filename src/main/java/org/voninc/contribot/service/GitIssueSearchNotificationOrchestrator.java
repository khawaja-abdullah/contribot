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

@Component
public class GitIssueSearchNotificationOrchestrator {

  private final List<IGitIssueFormatterStrategy> gitIssueFormatterStrategies;
  private final List<INotificationStrategy> notificationStrategies;

  @Autowired
  public GitIssueSearchNotificationOrchestrator(List<IGitIssueFormatterStrategy> gitIssueFormatterStrategies,
                                                List<INotificationStrategy> notificationStrategies) {
    this.gitIssueFormatterStrategies = gitIssueFormatterStrategies;
    this.notificationStrategies = notificationStrategies;
  }

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
