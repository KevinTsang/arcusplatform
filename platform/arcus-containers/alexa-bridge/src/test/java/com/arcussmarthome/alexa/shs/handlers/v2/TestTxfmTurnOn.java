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
package com.arcussmarthome.alexa.shs.handlers.v2;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableMap;
import com.arcussmarthome.alexa.AlexaInterfaces;
import com.arcussmarthome.alexa.AlexaUtil;
import com.arcussmarthome.alexa.message.AlexaMessage;
import com.arcussmarthome.alexa.message.Header;
import com.arcussmarthome.alexa.message.v2.Appliance;
import com.arcussmarthome.alexa.message.v2.request.TurnOnRequest;
import com.arcussmarthome.alexa.message.v2.response.TurnOnConfirmation;
import com.arcussmarthome.alexa.shs.ShsAssertions;
import com.arcussmarthome.messages.PlatformMessage;
import com.arcussmarthome.messages.address.Address;
import com.arcussmarthome.messages.capability.SceneCapability;
import com.arcussmarthome.messages.service.AlexaService;
import com.arcussmarthome.util.IrisUUID;

public class TestTxfmTurnOn {

   private Header turnOnHeader;

   @Before
   public void setup() {
      turnOnHeader = Header.v2(IrisUUID.randomUUID().toString(), "TurnOnRequest", "Alexa.ConnectedHome.Control");
   }

   @Test
   public void testTurnOnNonScene() {
      Appliance app = new Appliance();
      app.setApplianceId(Address.platformDriverAddress(IrisUUID.randomUUID()).getRepresentation());

      TurnOnRequest payload = new TurnOnRequest();
      payload.setAccessToken("token");
      payload.setAppliance(app);

      AlexaMessage msg = new AlexaMessage(turnOnHeader, payload);
      PlatformMessage platMsg = TxfmTestUtil.txfmReq(msg);

      ShsAssertions.assertExecuteRequest(
         platMsg,
         app.getApplianceId(),
         AlexaInterfaces.PowerController.REQUEST_TURNON,
         ImmutableMap.of(),
         null,
         false
      );
   }

   @Test
   public void testTurnOnScene() {

      Address sceneAddr = Address.platformService(TxfmTestUtil.placeId, SceneCapability.NAMESPACE, 1);

      Appliance app = new Appliance();
      app.setApplianceId(AlexaUtil.addressToEndpointId(sceneAddr.getRepresentation()));

      TurnOnRequest payload = new TurnOnRequest();
      payload.setAccessToken("token");
      payload.setAppliance(app);

      AlexaMessage msg = new AlexaMessage(turnOnHeader, payload);
      PlatformMessage platMsg = TxfmTestUtil.txfmReq(msg);

      ShsAssertions.assertExecuteRequest(
         platMsg,
         sceneAddr.getRepresentation(),
         AlexaInterfaces.SceneController.REQUEST_ACTIVATE,
         ImmutableMap.of(),
         null,
         false
      );
   }

   @Test
   public void testConfirmation() {

      Appliance app = new Appliance();
      app.setApplianceId(Address.platformDriverAddress(IrisUUID.randomUUID()).getRepresentation());

      TurnOnRequest payload = new TurnOnRequest();
      payload.setAccessToken("token");
      payload.setAppliance(app);

      AlexaMessage msg = new AlexaMessage(turnOnHeader, payload);
      AlexaMessage response = TxfmTestUtil.txfmResponse(msg, AlexaService.ExecuteResponse.builder().build());
      ShsAssertions.assertCommonResponseHeader(msg, response, "TurnOnConfirmation", "2");
      assertTrue(response.getPayload() instanceof TurnOnConfirmation);
   }
}

