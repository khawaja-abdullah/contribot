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
