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
package com.arcussmarthome.voice.alexa.handlers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.arcussmarthome.alexa.AlexaInterfaces;
import com.arcussmarthome.alexa.error.AlexaErrors;
import com.arcussmarthome.alexa.error.AlexaException;
import com.arcussmarthome.messages.MessageBody;
import com.arcussmarthome.messages.capability.Capability;
import com.arcussmarthome.messages.capability.DeviceAdvancedCapability;
import com.arcussmarthome.messages.capability.DoorLockCapability;
import com.arcussmarthome.messages.model.Model;
import com.arcussmarthome.messages.model.SimpleModel;
import com.arcussmarthome.messages.service.AlexaService;
import com.arcussmarthome.voice.alexa.AlexaConfig;

public class TestDirectiveTransformerLock {

   private Model lockModel;
   private AlexaConfig config;
   private MessageBody request;

   @Before
   public void setup() {
      lockModel = new SimpleModel();
      lockModel.setAttribute(Capability.ATTR_CAPS, ImmutableSet.of(DoorLockCapability.NAMESPACE, DeviceAdvancedCapability.NAMESPACE));

      config = new AlexaConfig();

      request = AlexaService.ExecuteRequest.builder()
         .withDirective(AlexaInterfaces.LockController.REQUEST_LOCK)
         .build();
   }

   @Test
   public void testLockBusyLocking() {
      lockModel.setAttribute(DoorLockCapability.ATTR_LOCKSTATE, DoorLockCapability.LOCKSTATE_LOCKING);

      try {
         DirectiveTransformer.transformerFor(request).txfmRequest(request, lockModel, config);
      } catch(AlexaException ae) {
         assertEquals(AlexaErrors.TYPE_ENDPOINT_BUSY, ae.getErrorMessage().getAttributes().get("type"));
      }
   }

   @Test
   public void testLockBusyUnlocking() {
      lockModel.setAttribute(DoorLockCapability.ATTR_LOCKSTATE, DoorLockCapability.LOCKSTATE_UNLOCKING);

      try {
         DirectiveTransformer.transformerFor(request).txfmRequest(request, lockModel, config);
      } catch(AlexaException ae) {
         assertEquals(AlexaErrors.TYPE_ENDPOINT_BUSY, ae.getErrorMessage().getAttributes().get("type"));
      }
   }

   @Test
   public void testLockJammed() {
      lockModel.setAttribute(DeviceAdvancedCapability.ATTR_ERRORS, ImmutableMap.of("WARN_JAM", "jammed"));

      Optional<MessageBody> optBody = DirectiveTransformer.transformerFor(request).txfmRequest(request, lockModel, config);
      assertFalse(optBody.isPresent());
   }

   @Test
   public void testLockAlreadyLocked() {
      lockModel.setAttribute(DoorLockCapability.ATTR_LOCKSTATE, DoorLockCapability.LOCKSTATE_LOCKED);
      Optional<MessageBody> optBody = DirectiveTransformer.transformerFor(request).txfmRequest(request, lockModel, config);
      assertFalse(optBody.isPresent());
   }

   @Test
   public void testLockUnlocked() {
      lockModel.setAttribute(DoorLockCapability.ATTR_LOCKSTATE, DoorLockCapability.LOCKSTATE_UNLOCKED);
      Optional<MessageBody> optBody = DirectiveTransformer.transformerFor(request).txfmRequest(request, lockModel, config);
      assertTrue(optBody.isPresent());
      MessageBody body = optBody.get();
      assertEquals(Capability.CMD_SET_ATTRIBUTES, body.getMessageType());
      assertEquals(1, body.getAttributes().size());
      assertEquals(DoorLockCapability.LOCKSTATE_LOCKED, DoorLockCapability.getLockstate(body));
   }
}

