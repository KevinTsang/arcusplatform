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
package com.arcussmarthome.platform.services.place.handlers;

import java.util.concurrent.TimeoutException;

import org.junit.Test;

import com.arcussmarthome.core.dao.DeviceDAO;
import com.arcussmarthome.core.dao.HubDAO;
import com.arcussmarthome.core.messaging.memory.InMemoryMessageModule;
import com.arcussmarthome.messages.MessageBody;
import com.arcussmarthome.messages.PlatformMessage;
import com.arcussmarthome.messages.address.Address;
import com.arcussmarthome.messages.capability.BridgeCapability;
import com.arcussmarthome.messages.capability.PlaceCapability;
import com.arcussmarthome.test.Mocks;
import com.arcussmarthome.test.Modules;

@Mocks({
   DeviceDAO.class,
   HubDAO.class
})
@Modules({
   InMemoryMessageModule.class
})
public class TestPlaceStopAddingDeviceHandler extends BasePlacePairingHandler {

   @Override
   public void setUp() throws Exception {
      super.setUp();
      handler = new StopAddingDevicesHandler(hubDao, deviceDao, bus);
   }

   StopAddingDevicesHandler handler;
   
   @Test
   public void testNotifyBridgeDevices() throws InterruptedException, TimeoutException{
      setDefaultMockHubandDeviceDAO();
      replay();
      handler.handleRequest(place, createStopAddDeviceRequest());
      bus.take(); //hub pair
      PlatformMessage bridge1 = bus.take();
      assertEquals(BridgeCapability.StopPairingRequest.NAME, bridge1.getMessageType());
      assertEquals(bridgeDevice1.getAddress(),bridge1.getDestination().getRepresentation());
      
      PlatformMessage bridge2 = bus.take();
      assertEquals(BridgeCapability.StopPairingRequest.NAME, bridge2.getMessageType());
      assertEquals(bridgeDevice2.getAddress(),bridge2.getDestination().getRepresentation());

   }
   
   private PlatformMessage createStopAddDeviceRequest(){
      MessageBody body = PlaceCapability.StopAddingDevicesRequest.instance();
      return PlatformMessage.buildRequest(body, clientAddress, Address.fromString(place.getAddress()))
            .withCorrelationId("correlationid")
            .withPlaceId(place.getId().toString())
            .withPopulation(place.getPopulation())
            .isRequestMessage(true)
            .create();
   }


}

