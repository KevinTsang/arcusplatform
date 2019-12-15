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
package com.arcussmarthome.common.subsystem.alarm.smoke;

import org.junit.Before;
import org.junit.Test;

import com.arcussmarthome.messages.capability.AlarmCapability;
import com.arcussmarthome.messages.capability.SmokeCapability;
import com.arcussmarthome.messages.model.Model;
import com.arcussmarthome.messages.model.serv.AlarmModel;

public class TestSmokeAlarm_Clearing extends SmokeAlarmTestCase {
	Model detected;
	Model safe;
	
	@Before
	public void bind() {
		detected = addSmokeDevice(SmokeCapability.SMOKE_DETECTED);
		safe = addSmokeDevice(SmokeCapability.SMOKE_SAFE);
		
		// need to pre-stage the system to be in clearing
		AlarmModel.setAlertState(SmokeAlarm.NAME, model, AlarmCapability.ALERTSTATE_CLEARING);
		AlarmModel.setDevices(SmokeAlarm.NAME, model, addressesOf(safe, detected));
		AlarmModel.setActiveDevices(SmokeAlarm.NAME, model, addressesOf(safe));
		AlarmModel.setTriggeredDevices(SmokeAlarm.NAME, model, addressesOf(detected));
		AlarmModel.setOfflineDevices(SmokeAlarm.NAME, model, addressesOf());
		
		alarm.bind(context);
		
		assertEquals(AlarmCapability.ALERTSTATE_CLEARING, AlarmModel.getAlertState(SmokeAlarm.NAME, model));
	}
	
	@Test
	public void testClearTrigger() {
		smokeClear(detected.getAddress());
		
		assertEquals(AlarmCapability.ALERTSTATE_READY, AlarmModel.getAlertState(SmokeAlarm.NAME, model));
		assertEquals(addressesOf(safe, detected),	AlarmModel.getDevices(SmokeAlarm.NAME, context.model()));
		assertEquals(addressesOf(safe, detected),	AlarmModel.getActiveDevices(SmokeAlarm.NAME, context.model()));
		assertEquals(addressesOf(), AlarmModel.getOfflineDevices(SmokeAlarm.NAME, context.model()));
		assertEquals(addressesOf(), AlarmModel.getTriggeredDevices(SmokeAlarm.NAME, context.model()));
	}
	
	@Test
	public void testDisconnectTrigger() {
		removeModel(detected);
		
		assertEquals(AlarmCapability.ALERTSTATE_READY, AlarmModel.getAlertState(SmokeAlarm.NAME, model));
		assertEquals(
				addressesOf(safe), 
				AlarmModel.getDevices(SmokeAlarm.NAME, context.model())
		);
		assertEquals(
				addressesOf(safe), 
				AlarmModel.getActiveDevices(SmokeAlarm.NAME, context.model())
		);
		assertEquals(addressesOf(), AlarmModel.getOfflineDevices(SmokeAlarm.NAME, context.model()));
		assertEquals(addressesOf(), AlarmModel.getTriggeredDevices(SmokeAlarm.NAME, context.model()));
	}
	
	@Test
	public void testRemoveTrigger() {
		removeModel(detected);
		
		assertEquals(AlarmCapability.ALERTSTATE_READY, AlarmModel.getAlertState(SmokeAlarm.NAME, model));
		assertEquals(
				addressesOf(safe), 
				AlarmModel.getDevices(SmokeAlarm.NAME, context.model())
		);
		assertEquals(
				addressesOf(safe), 
				AlarmModel.getActiveDevices(SmokeAlarm.NAME, context.model())
		);
		assertEquals(addressesOf(), AlarmModel.getOfflineDevices(SmokeAlarm.NAME, context.model()));
		assertEquals(addressesOf(), AlarmModel.getTriggeredDevices(SmokeAlarm.NAME, context.model()));
	}
	
	@Test
	public void testTriggerDevice() {
		smokeAlert(safe.getAddress());
		
		assertEquals(AlarmCapability.ALERTSTATE_ALERT, AlarmModel.getAlertState(SmokeAlarm.NAME, model));
		assertEquals(
				addressesOf(detected, safe), 
				AlarmModel.getDevices(SmokeAlarm.NAME, context.model())
		);
		assertEquals(addressesOf(), AlarmModel.getActiveDevices(SmokeAlarm.NAME, context.model()));
		assertEquals(addressesOf(), AlarmModel.getOfflineDevices(SmokeAlarm.NAME, context.model()));
		assertEquals(
				addressesOf(detected, safe), 
				AlarmModel.getTriggeredDevices(SmokeAlarm.NAME, context.model())
		);
	}
	
	@Test
	public void testAddSafeDevice() {
		Model clear = addSmokeDevice(SmokeCapability.SMOKE_SAFE);
		
		assertEquals(AlarmCapability.ALERTSTATE_CLEARING, AlarmModel.getAlertState(SmokeAlarm.NAME, model));
		assertEquals(addressesOf(safe, detected, clear), AlarmModel.getDevices(SmokeAlarm.NAME, context.model()));
		assertEquals(addressesOf(safe, clear), AlarmModel.getActiveDevices(SmokeAlarm.NAME, context.model()));
		assertEquals(addressesOf(), AlarmModel.getOfflineDevices(SmokeAlarm.NAME, context.model()));
		assertEquals(addressesOf(detected), AlarmModel.getTriggeredDevices(SmokeAlarm.NAME, context.model()));
	}
	
