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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.arcussmarthome.alexa.AlexaInterfaces;
import com.arcussmarthome.alexa.error.AlexaErrors;
import com.arcussmarthome.messages.MessageBody;
import com.arcussmarthome.messages.listener.annotation.Request;
import com.arcussmarthome.messages.service.AlexaService;
import com.arcussmarthome.messages.service.VoiceService;
import com.arcussmarthome.voice.alexa.AlexaMetrics;
import com.arcussmarthome.voice.alexa.http.AlexaHttpClient;
import com.arcussmarthome.voice.context.VoiceDAO;
import com.arcussmarthome.voice.proactive.ProactiveCreds;
import com.arcussmarthome.voice.context.VoiceContext;
import com.arcussmarthome.voice.proactive.ProactiveCredsDAO;

@Singleton
public class AcceptGrantHandler {

   private static final Logger logger = LoggerFactory.getLogger(AcceptGrantHandler.class);

   private final AlexaHttpClient client;
   private final VoiceDAO voiceDao;
   private final ProactiveCredsDAO proactiveCredsDao;

   @Inject
   public AcceptGrantHandler(AlexaHttpClient client, VoiceDAO voiceDao, ProactiveCredsDAO proactiveCredsDao) {
      this.client = client;
      this.voiceDao = voiceDao;
      this.proactiveCredsDao = proactiveCredsDao;
   }

   @Request(value = AlexaService.AcceptGrantRequest.NAME, service = true)
   public MessageBody handleAcceptGrant(
      VoiceContext context,
      @Named(AlexaService.AcceptGrantRequest.ATTR_CODE) String code
    ) {
      HandlerUtil.markAssistantIfNecessary(context, voiceDao);
      AlexaMetrics.incCommand(AlexaInterfaces.Authorization.REQUEST_ACCEPTGRANT);
      try {
         ProactiveCreds creds = client.createCreds(context.getPlaceId(), code);
         proactiveCredsDao.upsert(context.getPlaceId(), VoiceService.StartPlaceRequest.ASSISTANT_ALEXA, creds);
         context.updateProactiveCreds(VoiceService.StartPlaceRequest.ASSISTANT_ALEXA, creds);
      } catch(Exception e) {
         logger.warn("failed to acquire and persist proactive reporting tokens", e);
         AlexaMetrics.incAcceptGrantFailed();
         return AlexaErrors.ACCEPT_GRANT_FAILED;
      }
      return AlexaService.AcceptGrantResponse.instance();
   }
}

