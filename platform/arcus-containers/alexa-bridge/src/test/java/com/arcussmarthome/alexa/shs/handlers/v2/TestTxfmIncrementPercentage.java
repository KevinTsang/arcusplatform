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
import com.arcussmarthome.alexa.message.AlexaMessage;
import com.arcussmarthome.alexa.message.Header;
import com.arcussmarthome.alexa.message.v2.Appliance;
import com.arcussmarthome.alexa.message.v2.DoubleValue;
import com.arcussmarthome.alexa.message.v2.request.IncrementPercentageRequest;
import com.arcussmarthome.alexa.message.v2.response.IncrementPercentageConfirmation;
import com.arcussmarthome.alexa.shs.ShsAssertions;
import com.arcussmarthome.messages.PlatformMessage;
import com.arcussmarthome.messages.address.Address;
import com.arcussmarthome.messages.capability.FanCapability;
import com.arcussmarthome.messages.service.AlexaService;
import com.arcussmarthome.util.IrisUUID;

public class TestTxfmIncrementPercentage {

   private Header incPercentageHeader;

   @Before
   public void setup() {
      incPercentageHeader = Header.v2(IrisUUID.randomUUID().toString(), "IncrementPercentageRequest", "Alexa.ConnectedHome.Control");
   }

   @Test
   public void testIncrementPercentageLight() {
      Appliance app = new Appliance();
      app.setApplianceId(Address.platformDriverAddress(IrisUUID.randomUUID()).getRepresentation());

      IncrementPercentageRequest payload = new IncrementPercentageRequest();
      payload.setAccessToken("token");
      payload.setAppliance(app);
      payload.setDeltaPercentage(new DoubleValue(25.0));

      AlexaMessage msg = new AlexaMessage(incPercentageHeader, payload);
      PlatformMessage platMsg = TxfmTestUtil.txfmReq(msg);

      ShsAssertions.assertExecuteRequest(
         platMsg,
         app.getApplianceId(),
         AlexaInterfaces.BrightnessController.REQUEST_ADJUSTBRIGHTNESS,
         ImmutableMap.of(AlexaInterfaces.BrightnessController.ARG_BRIGHTNESSDELTA, 25),
         null,
         false
      );
   }

   @Test
   public void testIncrementPercentageFan() {
      Appliance app = new Appliance();
      app.setApplianceId(Address.platformDriverAddress(IrisUUID.randomUUID()).getRepresentation());
      app.setAdditionalApplianceDetails(ImmutableMap.of(FanCapability.ATTR_MAXSPEED, "3"));

      IncrementPercentageRequest payload = new IncrementPercentageRequest();
      payload.setAccessToken("token");
      payload.setAppliance(app);
      payload.setDeltaPercentage(new DoubleValue(25.0));

      AlexaMessage msg = new AlexaMessage(incPercentageHeader, payload);
      PlatformMessage platMsg = TxfmTestUtil.txfmReq(msg);

      ShsAssertions.assertExecuteRequest(
         platMsg,
         app.getApplianceId(),
         AlexaInterfaces.PercentageController.REQUEST_ADJUSTPERCENTAGE,
         ImmutableMap.of(AlexaInterfaces.PercentageController.ARG_PERCENTAGEDELTA, 25),
         null,
         false
      );
   }

   @Test
   public void testConfirmation() {
      Appliance app = new Appliance();
      app.setApplianceId(Address.platformDriverAddress(IrisUUID.randomUUID()).getRepresentation());

      IncrementPercentageRequest payload = new IncrementPercentageRequest();
      payload.setAccessToken("token");
      payload.setAppliance(app);
      payload.setDeltaPercentage(new DoubleValue(25.0));

      AlexaMessage msg = new AlexaMessage(incPercentageHeader, payload);
      AlexaMessage response = TxfmTestUtil.txfmResponse(msg, AlexaService.ExecuteResponse.builder().build());
      ShsAssertions.assertCommonResponseHeader(msg, response, "IncrementPercentageConfirmation", "2");
      assertTrue(response.getPayload() instanceof IncrementPercentageConfirmation);
   }
}

