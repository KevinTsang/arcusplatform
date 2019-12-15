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
package com.arcussmarthome.driver.groovy.context;

import org.junit.After;
import org.junit.Before;

import com.google.inject.Inject;
import com.arcussmarthome.capability.registry.CapabilityRegistry;
import com.arcussmarthome.driver.DeviceDriverContext;
import com.arcussmarthome.driver.PlatformDeviceDriverContext;
import com.arcussmarthome.driver.groovy.DriverBinding;
import com.arcussmarthome.driver.groovy.GroovyContextObject;
import com.arcussmarthome.driver.groovy.GroovyDriverTestCase;
import com.arcussmarthome.messages.model.Device;
import com.arcussmarthome.messages.model.Fixtures;

public class GroovyAttributeDefinitionTestCase extends GroovyDriverTestCase {
   @Inject CapabilityRegistry registry;
   
   protected Device device;
   protected DriverBinding binding;
   protected DeviceDriverContext context;

   @Before
   public void setUp() throws Exception {
      super.setUp();
      device = Fixtures.createDevice();
      context = new PlatformDeviceDriverContext(device, factory.load("Metadata.driver"), mockPopulationCacheMgr);
      binding = new DriverBinding(registry, factory);
      GroovyContextObject.setContext(context);
   }

   @Override
   @After
   public void tearDown() throws Exception {
      GroovyContextObject.clearContext();
      super.tearDown();
   }
   
}

