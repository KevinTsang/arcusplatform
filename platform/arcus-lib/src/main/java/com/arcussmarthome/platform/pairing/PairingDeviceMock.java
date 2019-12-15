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
package com.arcussmarthome.platform.pairing;

import java.util.Map;
import java.util.Set;

import com.google.common.collect.ImmutableSet;
import com.arcussmarthome.messages.capability.Capability;
import com.arcussmarthome.messages.capability.PairingDeviceCapability;
import com.arcussmarthome.messages.capability.PairingDeviceMockCapability;

public class PairingDeviceMock extends PairingDevice {
   private static final Set<String> capabilities = ImmutableSet.of(Capability.NAMESPACE, PairingDeviceCapability.NAMESPACE, PairingDeviceMockCapability.NAMESPACE);

   public PairingDeviceMock() {
      super();
      setAttribute(PairingDeviceMockCapability.ATTR_TARGETPRODUCTADDRESS, "");
      clearDirtyAttributes();
   }

   public PairingDeviceMock(Map<String, Object> attributes) {
      super(attributes);
      if(getAttribute(PairingDeviceMockCapability.ATTR_TARGETPRODUCTADDRESS) == null) {
         setAttribute(PairingDeviceMockCapability.ATTR_TARGETPRODUCTADDRESS, "");
         clearDirtyAttributes();
      }
   }

   public PairingDeviceMock(PairingDeviceMock copy) {
      super(copy);
   }

   @Override
   public Set<String> getCapabilities() {
      return capabilities;
   }

   @Override
   public PairingDeviceMock copy() {
      return new PairingDeviceMock(this);
   }

   @Override
   public int hashCode() {
      return 47 * super.hashCode();
   }

   // equals is inherited, the hash is changed so that PairingDevice & PairingDeviceMock don't have hash collisions
}

