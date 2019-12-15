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

import static org.junit.Assert.assertEquals;

import java.util.UUID;

import com.arcussmarthome.alexa.AlexaUtil;
import com.arcussmarthome.alexa.message.AlexaMessage;
import com.arcussmarthome.messages.MessageBody;
import com.arcussmarthome.messages.PlatformMessage;
import com.arcussmarthome.messages.address.Address;
import com.arcussmarthome.messages.service.VoiceService;
import com.arcussmarthome.messages.type.Population;
import com.arcussmarthome.util.IrisUUID;

enum TxfmTestUtil {
   ;

   static final UUID placeId = IrisUUID.randomUUID();
   static final int ttl = 30;
   static final String population = Population.NAME_GENERAL;

   static PlatformMessage txfmReq(AlexaMessage msg) {
      PlatformMessage platMsg = Txfm.transformerFor(msg).txfmRequest(msg, placeId, population, ttl);
      assertEquals(placeId.toString(), platMsg.getPlaceId());
      assertEquals(30, platMsg.getTimeToLive());
      return platMsg;
   }

   static AlexaMessage txfmResponse(AlexaMessage msg, MessageBody body) {
      PlatformMessage response = createResponse(body, msg.getHeader().getMessageId());
      return Txfm.transformerFor(msg).transformResponse(msg, response);
   }

   private static PlatformMessage createResponse(MessageBody body, String corrId) {
      return PlatformMessage.buildMessage(body, Address.platformService(VoiceService.NAMESPACE), AlexaUtil.ADDRESS_BRIDGE)
         .withPlaceId(placeId)
         .withCorrelationId(corrId)
         .create();
   }
}

