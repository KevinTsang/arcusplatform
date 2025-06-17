/*
 * Copyright 2019 Arcus Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.iris.notification.provider;

import java.io.IOException;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.iris.core.dao.AccountDAO;
import com.iris.core.dao.PersonDAO;
import com.iris.core.dao.PlaceDAO;
import com.iris.notification.dispatch.DispatchException;
import com.iris.notification.dispatch.DispatchUnsupportedByUserException;
import com.iris.notification.message.NotificationMessageRenderer;
import com.iris.notification.upstream.UpstreamNotificationResponder;
import com.iris.notification.utils.MailParams;
import com.iris.platform.notification.Notification;
import com.mailgun.api.v3.MailgunMessagesApi;
import com.mailgun.client.MailgunClient;
import com.mailgun.model.message.Message;
import com.mailgun.model.message.Message.MessageBuilder;
import com.mailgun.model.message.MessageResponse;
import com.mailgun.util.EmailUtil;

@Singleton
public class MailgunEmailProvider implements NotificationProvider {

   private final MailgunMessagesApi mailgunMessagesApiUS;
   private final NotificationMessageRenderer messageRenderer;
   private final UpstreamNotificationResponder responder;

   
   @Inject
   public MailgunEmailProvider(@Named("email.provider.apikey") String mailgunApiKey, PersonDAO personDao, PlaceDAO placeDao, AccountDAO accountDao, NotificationMessageRenderer messageRenderer, UpstreamNotificationResponder responder) {
      this.mailgunMessagesApiUS = MailgunClient.config(mailgunApiKey).createApi(MailgunMessagesApi.class);
      this.messageRenderer = messageRenderer;
      this.responder = responder;
   }

   @Override
   public void notifyCustomer(Notification notification) throws DispatchException, DispatchUnsupportedByUserException {
      // Implementation for sending email using Mailgun
      // This would typically involve creating a Mail object, setting the content, and using the Mailgun client to send it.
   }

   @Override
   public boolean supportedByUser(Notification notification) {
      // Logic to determine if the notification is supported by the user
      return true;
   }
    
   public void sendEmail(MailParams mailParams) throws DispatchException {
      String filterDomain = mailParams.getEmailFilterDomain();
      MessageBuilder messageBuilder = Message.builder()
         .from(EmailUtil.nameWithEmail(mailParams.getSenderName(), mailParams.getFromEmail().getEmail()))
         .to(EmailUtil.nameWithEmail(mailParams.getRecipientName(), mailParams.getToEmail().getEmail()))
         .subject(mailParams.getSubject())
         .text(mailParams.getPlaintextBody())
         .html(mailParams.getHtmlBody());
         
      MessageResponse messageResponse = mailgunMessagesApiUS.sendMessage(filterDomain, messageBuilder.build());
      if (messageResponse.getId() == null || messageResponse.getId().isEmpty()) {
      // TODO: Implement proper logging
      //   logger.error("Failed to send email using Mailgun, no message ID returned. Response: {}", messageResponse);
         throw new DispatchException("Failed to send notification email. Reason: no message ID returned");
      }
   }
}
