package org.voninc.contribot.service;

import org.voninc.contribot.util.NotificationChannelType;

/**
 * Strategy interface for dispatching notifications through a specific outbound channel.
 *
 * <p>Implementations encapsulate the details of sending messages (e.g., SMTP, Slack),
 * allowing the application to select an appropriate channel at runtime via
 * {@link #supports(NotificationChannelType)}.</p>
 */
public interface INotificationStrategy {

  /**
   * Dispatches a notification using the underlying channel.
   *
   * @param sender    The sender identity/address appropriate for the channel.
   * @param recipient The recipient identity/address appropriate for the channel.
   * @param subject   The subject or title of the notification (may be ignored by some channels).
   * @param message   The message body to send.
   */
  void dispatch(String sender, String recipient, String subject, String message);

  /**
   * Indicates whether this strategy supports the provided channel type.
   *
   * @param notificationChannelType The desired notification channel.
   * @return true if the strategy can handle the given channel; otherwise false.
   */
  boolean supports(NotificationChannelType notificationChannelType);

}
