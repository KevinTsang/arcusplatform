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
package com.arcussmarthome.netty.server.session;

import com.arcussmarthome.bridge.server.session.ClientToken;
import com.arcussmarthome.messages.address.Address;
import com.arcussmarthome.messages.address.ClientAddress;

public class IrisNettyClientClientToken implements ClientToken {
   
   public static IrisNettyClientClientToken fromAddress(Address address) {
      if (!(address instanceof ClientAddress)) {
         return null;
      }
      
      String clientId = ((ClientAddress)address).getId();
      if (clientId == null) {
         return null;
      }
      return new IrisNettyClientClientToken(clientId);
   }
   
   private final String key;
   
   public IrisNettyClientClientToken(String key) {
      this.key = key;
   }
   
   @Override
   public String getRepresentation() {
      return key;
   }

   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((key == null) ? 0 : key.hashCode());
      return result;
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (obj == null)
         return false;
      if (getClass() != obj.getClass())
         return false;
      IrisNettyClientClientToken other = (IrisNettyClientClientToken) obj;
      if (key == null) {
         if (other.key != null)
            return false;
      } else if (!key.equals(other.key))
         return false;
      return true;
   }

   @Override
   public String toString() {
      return "IrisNettyClientClientToken [key=" + key + "]";
   }
   
}

