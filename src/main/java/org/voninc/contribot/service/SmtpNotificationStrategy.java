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
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.voninc.contribot.util.NotificationChannelType;

/**
 * SMTP email notification strategy implementation.
 *
 * <p>This strategy sends notifications via email using SMTP (Simple Mail Transfer Protocol).
 * It leverages Spring's {@link JavaMailSender} to send simple text emails with customizable
 * sender, recipient, subject, and message body.</p>
 *
 * <p>This implementation is selected at runtime when {@link NotificationChannelType#EMAIL_SMTP}
 * is specified in the notification configuration.</p>
 */
@Service
public class SmtpNotificationStrategy implements INotificationStrategy {

  private final JavaMailSender javaMailSender;

  /**
   * Creates a new SMTP notification strategy with the provided mail sender.
   *
   * @param javaMailSender the Spring {@link JavaMailSender} bean used to send emails;
   *                       must not be null
   */
  @Autowired
  public SmtpNotificationStrategy(JavaMailSender javaMailSender) {
    this.javaMailSender = javaMailSender;
  }

  /**
   * Sends an email notification with the specified parameters.
   *
   * <p>This method constructs a {@link SimpleMailMessage}, populates it with the provided
   * sender, recipient, subject, and message body, and sends it via the configured SMTP server.</p>
   *
   * @param sender    the sender email address (should match the SMTP authentication credentials)
   * @param recipient the recipient email address
   * @param subject   the email subject line
   * @param message   the email message body (plain text)
   */
  @Override
  public void dispatch(String sender, String recipient, String subject, String message) {
    SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
    simpleMailMessage.setFrom(sender);
    simpleMailMessage.setTo(recipient);
    simpleMailMessage.setSubject(subject);
    simpleMailMessage.setText(message);
    javaMailSender.send(simpleMailMessage);
  }

  /**
   * Indicates whether this strategy supports EMAIL_SMTP channel.
   *
   * @param notificationChannelType The channel type to check
   * @return true if EMAIL_SMTP, false otherwise
   */
  @Override
  public boolean supports(NotificationChannelType notificationChannelType) {
    return NotificationChannelType.EMAIL_SMTP.equals(notificationChannelType);
  }

}
