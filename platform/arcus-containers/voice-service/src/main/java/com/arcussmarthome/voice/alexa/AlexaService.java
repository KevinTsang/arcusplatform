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
package com.arcussmarthome.voice.alexa;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.arcussmarthome.alexa.AlexaUtil;
import com.arcussmarthome.core.messaging.MessageListener;
import com.arcussmarthome.core.platform.PlatformDispatcherFactory;
import com.arcussmarthome.messages.PlatformMessage;
import com.arcussmarthome.messages.address.Address;
import com.arcussmarthome.voice.VoiceProvider;
import com.arcussmarthome.voice.alexa.handlers.AcceptGrantHandler;
import com.arcussmarthome.voice.alexa.handlers.DiscoverHandler;
import com.arcussmarthome.voice.alexa.handlers.ExecuteHandler;
import com.arcussmarthome.voice.context.VoiceContextResolver;

@Singleton
public class AlexaService implements VoiceProvider {

   private final MessageListener<PlatformMessage> dispatcher;

   @Inject
   public AlexaService(
      VoiceContextResolver contextResolver,
      PlatformDispatcherFactory factory,
      DiscoverHandler discoverHandler,
      ExecuteHandler executeHandler,
      AcceptGrantHandler acceptGrantHandler
   ) {
      this.dispatcher = factory
         .buildDispatcher()
         .addArgumentResolverFactory(contextResolver)
         .addAnnotatedHandler(discoverHandler)
         .addAnnotatedHandler(executeHandler)
         .addAnnotatedHandler(acceptGrantHandler)
         .build();
   }

   @Override
   public Address address() {
      return AlexaUtil.ADDRESS_SERVICE;
   }

   @Override
   public void onMessage(PlatformMessage msg) {
      dispatcher.onMessage(msg);
   }
}

