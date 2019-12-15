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
package com.arcussmarthome.notification.provider;

import java.util.Map;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.arcussmarthome.core.dao.AccountDAO;
import com.arcussmarthome.core.dao.PersonDAO;
import com.arcussmarthome.core.dao.PlaceDAO;
import com.arcussmarthome.messages.model.BaseEntity;
import com.arcussmarthome.messages.model.Person;
import com.arcussmarthome.notification.dispatch.DispatchException;
import com.arcussmarthome.notification.dispatch.DispatchUnsupportedByUserException;
import com.arcussmarthome.notification.message.NotificationMessageRenderer;
import com.arcussmarthome.notification.provider.twilio.TwilioSender;
import com.arcussmarthome.notification.upstream.UpstreamNotificationResponder;
import com.arcussmarthome.platform.notification.Notification;
import com.arcussmarthome.platform.notification.provider.NotificationProviderUtil;

@Singleton
public class IVRProvider implements NotificationProvider {

   private final PersonDAO personDao;
   private final TwilioSender twilio;
   private final UpstreamNotificationResponder responder;
   private final PlaceDAO placeDao;
   private final AccountDAO accountDao;

   @Inject
   public IVRProvider(TwilioSender twilio, PersonDAO personDao, PlaceDAO placeDao, AccountDAO accountDao, NotificationMessageRenderer messageRenderer, UpstreamNotificationResponder responder) {
      this.personDao = personDao;
      this.placeDao = placeDao;
      this.twilio = twilio;
      this.responder = responder;
      this.accountDao = accountDao;
   }

   @Override
   public void notifyCustomer(Notification notification) throws DispatchUnsupportedByUserException, DispatchException {

      // Collect recipient information
	   Map<String, BaseEntity<?, ?>> additionaEntityParams = NotificationProviderUtil.addAdditionalParamsAndReturnRecipient(placeDao, personDao, accountDao, notification);
       Person recipient = NotificationProviderUtil.getPersonFromParams(additionaEntityParams);
      if (recipient == null) {
         throw new DispatchUnsupportedByUserException("No person found with id: " + notification.getPersonId());
      }

      // Recipient should have mobile number on file
      String mobileNumber = recipient.getMobileNumber();
      if (mobileNumber == null) {
         throw new DispatchUnsupportedByUserException("Person has no phone number on file.");
      }
      try{
         String callSID = twilio.sendIVR(notification, recipient);
         responder.handleHandOff(notification);
      }
      catch(DispatchUnsupportedByUserException duu){
         throw duu;
      }
      catch(Exception e){
         throw new DispatchException(e);
      }
   }

}

