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
package com.arcussmarthome.bridge.server.session;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import com.arcussmarthome.messages.address.Address;
import com.arcussmarthome.netty.server.session.IrisNettyClientIds;

public class TestIrisNettyClientIds {

   @Test
   public void testValidAddress() {
      Address address = Address.fromString("CLNT:android:" + IrisNettyClientIds.createId());
      System.out.println(address);
      assertNotNull(address);
   }
   
   @Test
   public void testUnique() {
      int count = 100;
      Set<String> ids = new HashSet<>();
      for(int i=0; i<count; i++) {
         ids.add(IrisNettyClientIds.createId());
      }
      assertEquals(count, ids.size());
   }

}

