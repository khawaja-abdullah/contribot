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
 * Enumeration of supported notification delivery channels.
 *
 * <p>This enum defines the available channels through which notifications can be delivered
 * to users. Each channel is represented by a corresponding {@link org.voninc.contribot.service.INotificationStrategy}
 * implementation that handles the actual message dispatch.</p>
 *
 * <p>New channels can be added by creating a new enum constant and implementing the
 * corresponding {@link org.voninc.contribot.service.INotificationStrategy} interface.</p>
 */
public enum NotificationChannelType {

  /**
   * Email delivery via Simple Mail Transfer Protocol (SMTP).
   *
   * <p>Messages sent via this channel are delivered as email to the specified recipient.
   * This channel requires SMTP server configuration and valid email addresses.
   * Implementation: {@link org.voninc.contribot.service.SmtpNotificationStrategy}</p>
   */
  EMAIL_SMTP

}
