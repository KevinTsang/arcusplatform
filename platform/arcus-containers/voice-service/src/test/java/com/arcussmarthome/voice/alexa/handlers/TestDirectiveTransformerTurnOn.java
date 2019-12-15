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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableSet;
import com.arcussmarthome.alexa.AlexaInterfaces;
import com.arcussmarthome.messages.MessageBody;
import com.arcussmarthome.messages.capability.Capability;
import com.arcussmarthome.messages.capability.DimmerCapability;
import com.arcussmarthome.messages.capability.LightCapability;
import com.arcussmarthome.messages.capability.SwitchCapability;
import com.arcussmarthome.messages.model.Model;
import com.arcussmarthome.messages.model.SimpleModel;
import com.arcussmarthome.messages.service.AlexaService;
import com.arcussmarthome.voice.alexa.AlexaConfig;

public class TestDirectiveTransformerTurnOn {

   private Model switchModel;
   private Model dimmerModel;
   private Model lightModel;
   private AlexaConfig config;
   private MessageBody request;

   @Before
   public void setup() {
      switchModel = new SimpleModel();
      switchModel.setAttribute(Capability.ATTR_CAPS, ImmutableSet.of(SwitchCapability.NAMESPACE));

      dimmerModel = new SimpleModel();
      dimmerModel.setAttribute(Capability.ATTR_CAPS, ImmutableSet.of(SwitchCapability.NAMESPACE, DimmerCapability.NAMESPACE));

      lightModel = new SimpleModel();
      lightModel.setAttribute(Capability.ATTR_CAPS, ImmutableSet.of(SwitchCapability.NAMESPACE, DimmerCapability.NAMESPACE, LightCapability.NAMESPACE));

      config = new AlexaConfig();

      request = AlexaService.ExecuteRequest.builder()
         .withDirective(AlexaInterfaces.PowerController.REQUEST_TURNON)
         .build();
   }

   @Test
   public void testTurnOnSwitchOff() {
      switchModel.setAttribute(SwitchCapability.ATTR_STATE, SwitchCapability.STATE_OFF);

      Optional<MessageBody> optBody = DirectiveTransformer.transformerFor(request).txfmRequest(request, switchModel, config);
      assertTrue(optBody.isPresent());
      MessageBody body = optBody.get();
      assertEquals(Capability.CMD_SET_ATTRIBUTES, body.getMessageType());
      assertEquals(SwitchCapability.STATE_ON, body.getAttributes().get(SwitchCapability.ATTR_STATE));
   }

   @Test
   public void testTurnOnSwitchOn() {
      switchModel.setAttribute(SwitchCapability.ATTR_STATE, SwitchCapability.STATE_ON);

      Optional<MessageBody> optBody = DirectiveTransformer.transformerFor(request).txfmRequest(request, switchModel, config);
      assertFalse(optBody.isPresent());
   }

   @Test
   public void testTurnOnDimmerAt100() {
      dimmerModel.setAttribute(SwitchCapability.ATTR_STATE, SwitchCapability.STATE_OFF);
      dimmerModel.setAttribute(DimmerCapability.ATTR_BRIGHTNESS, 100);

      Optional<MessageBody> optBody = DirectiveTransformer.transformerFor(request).txfmRequest(request, dimmerModel, config);
      assertTrue(optBody.isPresent());
      MessageBody body = optBody.get();
      assertEquals(Capability.CMD_SET_ATTRIBUTES, body.getMessageType());
      assertEquals(SwitchCapability.STATE_ON, body.getAttributes().get(SwitchCapability.ATTR_STATE));
      assertNull(body.getAttributes().get(DimmerCapability.ATTR_BRIGHTNESS));
   }

   @Test
   public void testTurnOnDimmerNot100() {
      dimmerModel.setAttribute(SwitchCapability.ATTR_STATE, SwitchCapability.STATE_OFF);
      dimmerModel.setAttribute(DimmerCapability.ATTR_BRIGHTNESS, 50);

      Optional<MessageBody> optBody = DirectiveTransformer.transformerFor(request).txfmRequest(request, dimmerModel, config);
      assertTrue(optBody.isPresent());
      MessageBody body = optBody.get();
      assertEquals(Capability.CMD_SET_ATTRIBUTES, body.getMessageType());
      assertEquals(SwitchCapability.STATE_ON, body.getAttributes().get(SwitchCapability.ATTR_STATE));
      assertEquals(100, body.getAttributes().get(DimmerCapability.ATTR_BRIGHTNESS));
   }

   @Test
   public void testTurnOnLightNot100Stays() {
      lightModel.setAttribute(SwitchCapability.ATTR_STATE, SwitchCapability.STATE_OFF);
      lightModel.setAttribute(DimmerCapability.ATTR_BRIGHTNESS, 50);

      Optional<MessageBody> optBody = DirectiveTransformer.transformerFor(request).txfmRequest(request, lightModel, config);
      assertTrue(optBody.isPresent());
      MessageBody body = optBody.get();
      assertEquals(Capability.CMD_SET_ATTRIBUTES, body.getMessageType());
      assertEquals(SwitchCapability.STATE_ON, body.getAttributes().get(SwitchCapability.ATTR_STATE));
      assertNull(body.getAttributes().get(DimmerCapability.ATTR_BRIGHTNESS));
   }

}

