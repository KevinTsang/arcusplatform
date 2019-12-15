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
package com.arcussmarthome.agent.addressing;

import org.eclipse.jdt.annotation.Nullable;

public final class HubProtocolAddress implements HubAddr {
   private final String protocol;

   public HubProtocolAddress(String protocol) {
      this.protocol = protocol;
   }

   @Override
   public @Nullable String getServiceId() {
      return null;
   }

   @Override
   public @Nullable String getProtocolId() {
      return protocol;
   }

   @Override
   public boolean isPlatform() {
      return false;
   }

   @Override
   public boolean isPlatformBroadcast() {
      return false;
   }

   @Override
   public String toString() {
      return "ProtocolAddress [" +
         "addr=" + protocol +
         "]";
   }
}

