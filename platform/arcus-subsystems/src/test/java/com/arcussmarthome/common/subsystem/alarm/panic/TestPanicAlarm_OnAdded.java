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
package com.arcussmarthome.common.subsystem.alarm.panic;

import org.junit.Test;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.arcussmarthome.messages.capability.AlarmCapability;
import com.arcussmarthome.messages.model.Model;
import com.arcussmarthome.messages.model.serv.AlarmModel;

public class TestPanicAlarm_OnAdded extends PanicAlarmTestCase {

	@Test
	public void testInitState() {
		alarm.bind(context);
		
		assertEquals(ImmutableMap.of(PanicAlarm.NAME, ImmutableSet.of(AlarmCapability.NAMESPACE)), model.getInstances());
		assertEquals(AlarmCapability.ALERTSTATE_INACTIVE, AlarmModel.getAlertState(PanicAlarm.NAME, model));
		assertEquals(addressesOf(), AlarmModel.getDevices(PanicAlarm.NAME, context.model()));
		assertEquals(addressesOf(), AlarmModel.getActiveDevices(PanicAlarm.NAME, context.model()));
		assertEquals(addressesOf(), AlarmModel.getOfflineDevices(PanicAlarm.NAME, context.model()));
		assertEquals(addressesOf(), AlarmModel.getTriggeredDevices(PanicAlarm.NAME, context.model()));
	}
	
	@Test
	public void testAddKeypad() {
		Model keypad = addKeyPad();
		alarm.bind(context);
		
		assertEquals(AlarmCapability.ALERTSTATE_READY, AlarmModel.getAlertState(PanicAlarm.NAME, model));
		assertEquals(addressesOf(keypad), AlarmModel.getDevices(PanicAlarm.NAME, context.model()));
		assertEquals(addressesOf(keypad), AlarmModel.getActiveDevices(PanicAlarm.NAME, context.model()));
		assertEquals(addressesOf(), AlarmModel.getOfflineDevices(PanicAlarm.NAME, context.model()));
		assertEquals(addressesOf(), AlarmModel.getTriggeredDevices(PanicAlarm.NAME, context.model()));
	}

	@Test
	public void testAddRule() {
		Model rule = addRule("button-panic");
		alarm.bind(context);
		
		assertEquals(AlarmCapability.ALERTSTATE_READY, AlarmModel.getAlertState(PanicAlarm.NAME, model));
		assertEquals(addressesOf(rule), AlarmModel.getDevices(PanicAlarm.NAME, context.model()));
		assertEquals(addressesOf(rule), AlarmModel.getActiveDevices(PanicAlarm.NAME, context.model()));
		assertEquals(addressesOf(), AlarmModel.getOfflineDevices(PanicAlarm.NAME, context.model()));
		assertEquals(addressesOf(), AlarmModel.getTriggeredDevices(PanicAlarm.NAME, context.model()));
	}

	@Test
	public void testAddWrongRule() {
		Model rule = addRule("button-chime");
		alarm.bind(context);
		
		assertEquals(AlarmCapability.ALERTSTATE_INACTIVE, AlarmModel.getAlertState(PanicAlarm.NAME, model));
		assertEquals(addressesOf(), AlarmModel.getDevices(PanicAlarm.NAME, context.model()));
		assertEquals(addressesOf(), AlarmModel.getActiveDevices(PanicAlarm.NAME, context.model()));
		assertEquals(addressesOf(), AlarmModel.getOfflineDevices(PanicAlarm.NAME, context.model()));
		assertEquals(addressesOf(), AlarmModel.getTriggeredDevices(PanicAlarm.NAME, context.model()));
	}

}

