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
package com.arcussmarthome.platform.subsystem.cellbackup;

import org.junit.Test;

import com.arcussmarthome.messages.capability.CellBackupSubsystemCapability;
import com.arcussmarthome.messages.model.test.ModelFixtures;

public class TestCellBackupSubsystem_Ready extends CellBackupSubsystemTestCase {

   @Test
   public void testReadyAtStart() {
      addModel(createHubWithDongle());
      start(true);
      assertState(CellBackupSubsystemCapability.STATUS_READY, CellBackupSubsystemCapability.NOTREADYSTATE_BOTH, CellBackupSubsystemCapability.ERRORSTATE_NONE);
   }

   @Test
   public void testReadyAtStartPromon() {
      addModel(createHubWithDongle());
      startPromon();
      assertState(CellBackupSubsystemCapability.STATUS_READY, CellBackupSubsystemCapability.NOTREADYSTATE_BOTH, CellBackupSubsystemCapability.ERRORSTATE_NONE);
   }

   @Test
   public void testReadyByAddingDongle() {
      addModel(ModelFixtures.createHubAttributes());
      start(true);
      assertState(CellBackupSubsystemCapability.STATUS_NOTREADY, CellBackupSubsystemCapability.NOTREADYSTATE_NEEDSMODEM, CellBackupSubsystemCapability.ERRORSTATE_NONE);
      insertGoodDongle();
      assertState(CellBackupSubsystemCapability.STATUS_READY, CellBackupSubsystemCapability.NOTREADYSTATE_NEEDSMODEM, CellBackupSubsystemCapability.ERRORSTATE_NONE);
   }

   @Test
   public void testReadyByAddingDonglePromon() {
      addModel(ModelFixtures.createHubAttributes());
      startPromon();
      assertState(CellBackupSubsystemCapability.STATUS_NOTREADY, CellBackupSubsystemCapability.NOTREADYSTATE_NEEDSMODEM, CellBackupSubsystemCapability.ERRORSTATE_NONE);
      insertGoodDongle();
      assertState(CellBackupSubsystemCapability.STATUS_READY, CellBackupSubsystemCapability.NOTREADYSTATE_NEEDSMODEM, CellBackupSubsystemCapability.ERRORSTATE_NONE);
   }

   @Test
   public void testReadyByAddOn() {
      addModel(createHubWithDongle());
      start(false);
      assertState(CellBackupSubsystemCapability.STATUS_NOTREADY, CellBackupSubsystemCapability.NOTREADYSTATE_NEEDSSUB, CellBackupSubsystemCapability.ERRORSTATE_NONE);
      enableAddOn();
      assertState(CellBackupSubsystemCapability.STATUS_READY, CellBackupSubsystemCapability.NOTREADYSTATE_NEEDSSUB, CellBackupSubsystemCapability.ERRORSTATE_NONE);
   }

   @Test
   public void testReadyByPromon() {
      addModel(createHubWithDongle());
      start(false);
      assertState(CellBackupSubsystemCapability.STATUS_NOTREADY, CellBackupSubsystemCapability.NOTREADYSTATE_NEEDSSUB, CellBackupSubsystemCapability.ERRORSTATE_NONE);
      enablePromon();
      assertState(CellBackupSubsystemCapability.STATUS_READY, CellBackupSubsystemCapability.NOTREADYSTATE_NEEDSSUB, CellBackupSubsystemCapability.ERRORSTATE_NONE);
   }

   @Test
   public void testReadyByInsertSim() {
      addModel(createHubWithDongleNoSim());
      start(true);
      assertState(CellBackupSubsystemCapability.STATUS_ERRORED, CellBackupSubsystemCapability.NOTREADYSTATE_BOTH, CellBackupSubsystemCapability.ERRORSTATE_NOSIM);
      insertSim();
      assertState(CellBackupSubsystemCapability.STATUS_READY, CellBackupSubsystemCapability.NOTREADYSTATE_BOTH, CellBackupSubsystemCapability.ERRORSTATE_NONE);
   }

   @Test
   public void testReadyByInsertSimPromon() {
      addModel(createHubWithDongleNoSim());
      startPromon();
      assertState(CellBackupSubsystemCapability.STATUS_ERRORED, CellBackupSubsystemCapability.NOTREADYSTATE_BOTH, CellBackupSubsystemCapability.ERRORSTATE_NOSIM);
      insertSim();
      assertState(CellBackupSubsystemCapability.STATUS_READY, CellBackupSubsystemCapability.NOTREADYSTATE_BOTH, CellBackupSubsystemCapability.ERRORSTATE_NONE);
   }

   @Test
   public void testReadyByProvisionSim() {
      addModel(createHubWithDongleUnprovisionedSim());
      start(true);
      assertState(CellBackupSubsystemCapability.STATUS_ERRORED, CellBackupSubsystemCapability.NOTREADYSTATE_BOTH, CellBackupSubsystemCapability.ERRORSTATE_NOTPROVISIONED);
      provisionSim();
      assertState(CellBackupSubsystemCapability.STATUS_READY, CellBackupSubsystemCapability.NOTREADYSTATE_BOTH, CellBackupSubsystemCapability.ERRORSTATE_NONE);
   }

   @Test
   public void testReadyByProvisionSimPromon() {
      addModel(createHubWithDongleUnprovisionedSim());
      startPromon();
      assertState(CellBackupSubsystemCapability.STATUS_ERRORED, CellBackupSubsystemCapability.NOTREADYSTATE_BOTH, CellBackupSubsystemCapability.ERRORSTATE_NOTPROVISIONED);
      provisionSim();
      assertState(CellBackupSubsystemCapability.STATUS_READY, CellBackupSubsystemCapability.NOTREADYSTATE_BOTH, CellBackupSubsystemCapability.ERRORSTATE_NONE);
   }
}

