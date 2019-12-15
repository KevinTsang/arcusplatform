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
package com.arcussmarthome.platform.services.person.handlers;

import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.arcussmarthome.core.platform.ContextualRequestMessageHandler;
import com.arcussmarthome.messages.MessageBody;
import com.arcussmarthome.messages.PlatformMessage;
import com.arcussmarthome.messages.capability.PersonCapability;
import com.arcussmarthome.messages.model.Person;
import com.arcussmarthome.platform.services.PersonDeleter;

@Singleton
public class PersonRemoveFromPlaceHandler implements ContextualRequestMessageHandler<Person> {

   private final PersonDeleter deleter;

   @Inject
   public PersonRemoveFromPlaceHandler(PersonDeleter deleter) {
      this.deleter = deleter;
   }

   @Override
   public String getMessageType() {
      return PersonCapability.RemoveFromPlaceRequest.NAME;
   }

   @Override
   public MessageBody handleRequest(Person context, PlatformMessage msg) {
      MessageBody body = msg.getValue();
      String placeId = PersonCapability.RemoveFromPlaceRequest.getPlaceId(body);
      if(StringUtils.isBlank(placeId)) {
         placeId = msg.getPlaceId();
      }
      
      deleter.removePersonFromPlace(UUID.fromString(placeId), context, msg.getActor(), true);
      return PersonCapability.RemoveFromPlaceResponse.instance();
   }
}

