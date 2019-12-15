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
/**
 * 
 */
package com.arcussmarthome.platform.services.place.handlers;


import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.arcussmarthome.capability.attribute.transform.BeanAttributesTransformer;
import com.arcussmarthome.core.dao.PlaceDAO;
import com.arcussmarthome.core.platform.ContextualRequestMessageHandler;
import com.arcussmarthome.core.platform.PlatformMessageBus;
import com.arcussmarthome.messages.MessageBody;
import com.arcussmarthome.messages.PlatformMessage;
import com.arcussmarthome.messages.address.Address;
import com.arcussmarthome.messages.capability.Capability;
import com.arcussmarthome.messages.capability.PlaceCapability;
import com.arcussmarthome.messages.model.Place;
import com.arcussmarthome.platform.address.StreetAddress;
import com.arcussmarthome.platform.address.updater.AddressUpdater;
import com.arcussmarthome.platform.address.updater.AddressUpdaterFactory;

import java.util.Map;

/**
 * Handler that handles UpdateAddressRequest.
 */
@Singleton
public class PlaceUpdateAddressHandler implements ContextualRequestMessageHandler<Place> {

   private final PlaceDAO placeDao;
   private final BeanAttributesTransformer<Place> transformer;
   private final PlatformMessageBus messageBus;
   private final AddressUpdaterFactory updaterFactory;

   @Inject
   public PlaceUpdateAddressHandler(
         PlaceDAO placeDao,
         BeanAttributesTransformer<Place> transformer,
         PlatformMessageBus messageBus,
         AddressUpdaterFactory updaterFactory
   ) {
      this.placeDao = placeDao;
      this.transformer = transformer;
      this.messageBus = messageBus;
      this.updaterFactory = updaterFactory;
   }

   @Override
   public String getMessageType() {
      return PlaceCapability.UpdateAddressRequest.NAME;
   }

   @Override
   public MessageBody handleRequest(Place context, PlatformMessage msg) {
      MessageBody body = msg.getValue();
      StreetAddress newAddress = StreetAddress.fromMap(PlaceCapability.UpdateAddressRequest.getStreetAddress(body));
      AddressUpdater updater = updaterFactory.updaterFor(context);
      Map<String,Object> updates = updater.updateAddress(context, newAddress);
      if(!updates.isEmpty()) {
         updatePlaceEmitVC(context, updates);
      }
      return PlaceCapability.UpdateAddressResponse.instance();
   }

   private void updatePlaceEmitVC(Place context, Map<String,Object> updates) {
      transformer.merge(context, updates);
      placeDao.save(context);
      messageBus.send(
            PlatformMessage.buildBroadcast(
                  MessageBody.buildMessage(Capability.EVENT_VALUE_CHANGE, updates),
                  Address.fromString(context.getAddress())
            )
            .withPlaceId(context.getId())
            .withPopulation(context.getPopulation())
            .create()
      );
   }
}

