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
package com.arcussmarthome.core.protocol.ipcd.exceptions;

public class DeviceNotFoundException extends IpcdDaoException {

   private final String protocolAddress;

   public DeviceNotFoundException(String protocolAddress) {
      super("ipcd device with protocol address " + protocolAddress + " could not be found");
      this.protocolAddress = protocolAddress;
   }

   public String getProtocolAddress() {
      return protocolAddress;
   }
}

