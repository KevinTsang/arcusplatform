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
package com.arcussmarthome.driver.groovy;

import org.junit.Test;

import com.arcussmarthome.driver.DeviceDriverDefinition;
import com.arcussmarthome.messages.capability.Capability;
import com.arcussmarthome.messages.capability.DeviceAdvancedCapability;
import com.arcussmarthome.messages.capability.DeviceCapability;
import com.arcussmarthome.messages.capability.DeviceConnectionCapability;
import com.arcussmarthome.messages.capability.DevicePowerCapability;
import com.arcussmarthome.messages.capability.SwitchCapability;
import com.arcussmarthome.model.Version;
import com.arcussmarthome.util.IrisCollections;
import com.arcussmarthome.validators.ValidationException;

/**
 *
 */
public class TestGroovyDriverDefinition extends GroovyDriverTestCase {

   @Test
   public void testMetaDataDriver() throws Exception {
      DeviceDriverDefinition definition = factory.parse("Metadata.driver");
      assertEquals("Iris Nifty Switch", definition.getName());
      assertEquals("Driver for a nifty Iris switch", definition.getDescription());
      assertEquals(new Version(1), definition.getVersion());
      assertEquals(
         IrisCollections
            .setOf(
               registry.getCapabilityDefinitionByNamespace(DeviceCapability.NAMESPACE),
               registry.getCapabilityDefinitionByNamespace(DeviceAdvancedCapability.NAMESPACE),
               registry.getCapabilityDefinitionByNamespace(DeviceConnectionCapability.NAMESPACE),
               registry.getCapabilityDefinitionByNamespace(DevicePowerCapability.NAMESPACE),
               registry.getCapabilityDefinitionByNamespace(SwitchCapability.NAMESPACE),
               registry.getCapabilityDefinitionByNamespace(Capability.NAMESPACE)
            ),
            definition.getCapabilities()
      );
   }

   @Test
   public void testEmptyDriver() throws Exception {
      try {
         factory.parse("Empty.driver");
         fail("Allowed a driver with no name or capabilities to be defined");
      }
      catch(ValidationException e) {
         // expected
         e.printStackTrace(System.out);
      }
   }

}

