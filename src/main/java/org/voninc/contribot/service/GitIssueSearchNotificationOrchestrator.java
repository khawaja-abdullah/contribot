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
