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
package com.arcussmarthome.platform.services.hub.handlers;

import java.util.UUID;
import java.util.concurrent.TimeoutException;

import org.easymock.EasyMock;
import org.junit.Test;

import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import com.arcussmarthome.core.dao.HubDAO;
import com.arcussmarthome.core.messaging.memory.InMemoryMessageModule;
import com.arcussmarthome.core.messaging.memory.InMemoryPlatformMessageBus;
import com.arcussmarthome.messages.MessageBody;
import com.arcussmarthome.messages.PlatformMessage;
import com.arcussmarthome.messages.address.Address;
import com.arcussmarthome.messages.capability.Capability;
import com.arcussmarthome.messages.capability.HubAdvancedCapability;
import com.arcussmarthome.messages.model.Hub;
import com.arcussmarthome.messages.services.PlatformConstants;
import com.arcussmarthome.messages.type.Population;
import com.arcussmarthome.population.PlacePopulationCacheManager;
import com.arcussmarthome.test.IrisMockTestCase;
import com.arcussmarthome.test.Mocks;
import com.arcussmarthome.test.Modules;

@Mocks({
   HubDAO.class,
   PlacePopulationCacheManager.class
})
@Modules({InMemoryMessageModule.class})
public class TestOnDeleteHandler extends IrisMockTestCase {

   @Inject private HubDAO hubDao;
   @Inject private InMemoryPlatformMessageBus bus;
   @Inject private PlacePopulationCacheManager mockPopulationCacheMgr;
   private PlaceDeletedListener onDeleteHandler;

   private Hub hub;

   @Override
   public void setUp() throws Exception {
      super.setUp();
      hub = new Hub();
      hub.setId("ABC-1234");
      hub.setPlace(UUID.randomUUID());
      
      EasyMock.expect(mockPopulationCacheMgr.getPopulationByPlaceId(EasyMock.anyObject(UUID.class))).andReturn(Population.NAME_GENERAL).anyTimes();

      onDeleteHandler = new PlaceDeletedListener(hubDao, new HubDeleteHandler(hubDao, bus, mockPopulationCacheMgr));
   }

   @Override
   public void tearDown() throws Exception {
      verify();
      super.tearDown();
   }

   @Test
   public void testPlaceWithNoHubDoesNothing() throws Exception {
      EasyMock.expect(hubDao.findHubForPlace(hub.getPlace())).andReturn(null);
      replay();
      onDeleteHandler.handleStaticEvent(createDelete());
      try {
         bus.take();
      } catch(TimeoutException te) {
         // expected because no messages should be pushed
      }
   }

   @Test
   public void testPlaceWithHubDeletesHub() throws Exception {
      EasyMock.expect(hubDao.findHubForPlace(hub.getPlace())).andReturn(hub);
      hubDao.delete(hub);
      EasyMock.expectLastCall();
      replay();

      onDeleteHandler.handleStaticEvent(createDelete());

      PlatformMessage dereg = bus.take();
      assertEquals(HubAdvancedCapability.DeregisterEvent.NAME, dereg.getMessageType());

      PlatformMessage deleted = bus.take();
      assertEquals(Capability.EVENT_DELETED, deleted.getMessageType());
      assertEquals(hub.getAddress(), deleted.getSource().getRepresentation());
   }

   private PlatformMessage createDelete() {
      MessageBody body = MessageBody.buildMessage(Capability.EVENT_DELETED, ImmutableMap.of());
      return PlatformMessage.buildBroadcast(body, Address.platformService(hub.getPlace(), PlatformConstants.SERVICE_PLACES))
            .withPlaceId(hub.getPlace())
            .withPopulation(Population.NAME_GENERAL)
            .create();
   }

}