	@Test
	public void testAddTriggeredDevice() {
		Model trigger = addSmokeDevice(SmokeCapability.SMOKE_DETECTED);
		
		assertEquals(AlarmCapability.ALERTSTATE_ALERT, AlarmModel.getAlertState(SmokeAlarm.NAME, model));
		assertEquals(addressesOf(safe, detected, trigger), AlarmModel.getDevices(SmokeAlarm.NAME, context.model()));
		assertEquals(addressesOf(safe),	AlarmModel.getActiveDevices(SmokeAlarm.NAME, context.model()));
		assertEquals(addressesOf(), AlarmModel.getOfflineDevices(SmokeAlarm.NAME, context.model()));
		assertEquals(addressesOf(detected, trigger), AlarmModel.getTriggeredDevices(SmokeAlarm.NAME, context.model()));
	}

	@Test
	public void testAddOfflineDevice() {
		Model offline = addOfflineSmokeDevice(SmokeCapability.SMOKE_DETECTED);
		
		assertEquals(AlarmCapability.ALERTSTATE_CLEARING, AlarmModel.getAlertState(SmokeAlarm.NAME, model));
		assertEquals(addressesOf(safe, detected, offline), AlarmModel.getDevices(SmokeAlarm.NAME, context.model()));
		assertEquals(addressesOf(safe),	AlarmModel.getActiveDevices(SmokeAlarm.NAME, context.model()));
		assertEquals(addressesOf(offline), AlarmModel.getOfflineDevices(SmokeAlarm.NAME, context.model()));
		assertEquals(addressesOf(detected), AlarmModel.getTriggeredDevices(SmokeAlarm.NAME, context.model()));
	}
	
	@Test
	public void testCancelWhileTriggered() {
		cancel();
		
		assertEquals(AlarmCapability.ALERTSTATE_CLEARING, AlarmModel.getAlertState(SmokeAlarm.NAME, model));
		assertEquals(addressesOf(safe, detected), AlarmModel.getDevices(SmokeAlarm.NAME, context.model()));
		assertEquals(addressesOf(safe),	AlarmModel.getActiveDevices(SmokeAlarm.NAME, context.model()));
		assertEquals(addressesOf(), AlarmModel.getOfflineDevices(SmokeAlarm.NAME, context.model()));
		assertEquals(addressesOf(detected), AlarmModel.getTriggeredDevices(SmokeAlarm.NAME, context.model()));
	}

	@Test
	public void testCancelWhileClear() {
		smokeClear(detected.getAddress());
		cancel();
		
		assertEquals(AlarmCapability.ALERTSTATE_READY, AlarmModel.getAlertState(SmokeAlarm.NAME, model));
		assertEquals(addressesOf(safe, detected), AlarmModel.getDevices(SmokeAlarm.NAME, context.model()));
		assertEquals(addressesOf(safe, detected),	AlarmModel.getActiveDevices(SmokeAlarm.NAME, context.model()));
		assertEquals(addressesOf(), AlarmModel.getOfflineDevices(SmokeAlarm.NAME, context.model()));
		assertEquals(addressesOf(), AlarmModel.getTriggeredDevices(SmokeAlarm.NAME, context.model()));
	}

	@Test
	public void testCancelWhileTriggerOffline() {
		disconnect(detected.getAddress());
		cancel();
		
		// TODO should this go to CLEARING until the device has come back online AND gone to safe
		//      if not we risk it coming back online before its cleared and reporting a false clear
		assertEquals(AlarmCapability.ALERTSTATE_READY, AlarmModel.getAlertState(SmokeAlarm.NAME, model));
		assertEquals(addressesOf(safe, detected), AlarmModel.getDevices(SmokeAlarm.NAME, context.model()));
		assertEquals(addressesOf(safe), AlarmModel.getActiveDevices(SmokeAlarm.NAME, context.model()));
		assertEquals(addressesOf(detected), AlarmModel.getOfflineDevices(SmokeAlarm.NAME, context.model()));
		assertEquals(addressesOf(), AlarmModel.getTriggeredDevices(SmokeAlarm.NAME, context.model()));
	}

	@Test
	public void testCancelAfterTriggerRemoved() {
		removeModel(detected);
		cancel();
		
		assertEquals(AlarmCapability.ALERTSTATE_READY, AlarmModel.getAlertState(SmokeAlarm.NAME, model));
		assertEquals(addressesOf(safe), AlarmModel.getDevices(SmokeAlarm.NAME, context.model()));
		assertEquals(addressesOf(safe), AlarmModel.getActiveDevices(SmokeAlarm.NAME, context.model()));
		assertEquals(addressesOf(), AlarmModel.getOfflineDevices(SmokeAlarm.NAME, context.model()));
		assertEquals(addressesOf(), AlarmModel.getTriggeredDevices(SmokeAlarm.NAME, context.model()));
	}

}

