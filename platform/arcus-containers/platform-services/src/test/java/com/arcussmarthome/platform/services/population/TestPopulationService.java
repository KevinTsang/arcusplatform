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
package com.arcussmarthome.platform.services.population;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.common.util.concurrent.MoreExecutors;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.arcussmarthome.bootstrap.Bootstrap;
import com.arcussmarthome.bootstrap.ServiceLocator;
import com.arcussmarthome.bootstrap.guice.GuiceServiceLocator;
import com.arcussmarthome.capability.attribute.transform.AttributeMapTransformModule;
import com.arcussmarthome.capability.registry.CapabilityRegistry;
import com.arcussmarthome.core.dao.EmptyResourceBundle;
import com.arcussmarthome.core.dao.PopulationDAO;
import com.arcussmarthome.core.dao.ResourceBundleDAO;
import com.arcussmarthome.core.messaging.memory.InMemoryMessageModule;
import com.arcussmarthome.core.messaging.memory.InMemoryPlatformMessageBus;
import com.arcussmarthome.core.platform.ContextualRequestMessageHandler;
import com.arcussmarthome.core.platform.PlatformMessageBus;
import com.arcussmarthome.messages.MessageBody;
import com.arcussmarthome.messages.PlatformMessage;
import com.arcussmarthome.messages.address.Address;
import com.arcussmarthome.messages.model.Fixtures;
import com.arcussmarthome.messages.services.PlatformConstants;
import com.arcussmarthome.messages.type.Population;
import com.arcussmarthome.platform.services.population.handlers.ListPopulationsHandler;

public class TestPopulationService {
   private PopulationService service;
   private InMemoryPlatformMessageBus bus;
   private PopulationDAO populationDao;
   private final List<Population> populationList = new ArrayList<>();

   @SuppressWarnings("unchecked")
   @Before
   public void setup() throws Exception {
      Bootstrap bootstrap = Bootstrap.builder()
            .withModuleClasses(InMemoryMessageModule.class, AttributeMapTransformModule.class)
            .withModules(new AbstractModule() {
               
               @Override
               protected void configure() {
                  bind(ResourceBundleDAO.class).to(EmptyResourceBundle.class);
               }
               
               @Provides @Named(PopulationService.PROP_THREADPOOL) @Singleton
               public Executor executor() {
                  return MoreExecutors.directExecutor();
               }
               
            })
            .build();
      ServiceLocator.init(GuiceServiceLocator.create(bootstrap.bootstrap()));

      bus = (InMemoryPlatformMessageBus)ServiceLocator.getInstance(PlatformMessageBus.class);

      populationList.add(makePopulation(Population.NAME_GENERAL));
      populationList.add(makePopulation(Population.NAME_QA));
      populationList.add(makePopulation(Population.NAME_BETA));

      populationDao = EasyMock.createMock(PopulationDAO.class);
      EasyMock.expect(populationDao.listPopulations()).andReturn(populationList).anyTimes();
      EasyMock.expect(populationDao.getDefaultPopulation()).andReturn(populationList.get(0)).anyTimes();
      EasyMock.replay(populationDao);

      Set<ContextualRequestMessageHandler<Population>> handlers = new HashSet<>();
      CapabilityRegistry registry = ServiceLocator.getInstance(CapabilityRegistry.class);
      handlers.add(new ListPopulationsHandler(populationDao));

      service = new PopulationService(
                     ServiceLocator.getInstance(PlatformMessageBus.class),
                     MoreExecutors.directExecutor(),
                     populationDao,
                     handlers);
      service.init();
   }

   @Test
   public void testGetPopulations() throws Exception {
      MessageBody request = com.arcussmarthome.messages.service.PopulationService.ListPopulationsRequest.instance();
      makeAndSendMessage(request);

      PlatformMessage takeMessage = bus.take();
      Assert.assertNotNull(takeMessage);

      MessageBody response = takeMessage.getValue();
      Assert.assertEquals(com.arcussmarthome.messages.service.PopulationService.ListPopulationsResponse.NAME, response.getMessageType());

      List<Map<String, Object>> populations = com.arcussmarthome.messages.service.PopulationService.ListPopulationsResponse.getPopulations(response);
      Assert.assertEquals(3, populations.size());

      for (int i = 0; i < 3; i++) {
      	Population actualPop = new Population(populations.get(i));
      	Assert.assertEquals(populationList.get(i).getName(), actualPop.getName());    
      	Assert.assertEquals(populationList.get(i).getDescription(), actualPop.getDescription());   
      	Assert.assertEquals(populationList.get(i).getMinHubV2Version(), actualPop.getMinHubV2Version());  
      	Assert.assertEquals(populationList.get(i).getIsDefault(), actualPop.getIsDefault());
      }
   }

   private void makeAndSendMessage(MessageBody request) {
      PlatformMessage sendMessage = PlatformMessage.builder()
            .from(Fixtures.createClientAddress())
            .to(Address.platformService(PlatformConstants.SERVICE_POPULATION))
            .withPayload(request)
            .isRequestMessage(true)
            .create();

      service.handleMessage(sendMessage);
   }

   private Population makePopulation(String name) {
      Population population = new Population();
      population.setName(name);
      population.setDescription(name + " population.");
      return population;
   }
}

