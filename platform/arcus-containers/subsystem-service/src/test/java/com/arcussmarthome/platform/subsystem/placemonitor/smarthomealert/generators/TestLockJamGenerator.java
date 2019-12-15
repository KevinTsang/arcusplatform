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

import static com.arcussmarthome.platform.subsystem.placemonitor.smarthomealert.SmartHomeAlerts.CONTEXT_ATTR_DEVICEID;
import static com.arcussmarthome.platform.subsystem.placemonitor.smarthomealert.SmartHomeAlerts.CONTEXT_ATTR_DEVICENAME;
import static com.arcussmarthome.platform.subsystem.placemonitor.smarthomealert.SmartHomeAlerts.CONTEXT_ATTR_DEVICETYPE;
import static com.arcussmarthome.platform.subsystem.placemonitor.smarthomealert.SmartHomeAlerts.CONTEXT_ATTR_DEVICEVENDOR;
import static com.arcussmarthome.platform.subsystem.placemonitor.smarthomealert.SmartHomeAlerts.CONTEXT_ATTR_PRODUCTCATALOGID;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableMap;
import com.arcussmarthome.messages.capability.DeviceAdvancedCapability;
import com.arcussmarthome.messages.type.SmartHomeAlert;
import com.arcussmarthome.platform.subsystem.placemonitor.smarthomealert.AlertKeys;
import com.arcussmarthome.platform.subsystem.placemonitor.smarthomealert.SmartHomeAlertTestCase;
import com.arcussmarthome.platform.subsystem.placemonitor.smarthomealert.SmartHomeAlerts;

public class TestLockJamGenerator extends SmartHomeAlertTestCase {

   private LockJamGenerator generator;

   @Override
   @Before
   public void setUp() throws Exception {
      super.setUp();
      generator = new LockJamGenerator(prodCatManager);
   }

   @Test
   public void testOnStartedAddsAlert() {
      replay();
      modelStore.updateModel(lock.getAddress(), ImmutableMap.of(DeviceAdvancedCapability.ATTR_ERRORS, ImmutableMap.of(LockJamGenerator.WARN_KEY, "jammed")));
      generator.onStarted(context, scratchPad);

      String key = AlertKeys.key(SmartHomeAlert.ALERTTYPE_DEV_WARN_LOCK_JAM, lock.getAddress());
      assertScratchPadHasAlert(key);

      SmartHomeAlert expected = createAlert();
      assertAlert(expected, scratchPad.getAlert(key));
   }

   @Test
   public void testHandleModelChangedAddsAlert() {
      replay();
      lock.setAttribute(DeviceAdvancedCapability.ATTR_ERRORS, ImmutableMap.of(LockJamGenerator.WARN_KEY, "jammed"));
      generator.handleModelChanged(context, lock, scratchPad);

      String key = AlertKeys.key(SmartHomeAlert.ALERTTYPE_DEV_WARN_LOCK_JAM, lock.getAddress());
      assertScratchPadHasAlert(key);

      SmartHomeAlert expected = createAlert();
      assertAlert(expected, scratchPad.getAlert(key));
   }

   @Test
   public void testHandleModelChangedClearsAlert() {
      replay();
      scratchPad.putAlert(createAlert());
      generator.handleModelChanged(context, lock, scratchPad);

      String key = AlertKeys.key(SmartHomeAlert.ALERTTYPE_DEV_WARN_LOCK_JAM, lock.getAddress());
      assertScratchPadNoAlert(key);
   }

   @Test
   public void testHandleModelChangedIgnoresNonLock() {
      replay();
      door.setAttribute(DeviceAdvancedCapability.ATTR_ERRORS, ImmutableMap.of(LockJamGenerator.WARN_KEY, "jammed"));
      generator.handleModelChanged(context, door, scratchPad);

      String key = AlertKeys.key(SmartHomeAlert.ALERTTYPE_DEV_WARN_LOCK_JAM, door.getAddress());
      assertScratchPadNoAlert(key);
   }

   private SmartHomeAlert createAlert() {
      return SmartHomeAlerts.create(
         SmartHomeAlert.ALERTTYPE_DEV_WARN_LOCK_JAM,
         SmartHomeAlert.SEVERITY_CRITICAL,
         lock.getAddress(),
         ImmutableMap.<String, Object>builder()
            .put(CONTEXT_ATTR_DEVICEID, lock.getId())
            .put(CONTEXT_ATTR_DEVICENAME, "")
            .put(CONTEXT_ATTR_DEVICETYPE, "Door Lock")
            .put(CONTEXT_ATTR_DEVICEVENDOR, "Test")
            .put(CONTEXT_ATTR_PRODUCTCATALOGID, entry.getId())
            .build(),
         PLACE_ID
      );
   }
}

