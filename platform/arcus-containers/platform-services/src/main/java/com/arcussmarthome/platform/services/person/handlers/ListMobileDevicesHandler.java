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

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.arcussmarthome.capability.attribute.transform.BeanAttributesTransformer;
import com.arcussmarthome.core.dao.MobileDeviceDAO;
import com.arcussmarthome.core.platform.ContextualRequestMessageHandler;
import com.arcussmarthome.messages.MessageBody;
import com.arcussmarthome.messages.PlatformMessage;
import com.arcussmarthome.messages.capability.PersonCapability;
import com.arcussmarthome.messages.model.MobileDevice;
import com.arcussmarthome.messages.model.Person;


@Singleton
public class ListMobileDevicesHandler implements ContextualRequestMessageHandler<Person> {

   private final MobileDeviceDAO dao;
   private final BeanAttributesTransformer<MobileDevice> transformer;

   @Inject
   public ListMobileDevicesHandler(MobileDeviceDAO dao, BeanAttributesTransformer<MobileDevice> transformer) {
      this.dao = dao;
      this.transformer = transformer;
   }

   @Override
   public String getMessageType() {
      return PersonCapability.ListMobileDevicesRequest.NAME;
   }

   @Override
   public MessageBody handleRequest(Person context, PlatformMessage msg) {
      List<MobileDevice> devices = dao.listForPerson(context);
      List<Map<String,Object>> transformed = devices
            .stream()
            .map((d) -> { return transformer.transform(d); })
            .collect(Collectors.toList());

      return PersonCapability.ListMobileDevicesResponse.builder()
            .withMobileDevices(transformed)
            .build();
   }
}

