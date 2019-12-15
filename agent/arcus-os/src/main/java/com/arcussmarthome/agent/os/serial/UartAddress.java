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
package com.arcussmarthome.agent.os.serial;

import java.net.SocketAddress;

public class UartAddress extends SocketAddress {
   private static final long serialVersionUID = 5465774532548149662L;

   private final String port;

   public UartAddress(String port) {
      this.port = port;
   }

   public String getPort() {
      return port;
   }

   @Override
   public String toString() {
      StringBuilder bld = new StringBuilder();
      bld.append(getClass().getSimpleName()).append(" [");
      bld.append("port=").append(port);
      bld.append("]");
      return bld.toString();
   }
}

