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
/**
 *
 */
package com.arcussmarthome.registry;

import org.junit.Test;

import com.google.inject.Inject;
import com.arcussmarthome.capability.definition.CapabilityDefinition;
import com.arcussmarthome.capability.definition.DefinitionRegistry;
import com.arcussmarthome.capability.definition.ServiceDefinition;
import com.arcussmarthome.capability.registry.CapabilityRegistryModule;
import com.arcussmarthome.messages.capability.Capability;
import com.arcussmarthome.messages.capability.DeviceCapability;
import com.arcussmarthome.messages.capability.SubsystemCapability;
import com.arcussmarthome.messages.service.PersonService;
import com.arcussmarthome.messages.service.SubsystemService;
import com.arcussmarthome.messages.type.HistoryLog;
import com.arcussmarthome.test.IrisTestCase;
import com.arcussmarthome.test.Modules;

/**
 * Spot checks some well known capabilities / etc
 */
@Modules(CapabilityRegistryModule.class)
public class TestClasspathDefinitionRegistry extends IrisTestCase {

   @Inject DefinitionRegistry registry;

   @Test
   public void testGetCapability() {
      assertEquals(Capability.DEFINITION, registry.getCapability(Capability.NAME));
      assertEquals(Capability.DEFINITION, registry.getCapability(Capability.NAMESPACE));

      CapabilityDefinition device = registry.getCapability(DeviceCapability.NAME);
      assertNotNull(device);
      assertEquals(device, registry.getCapability(DeviceCapability.NAMESPACE));

      CapabilityDefinition subsystem = registry.getCapability(SubsystemCapability.NAME);
      assertNotNull(subsystem);
      assertEquals(subsystem, registry.getCapability(SubsystemCapability.NAMESPACE));
   }

   @Test
   public void testGetService() {
      ServiceDefinition personService = registry.getService(PersonService.NAME);
      assertNotNull(personService);
      assertEquals(personService, registry.getService(PersonService.NAMESPACE));

      ServiceDefinition subsystemService = registry.getService(SubsystemService.NAME);
      assertNotNull(subsystemService);
      assertEquals(subsystemService, registry.getService(SubsystemService.NAMESPACE));
   }

   @Test
   public void testGetType() {
      assertNotNull(registry.getStruct(HistoryLog.NAME));
   }

   @Test
   public void testGetAttribute() {
      assertNotNull(registry.getAttribute(Capability.ATTR_ID));
      assertNotNull(registry.getAttribute(Capability.ATTR_ADDRESS));
      assertNotNull(registry.getAttribute(Capability.ATTR_CAPS));
      assertNotNull(registry.getAttribute(Capability.ATTR_TYPE));
      assertNotNull(registry.getAttribute(Capability.ATTR_TAGS));
      assertNotNull(registry.getAttribute(Capability.ATTR_IMAGES));
      assertNotNull(registry.getAttribute(Capability.ATTR_INSTANCES));

      assertNotNull(registry.getAttribute(DeviceCapability.ATTR_NAME));

      assertNotNull(registry.getAttribute(SubsystemCapability.ATTR_NAME));
   }
}


