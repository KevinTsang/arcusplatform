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
package com.arcussmarthome.driver.groovy.scheduler;

import com.arcussmarthome.driver.DeviceDriver;
import com.arcussmarthome.driver.capability.Capability;
import com.arcussmarthome.driver.groovy.DriverBinding;
import com.arcussmarthome.driver.groovy.binding.CapabilityEnvironmentBinding;
import com.arcussmarthome.driver.groovy.binding.EnvironmentBinding;
import com.arcussmarthome.driver.groovy.plugin.GroovyDriverPlugin;

public class SchedulerPlugin implements GroovyDriverPlugin {

   public SchedulerPlugin() {
   }

   @Override
   public void enhanceEnvironment(EnvironmentBinding binding) {
      binding.setProperty("onEvent", new OnScheduledClosure(binding));
      binding.setProperty("Scheduler", new SchedulerContext());

   }

   @Override
   public void postProcessEnvironment(EnvironmentBinding binding) {
      // no-op
   }

   @Override
   public void enhanceDriver(DriverBinding bindings, DeviceDriver driver) {
      // no-op
   }

   @Override
   public void enhanceCapability(CapabilityEnvironmentBinding bindings, Capability capability) {
      // no-op
   }

}

