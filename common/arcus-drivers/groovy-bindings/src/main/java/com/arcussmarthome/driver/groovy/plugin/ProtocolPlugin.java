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
package com.arcussmarthome.driver.groovy.plugin;

import java.util.List;
import java.util.Map;

import com.arcussmarthome.device.model.AttributeDefinition;
import com.arcussmarthome.driver.DeviceDriver;
import com.arcussmarthome.driver.DriverConstants;
import com.arcussmarthome.driver.capability.Capability;
import com.arcussmarthome.driver.groovy.DriverBinding;
import com.arcussmarthome.driver.groovy.binding.CapabilityEnvironmentBinding;
import com.arcussmarthome.driver.groovy.binding.EnvironmentBinding;
import com.arcussmarthome.driver.handler.ContextualEventHandler;
import com.arcussmarthome.driver.metadata.EventMatcher;
import com.arcussmarthome.messages.PlatformMessage;
import com.arcussmarthome.messages.model.Device;
import com.arcussmarthome.protocol.Protocol;
import com.arcussmarthome.protocol.ProtocolMessage;
import com.arcussmarthome.protocol.RemoveProtocolRequest;

/**
 * Base class for plugins that expose Protocol specific functionality.
 */
public abstract class ProtocolPlugin implements GroovyDriverPlugin {

   @Override
   public void enhanceEnvironment(EnvironmentBinding binding) {
      if(binding instanceof DriverBinding) {
         ((DriverBinding) binding).getBuilder().addMatchAttributes(getMatcherAttributes());
      }
      addRootProperties(binding);
   }

   @SuppressWarnings({ "unchecked", "rawtypes" })
   @Override
   public void postProcessEnvironment(EnvironmentBinding binding) {
      final ContextualEventHandler<ProtocolMessage> handler = createHandler(binding.getBuilder().getEventMatchers());
      if(handler != null) {
         binding.getBuilder().addProtocolHandler(getProtocol().getNamespace(), (ContextualEventHandler) handler);
      }
   }

   @Override
   public void enhanceDriver(DriverBinding binding, DeviceDriver driver) {
      String protocolName = driver.getBaseAttributes().get(DriverConstants.DEVADV_ATTR_PROTOCOL);
      if(protocolName != null && protocolName.equals(getProtocol().getNamespace())) {
         addContextProperties(binding);
      }
   }

   @Override
   public void enhanceCapability(CapabilityEnvironmentBinding binding, Capability capability) {
      addContextProperties(binding);
   }
   
   // FIXME this should be on the driver where we have full context
   public PlatformMessage handleRemove(Device device, long duration, boolean force) {
      RemoveProtocolRequest request = new RemoveProtocolRequest(device);
      request.setTimeoutMs(duration);
      request.setForceRemove(force);
      return getProtocol().remove(request);
   }

   protected abstract void addRootProperties(EnvironmentBinding binding);

   protected abstract void addContextProperties(EnvironmentBinding binding);

   public abstract Protocol<?> getProtocol();

   /**
    * The set of {@link AttributeDefinition}s that will
    * be provided by this protocol on startup.
    * @return
    */
   public abstract Map<String, AttributeDefinition> getMatcherAttributes();

   public abstract ContextualEventHandler<ProtocolMessage> createHandler(List<EventMatcher> matcher);

}

