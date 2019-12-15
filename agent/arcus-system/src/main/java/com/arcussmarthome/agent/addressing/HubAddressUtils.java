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

import com.arcussmarthome.messages.address.Address;

public final class HubAddressUtils {
   private HubAddressUtils() {
   }

   public static HubPlatformBroadcastAddress platformBroadcast() {
      return HubPlatformBroadcastAddress.INSTANCE;
   }

   public static HubServiceAddress service(String service) {
      return new HubServiceAddress(service);
   }

   public static HubProtocolAddress protocol(String protocol) {
      return new HubProtocolAddress(protocol);
   }

   public static HubBridgeAddress bridge(String service, String protocol) {
      return new HubBridgeAddress(service, protocol);
   }

   public static HubPlatformAddress platform(Address address) {
      return new HubPlatformAddress(address);
   }
}

