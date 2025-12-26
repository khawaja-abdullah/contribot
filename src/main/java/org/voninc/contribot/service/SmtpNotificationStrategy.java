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
 * SMTP-based implementation of {@link INotificationStrategy} that sends emails using Spring's {@link JavaMailSender}.
 *
 * <p>This strategy constructs a {@link SimpleMailMessage} with the provided sender, recipient, subject, and message
 * and delegates message delivery to the configured {@link JavaMailSender} instance.</p>
 */
@Service
public class SmtpNotificationStrategy implements INotificationStrategy {

  private final JavaMailSender javaMailSender;

  /**
   * Creates a new SMTP notification strategy.
   *
   * @param javaMailSender The mail sender used to dispatch email messages.
   */
  @Autowired
  public SmtpNotificationStrategy(JavaMailSender javaMailSender) {
    this.javaMailSender = javaMailSender;
  }

  /**
   * Sends an email using SMTP with the specified parameters.
   *
   * @param sender    The email address of the sender.
   * @param recipient The email address of the recipient.
   * @param subject   The email subject.
   * @param message   The email body text.
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
   * {@inheritDoc}
   */
  @Override
  public boolean supports(NotificationChannelType notificationChannelType) {
    return NotificationChannelType.EMAIL_SMTP.equals(notificationChannelType);
  }

}
