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
package com.arcussmarthome.platform.subsystem.incident;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import org.easymock.EasyMock;
import org.easymock.IExpectationSetters;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import com.arcussmarthome.common.subsystem.SubsystemExecutor;
import com.arcussmarthome.core.messaging.memory.InMemoryMessageModule;
import com.arcussmarthome.core.messaging.memory.InMemoryPlatformMessageBus;
import com.arcussmarthome.messages.ErrorEvent;
import com.arcussmarthome.messages.PlatformMessage;
import com.arcussmarthome.messages.capability.AlarmIncidentCapability.ListHistoryEntriesRequest;
import com.arcussmarthome.messages.capability.AlarmIncidentCapability.ListHistoryEntriesResponse;
import com.arcussmarthome.messages.capability.AlarmIncidentCapability.VerifyRequest;
import com.arcussmarthome.messages.capability.Capability;
import com.arcussmarthome.messages.errors.Errors;
import com.arcussmarthome.messages.model.Fixtures;
import com.arcussmarthome.messages.model.ServiceLevel;
import com.arcussmarthome.messages.type.Population;
import com.arcussmarthome.platform.PagedResults;
import com.arcussmarthome.platform.alarm.incident.AlarmIncident;
import com.arcussmarthome.platform.alarm.incident.AlarmIncidentDAO;
import com.arcussmarthome.platform.history.HistoryLogDAO;
import com.arcussmarthome.platform.subsystem.SubsystemRegistry;
import com.arcussmarthome.population.PlacePopulationCacheManager;
import com.arcussmarthome.test.IrisMockTestCase;
import com.arcussmarthome.test.Mocks;
import com.arcussmarthome.test.Modules;
import com.arcussmarthome.util.ThreadPoolBuilder;

@Modules(InMemoryMessageModule.class)
@Mocks(value={
		AlarmIncidentDAO.class,
		AlarmIncidentHistoryListener.class,
		HistoryLogDAO.class,
		SubsystemExecutor.class,
		SubsystemRegistry.class,
		PlatformAlarmIncidentService.class, 
		PlacePopulationCacheManager.class
})
public class TestAlarmIncidentServiceImpl extends IrisMockTestCase {
	@Inject InMemoryPlatformMessageBus messageBus;
	@Inject SubsystemRegistry registry;
	@Inject SubsystemExecutor executor;
	@Inject AlarmIncidentDAO alarmIncidentDao;
	@Inject HistoryLogDAO historyLogDao;
	
	@Inject AlarmIncidentServiceImpl service;
	@Inject protected PlacePopulationCacheManager mockPopulationCacheMgr;
	
	UUID placeId = UUID.randomUUID();
	AlarmIncident incident = IncidentFixtures.createPreAlert(ServiceLevel.PREMIUM_PROMON);
	
	@Provides @Named(AlarmIncidentServiceImpl.NAME_EXECUTOR_POOL)
	public ExecutorService executor() {
		return ThreadPoolBuilder.newSingleThreadedScheduler("test-alarm-incident");
	}
	
	@Before
	public void setupMocks() {
   	EasyMock.expect(mockPopulationCacheMgr.getPopulationByPlaceId(EasyMock.anyObject(UUID.class))).andReturn(Population.NAME_GENERAL).anyTimes();
   }	
	
	protected PlatformMessage getAttributes() {
		return 
				PlatformMessage
					.request(incident.getAddress())
					.from(Fixtures.createClientAddress())
					.withPlaceId(placeId)
					.withPayload(Capability.CMD_GET_ATTRIBUTES)
					.create();
	}
	
	protected PlatformMessage verifyRequest() {
		return 
				PlatformMessage
					.request(incident.getAddress())
					.from(Fixtures.createClientAddress())
					.withPlaceId(placeId)
					.withPayload(VerifyRequest.instance())
					.create();
	}
	
	protected PlatformMessage listHistory() {
		return 
				PlatformMessage
					.request(incident.getAddress())
					.from(Fixtures.createClientAddress())
					.withPlaceId(placeId)
					.withPayload(ListHistoryEntriesRequest.builder().build())
					.create();
	}
	
	protected IExpectationSetters<Optional<SubsystemExecutor>> expectGetSubsystem() {
		return
			EasyMock
				.expect(registry.loadByPlace(placeId));
	}
	
	protected IExpectationSetters<AlarmIncident> expectFindAlarmIncidentById() {
		return
			EasyMock
				.expect(alarmIncidentDao.findById(placeId, incident.getId()));
	}
	
	@After
	public void verify() {
		super.verify();
	}
	
	@Test
	public void testGetAttributes() throws Exception {
		expectFindAlarmIncidentById().andReturn(incident);
		PlatformMessage request = getAttributes();
		replay();
		
		service.handleMessage(request);
		
		PlatformMessage message = messageBus.take();
		assertEquals(Capability.EVENT_GET_ATTRIBUTES_RESPONSE, message.getMessageType());
		
		verify();
	}
	
	@Test
	public void testGetAttributesAlarmIncidentNotFound() throws Exception {
		expectFindAlarmIncidentById().andReturn(null);
		PlatformMessage request = getAttributes();
		replay();
		
		service.handleMessage(request);
		
		PlatformMessage message = messageBus.take();
		assertEquals(ErrorEvent.MESSAGE_TYPE, message.getMessageType());
		assertEquals(Errors.CODE_NOT_FOUND, message.getValue().getAttributes().get(ErrorEvent.CODE_ATTR));
		
		verify();
	}

	@Test
	public void testGetAttributesAlarmIncidentException() throws Exception {
		expectFindAlarmIncidentById().andThrow(new RuntimeException("BOOM"));
		PlatformMessage request = getAttributes();
		replay();
		
		service.handleMessage(request);
		
		PlatformMessage message = messageBus.take();
		assertEquals(ErrorEvent.MESSAGE_TYPE, message.getMessageType());
	}

	@Test
	public void testSubsystemExecutorFound() throws Exception {
		CountDownLatch latch = new CountDownLatch(1);
		expectGetSubsystem().andReturn(Optional.of(executor));
		PlatformMessage request = verifyRequest();
		executor.onPlatformMessage(request);
		EasyMock.expectLastCall().andAnswer(() -> {
			latch.countDown();
			return null;
		});
		replay();
		
		service.handleMessage(request);
		
		assertTrue(latch.await(100, TimeUnit.MILLISECONDS));
		assertEquals(null, messageBus.poll());
	}
	
	@Test
	public void testSubsystemExecutorNotFound() throws Exception {
		expectGetSubsystem().andReturn(Optional.empty());
		PlatformMessage request = verifyRequest();
		replay();
		
		service.handleMessage(request);
		
		PlatformMessage message = messageBus.take();
		assertEquals(ErrorEvent.MESSAGE_TYPE, message.getMessageType());
		assertEquals(Errors.CODE_NOT_FOUND, message.getValue().getAttributes().get(ErrorEvent.CODE_ATTR));
	}
	
	@Test
	public void testListHistoryEntries() throws Exception {
		PlatformMessage request = listHistory();
		EasyMock
			.expect(historyLogDao.listEntriesByQuery(EasyMock.notNull()))
			.andReturn(PagedResults.emptyPage());
					
		replay();
		
		service.handleMessage(request);
		
		PlatformMessage message = messageBus.take();
		assertEquals(ListHistoryEntriesResponse.NAME, message.getMessageType());
	}
}

