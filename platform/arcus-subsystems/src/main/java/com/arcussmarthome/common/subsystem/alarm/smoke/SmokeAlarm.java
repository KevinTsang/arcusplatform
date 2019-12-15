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

import com.arcussmarthome.common.subsystem.SubsystemContext;
import com.arcussmarthome.common.subsystem.alarm.generic.AlarmState;
import com.arcussmarthome.common.subsystem.alarm.generic.AlarmState.TriggerEvent;
import com.arcussmarthome.common.subsystem.alarm.generic.AlarmStateMachine;
import com.arcussmarthome.messages.capability.AlarmCapability;
import com.arcussmarthome.messages.capability.SmokeCapability;
import com.arcussmarthome.messages.model.Model;
import com.arcussmarthome.messages.model.subs.AlarmSubsystemModel;
import com.arcussmarthome.model.predicate.Predicates;

public class SmokeAlarm extends AlarmStateMachine<AlarmSubsystemModel> {
	public static final String NAME = "SMOKE";

	public SmokeAlarm() {
		super(
				NAME,
				Predicates.isA(SmokeCapability.NAMESPACE),
				Predicates.attributeEquals(SmokeCapability.ATTR_SMOKE, SmokeCapability.SMOKE_DETECTED)
		);
	}

	@Override
	protected TriggerEvent getTriggerType(SubsystemContext<AlarmSubsystemModel> context, Model model) {
		return TriggerEvent.SMOKE;
	}

	@Override
	protected AlarmState<? super AlarmSubsystemModel> state(String name) {
		switch(name) {
			case AlarmCapability.ALERTSTATE_ALERT: return SmokeAlertState.instance();
			default: return super.state(name);
		}
	}
}

