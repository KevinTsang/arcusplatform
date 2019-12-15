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
package com.arcussmarthome.platform.subsystem.placemonitor.offlinenotifications;

import static com.arcussmarthome.messages.capability.PlaceMonitorSubsystemCapability.ATTR_DEADBATTERYNOTIFICATIONSENT;

import java.util.Date;
import java.util.Map;

import com.google.inject.Singleton;
import com.arcussmarthome.common.subsystem.SubsystemContext;
import com.arcussmarthome.messages.model.Model;
import com.arcussmarthome.messages.model.subs.PlaceMonitorSubsystemModel;

@Singleton
public class DeadBatteryNotificationsHandler extends AbstractLowBatteryNotificationsHandler
{
   @Override
   protected String getNotificationSentAttribute()
   {
      return ATTR_DEADBATTERYNOTIFICATIONSENT;
   }

   @Override
   protected int getNotificationThreshold(String productId)
   {
      return notificationThresholdsConfig.get().getBatteryDead(productId);
   }

   @Override
   protected int getNextNotificationThreshold(String productId)
   {
      // Since "dead" is the lowest notification level, just return it as the "next" one here.  Subtract 1 so that the
      // "batteryLevel > nextNotificationThreshold" in BatteryNotificationsHandler.onDeviceBatteryChange() works
      // correctly.
      return notificationThresholdsConfig.get().getBatteryDead(productId) - 1;
   }

   @Override
   protected int getNotificationClearThreshold(String productId)
   {
      return notificationThresholdsConfig.get().getBatteryDeadClear(productId);
   }

   @Override
   protected Map<String, Date> getSentNotificationsDeviceMap(PlaceMonitorSubsystemModel model)
   {
      return model.getDeadBatteryNotificationSent();
   }

   @Override
   protected void sendNotification(Model device, SubsystemContext<PlaceMonitorSubsystemModel> context)
   {
      notifier.sendDeviceHasADeadRechargeableBattery(device, context);
   }
}

