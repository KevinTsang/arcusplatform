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
package com.arcussmarthome.security.authz;

import static org.junit.Assert.assertEquals;

import java.util.UUID;

import org.junit.Test;

public class TestAuthorizationContext {

   @Test
   public void testPermissionDivision() {
      AuthorizationContext context = AuthzFixtures.createContext(
            AuthzFixtures.placeId,
            "swit:x:" + UUID.randomUUID().toString(), "swit:*:*");

      assertEquals(1, context.getInstancePermissions(AuthzFixtures.placeId).size());
      assertEquals(1, context.getNonInstancePermissions(AuthzFixtures.placeId).size());
   }
}

