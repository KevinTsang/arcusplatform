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
package com.arcussmarthome.platform.subsystem.pairing;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.arcussmarthome.messages.MessageBody;
import com.arcussmarthome.messages.capability.PairingSubsystemCapability.DismissAllResponse;
import com.arcussmarthome.messages.capability.PairingSubsystemCapability.FactoryResetResponse;
import com.arcussmarthome.messages.capability.PairingSubsystemCapability.ListHelpStepsResponse;
import com.arcussmarthome.messages.capability.PairingSubsystemCapability.SearchResponse;
import com.arcussmarthome.messages.capability.PairingSubsystemCapability.StopSearchingResponse;
import com.arcussmarthome.messages.model.Model;
import com.arcussmarthome.platform.subsystem.pairing.state.PairingSubsystemTestCase;

public class TestPairingSteps_Hub extends PairingSubsystemTestCase {
	private Model hub;
	
	@Before
	public void stagePairingSteps() throws Exception {
		expectLoadProductAndReturn(productZigbee()).anyTimes();
		expectCurrentProductAndReturn(productZigbee()).anyTimes();
		replay();
		
		hub = stagePairingStepsHub();
		assertPairingStepsHub(productAddress);
		assertNoRequests();
	}
	
	@Test
	public void testSearch() throws Exception {
		// send the request
		{
			Optional<MessageBody> response = search();
			// no response until the hub responds
			assertEquals(Optional.empty(), response);
			assertPairingStepsHub(productAddress);
		}
		// complete the action
		{
			SendAndExpect request = popRequest();
			assertStartPairingRequestSent(hub.getAddress(), request);
			request.getAction().onResponse(context, buildStartPairingResponse(hub.getAddress()));
			MessageBody response = responses.getValues().get(responses.getValues().size() - 1);
			assertEquals(SearchResponse.NAME, response.getMessageType());
			assertEquals(SearchResponse.MODE_HUB, SearchResponse.getMode(response));
			assertSearchingHubNotFound(productAddress);
		}
	}
	
	@Test
	public void testSearchHubOffline() throws Exception {
		// send the request
		{
			Optional<MessageBody> response = search();
			// no response until the hub responds
			assertEquals(Optional.empty(), response);
			assertPairingStepsHub(productAddress);
		}
		// complete the action
		{
			SendAndExpect request = popRequest();
			assertStartPairingRequestSent(hub.getAddress(), request);
			request.getAction().onTimeout(context);
			MessageBody response = responses.getValues().get(responses.getValues().size() - 1);
			assertError(SearchResponse.CODE_HUB_OFFLINE, response);
			assertPairingStepsHub(productAddress);
		}
	}
	
	@Test
	public void testListHelpSteps() {
		MessageBody response = listHelpSteps().get();
		assertEquals(ListHelpStepsResponse.NAME, response.getMessageType());
		assertPairingStepsHub(productAddress);
	}

	@Test
	public void testDismissAll() {
		MessageBody response = dismissAll().get();
		assertEquals(DismissAllResponse.NAME, response.getMessageType());
		assertEquals(ImmutableList.of(), response.getAttributes().get(DismissAllResponse.ATTR_ACTIONS));
		assertStopPairingRequestSent(hub.getAddress());
		assertIdle();
	}

	@Test
	public void testFactoryResetZigbee() {
		MessageBody response = factoryReset().get();
		assertEquals(FactoryResetResponse.NAME, response.getMessageType());
		assertStopPairingRequestSent(hub.getAddress());
		assertFactoryResetIdle(productAddress);
	}

	@Test
	public void testFactoryResetZWave() {
		reset();
		expectLoadProductAndReturn(productZWave()).anyTimes();
		expectCurrentProductAndReturn(productZWave()).anyTimes();
		replay();
		
		// send the request
		{
			Optional<MessageBody> response = factoryReset();
			// no response until the hub responds
			assertEquals(Optional.empty(), response);
			assertPairingStepsHub(productAddress);
			assertStopPairingRequestSent(hub.getAddress());
		}
		// complete the action
		{
			SendAndExpect request = popRequest();
			assertStartUnpairingRequestSent(hub.getAddress(), request);
			request.getAction().onResponse(context, buildStartUnpairingResponse(hub.getAddress()));
			MessageBody response = responses.getValues().get(responses.getValues().size() - 1);
			assertEquals(FactoryResetResponse.NAME, response.getMessageType());
			assertEquals(1, FactoryResetResponse.getSteps(response).size());
			assertFactoryResetZWave(productAddress);
		}
	}

	@Test
	public void testStopSearching() {
		MessageBody response = stopSearching().get();
		assertEquals(StopSearchingResponse.instance(), response);
		assertStopPairingRequestSent(hub.getAddress());
		assertIdle();
	}

	@Test
	public void testTimeout() {
		sendTimeout();
		assertStopPairingRequestSent(hub.getAddress());
		assertIdle();
	}

}

