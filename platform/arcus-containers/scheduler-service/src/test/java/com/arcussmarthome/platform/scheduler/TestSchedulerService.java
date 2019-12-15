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
package com.arcussmarthome.platform.scheduler;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.easymock.EasyMock;
import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;
import com.arcussmarthome.core.messaging.memory.InMemoryMessageModule;
import com.arcussmarthome.core.messaging.memory.InMemoryPlatformMessageBus;
import com.arcussmarthome.io.json.JSON;
import com.arcussmarthome.messages.MessageBody;
import com.arcussmarthome.messages.PlatformMessage;
import com.arcussmarthome.messages.address.Address;
import com.arcussmarthome.messages.capability.SchedulerCapability;
import com.arcussmarthome.messages.model.Fixtures;
import com.arcussmarthome.messages.model.SimpleModel;
import com.arcussmarthome.messages.model.test.ModelFixtures;
import com.arcussmarthome.messages.service.SchedulerService.ListSchedulersRequest;
import com.arcussmarthome.messages.service.SchedulerService.ListSchedulersResponse;
import com.arcussmarthome.service.scheduler.SchedulerCapabilityDispatcher;
import com.arcussmarthome.service.scheduler.SchedulerCapabilityService;
import com.arcussmarthome.service.scheduler.SchedulerRegistry;
import com.arcussmarthome.test.IrisMockTestCase;
import com.arcussmarthome.test.Mocks;
import com.arcussmarthome.test.Modules;

/**
 * 
 */
@Modules(InMemoryMessageModule.class)
@Mocks(SchedulerRegistry.class)
public class TestSchedulerService extends IrisMockTestCase {
   UUID placeId = UUID.randomUUID();
   Address clientAddress = Fixtures.createClientAddress();
   Address serviceAddress = Address.platformService(SchedulerCapability.NAMESPACE);
   
   // unit under test
   @Inject SchedulerCapabilityService service;
   
   @Inject InMemoryPlatformMessageBus messageBus;
   @Inject SchedulerRegistry mockRegistry;
   
   protected void send(MessageBody body) {
      PlatformMessage message =
            PlatformMessage
               .buildRequest(body, clientAddress, serviceAddress)
               .withPlaceId(placeId)
               .create();
      messageBus.send(message);
      // throw away what we sent
      assertEquals(message, messageBus.poll());
   }
   
   @Test
   public void testListNoneForPlace() throws Exception {
      EasyMock
         .expect(mockRegistry.loadByPlace(placeId, true))
         .andReturn(ImmutableList.of());
      replay();
      
      MessageBody body =
            ListSchedulersRequest
               .builder()
               .withPlaceId(placeId.toString())
               .build();
      send(body);
      
      MessageBody response = messageBus.take().getValue();
      assertEquals(ListSchedulersResponse.NAME, response.getMessageType());
      assertEquals(ImmutableList.of(), ListSchedulersResponse.getSchedulers(response));
      
      verify();
   }

   @Test
   public void testListSomeForPlace() throws Exception {
      Map<String, Object> schedulerAttributes1 = ModelFixtures.createServiceAttributes(SchedulerCapability.NAMESPACE);
      Map<String, Object> schedulerAttributes2 = ModelFixtures.createServiceAttributes(SchedulerCapability.NAMESPACE);
      
      SchedulerCapabilityDispatcher executor1 = EasyMock.createNiceMock(SchedulerCapabilityDispatcher.class);
      EasyMock.expect(executor1.getScheduler()).andReturn(new SimpleModel(schedulerAttributes1)).anyTimes();
      SchedulerCapabilityDispatcher executor2 = EasyMock.createNiceMock(SchedulerCapabilityDispatcher.class);
      EasyMock.expect(executor2.getScheduler()).andReturn(new SimpleModel(schedulerAttributes2)).anyTimes();
      
      EasyMock
         .expect(mockRegistry.loadByPlace(placeId, true))
         .andReturn(ImmutableList.of(executor1, executor2));
      replay();
      EasyMock.replay(executor1, executor2);
      
      MessageBody body =
            ListSchedulersRequest
               .builder()
               .withPlaceId(placeId.toString())
               .build();
      send(body);
      
      MessageBody response = messageBus.take().getValue();
      assertEquals(ListSchedulersResponse.NAME, response.getMessageType());
      List<Map<String, Object>> responseAttributes = ListSchedulersResponse.getSchedulers(response);
      assertEquals(
            JSON.fromJson(JSON.toJson(
                  ImmutableList.of(schedulerAttributes1, schedulerAttributes2)
            ), Object.class), 
            responseAttributes
      );
      
      verify();
   }
   
   @Test
   public void testListWrongPlace() throws Exception {
      // there shouldn't be any requests to the scheduler because of the mis-match
      replay();
      
      MessageBody body =
            ListSchedulersRequest
               .builder()
               .withPlaceId(UUID.randomUUID().toString())
               .build();
      send(body);
      
      MessageBody response = messageBus.take().getValue();
      assertEquals(ListSchedulersResponse.NAME, response.getMessageType());
      assertEquals(
            ImmutableList.of(), 
            ListSchedulersResponse.getSchedulers(response)
      );
      
      verify();
   }

}

