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
package com.arcussmarthome.platform.subsystem.placemonitor.smarthomealert.generators;

import static com.arcussmarthome.platform.subsystem.placemonitor.smarthomealert.SmartHomeAlerts.CONTEXT_ATTR_HUBID;

import java.util.Set;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.inject.Singleton;
import com.arcussmarthome.common.subsystem.SubsystemContext;
import com.arcussmarthome.messages.address.Address;
import com.arcussmarthome.messages.capability.CellBackupSubsystemCapability;
import com.arcussmarthome.messages.capability.PlaceCapability;
import com.arcussmarthome.messages.model.Model;
import com.arcussmarthome.messages.model.subs.CellBackupSubsystemModel;
import com.arcussmarthome.messages.model.subs.PlaceMonitorSubsystemModel;
import com.arcussmarthome.messages.type.SmartHomeAlert;
import com.arcussmarthome.platform.subsystem.placemonitor.smarthomealert.AlertGenerator;
import com.arcussmarthome.platform.subsystem.placemonitor.smarthomealert.AlertKeys;
import com.arcussmarthome.platform.subsystem.placemonitor.smarthomealert.AlertScratchPad;
import com.arcussmarthome.platform.subsystem.placemonitor.smarthomealert.SmartHomeAlerts;

@Singleton
public class CellServiceErrorGenerator extends AlertGenerator {

   private static final Set<String> INTERESTING_ATTRS = ImmutableSet.of(
      CellBackupSubsystemCapability.ATTR_STATUS,
      CellBackupSubsystemCapability.ATTR_ERRORSTATE
   );

   @Override
   public void onStarted(SubsystemContext<PlaceMonitorSubsystemModel> context, AlertScratchPad scratch) {
      Model cellbackupModel = context.models().getModelByAddress(Address.platformService(context.getPlaceId(), CellBackupSubsystemCapability.NAMESPACE));
      handleModelChanged(context, cellbackupModel, scratch);
   }

   @Override
   protected boolean isInterestedInAttributeChange(String attribute) {
      return INTERESTING_ATTRS.contains(attribute);
   }

   @Override
   protected void handleModelChanged(SubsystemContext<PlaceMonitorSubsystemModel> context, Model model, AlertScratchPad scratchPad) {
      Model hub = SmartHomeAlerts.hubModel(context);
      if(hub == null) {
         context.logger().info("ignoring model changes for cellbackup because {} has not hub", context.getPlaceId());
         return;
      }

      if(CellBackupSubsystemModel.isStatusERRORED(model)) {
         String key = SmartHomeAlert.ALERTTYPE_PLACE_4G_SERVICE_ERROR;
         if(CellBackupSubsystemModel.isErrorStateDISABLED(model)) {
            scratchPad.removeAlert(AlertKeys.key(SmartHomeAlert.ALERTTYPE_PLACE_4G_SERVICE_ERROR, context.getPlaceId()));
            key = SmartHomeAlert.ALERTTYPE_PLACE_4G_SERVICE_SUSPENDED;
         } else {
            scratchPad.removeAlert(AlertKeys.key(SmartHomeAlert.ALERTTYPE_PLACE_4G_SERVICE_SUSPENDED, context.getPlaceId()));
         }
         scratchPad.putAlert(SmartHomeAlerts.create(
            key,
            SmartHomeAlert.SEVERITY_LOW,
            Address.platformService(context.getPlaceId(), PlaceCapability.NAMESPACE),
            ImmutableMap.of(CONTEXT_ATTR_HUBID, hub.getId()),
            context.getPlaceId()
         ));
      } else {
         scratchPad.removeAlert(AlertKeys.key(SmartHomeAlert.ALERTTYPE_PLACE_4G_SERVICE_SUSPENDED, context.getPlaceId()));
         scratchPad.removeAlert(AlertKeys.key(SmartHomeAlert.ALERTTYPE_PLACE_4G_SERVICE_ERROR, context.getPlaceId()));
      }
   }
}

