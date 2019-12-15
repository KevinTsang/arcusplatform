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
package com.arcussmarthome.agent.alarm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.arcussmarthome.agent.reflex.ReflexDevice;
import com.arcussmarthome.messages.capability.CarbonMonoxideCapability;
import com.arcussmarthome.messages.type.IncidentTrigger;

import static com.arcussmarthome.agent.alarm.AlarmEvents.*;

public class AlarmCo extends AbstractSafetyAlarm {
   private static final Logger log = LoggerFactory.getLogger(AlarmSecurity.class);
   public static final String NAME = "co";

   public AlarmCo(AlarmController parent) {
      super(parent, log, NAME);
   }

   @Override
   protected boolean isTriggerInteresting(TriggerEvent event) {
      return event.getTrigger() == Trigger.CO;
   }

   @Override
   protected boolean isSupported(ReflexDevice device) {
      return device.getCapabilities().contains(CarbonMonoxideCapability.NAME);
   }

   @Override
   protected boolean isTriggered(ReflexDevice device) {
      return CarbonMonoxideCapability.CO_DETECTED.equals(device.getAttribute(CarbonMonoxideCapability.ATTR_CO));
   }

   @Override
   protected String getIncidentAlarmType() {
      return IncidentTrigger.ALARM_CO;
   }

   @Override
   public String getName() {
      return NAME;
   }

   @Override
   public int getPriority() {
      return 2;

   }
}

