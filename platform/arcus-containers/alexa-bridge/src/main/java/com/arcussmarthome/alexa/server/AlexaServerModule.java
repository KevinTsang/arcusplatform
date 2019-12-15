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
package com.arcussmarthome.alexa.server;

import java.util.concurrent.ExecutorService;

import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import com.arcussmarthome.alexa.AlexaUtil;
import com.arcussmarthome.alexa.bus.AlexaPlatformService;
import com.arcussmarthome.alexa.shs.handlers.SmartHomeSkillHandler;
import com.arcussmarthome.alexa.shs.handlers.SmartHomeSkillRequestHandler;
import com.arcussmarthome.alexa.shs.handlers.v2.SmartHomeSkillV2Handler;
import com.arcussmarthome.alexa.shs.handlers.v3.SmartHomeSkillV3Handler;
import com.arcussmarthome.bootstrap.guice.AbstractIrisModule;
import com.arcussmarthome.bridge.metrics.BridgeMetrics;
import com.arcussmarthome.bridge.server.BridgeConfigModule;
import com.arcussmarthome.bridge.server.http.RequestHandler;
import com.arcussmarthome.core.dao.cassandra.CassandraDAOModule;
import com.arcussmarthome.core.metricsreporter.builder.MetricsTopicReporterBuilderModule;
import com.arcussmarthome.messages.address.Address;
import com.arcussmarthome.messages.service.VoiceService;
import com.arcussmarthome.population.PlacePopulationCacheModule;
import com.arcussmarthome.util.ThreadPoolBuilder;
import com.arcussmarthome.voice.VoiceBridgeConfig;
import com.arcussmarthome.voice.VoiceBridgeMetrics;
import com.arcussmarthome.voice.VoiceBridgeModule;
import com.netflix.governator.annotations.Modules;

@Modules(include={
   CassandraDAOModule.class,
   VoiceBridgeModule.class,
   BridgeConfigModule.class,
   MetricsTopicReporterBuilderModule.class,
   PlacePopulationCacheModule.class
})
public class AlexaServerModule extends AbstractIrisModule {

   @Override
   protected void configure() {

      bind(Address.class).annotatedWith(Names.named(VoiceBridgeConfig.NAME_BRIDGEADDRESS)).toInstance(AlexaUtil.ADDRESS_BRIDGE);
      bind(String.class).annotatedWith(Names.named(VoiceBridgeConfig.NAME_BRIDGEASSISTANT)).toInstance(VoiceService.StartPlaceRequest.ASSISTANT_ALEXA);

      bind(AlexaPlatformService.class).asEagerSingleton();

      Multibinder<SmartHomeSkillHandler> handlers = Multibinder.newSetBinder(binder(), SmartHomeSkillHandler.class);
      handlers.addBinding().to(SmartHomeSkillV2Handler.class);
      handlers.addBinding().to(SmartHomeSkillV3Handler.class);

      // Bind Http Handlers
      Multibinder<RequestHandler> rhBindings = Multibinder.newSetBinder(binder(), RequestHandler.class);
      rhBindings.addBinding().to(SmartHomeSkillRequestHandler.class);

      VoiceBridgeMetrics metrics = new VoiceBridgeMetrics("alexa-bridge","alexa.bridge", "directive");
      bind(BridgeMetrics.class).toInstance(metrics);
      bind(VoiceBridgeMetrics.class).toInstance(metrics);
   }

   @Provides
   @Named(VoiceBridgeConfig.NAME_EXECUTOR)
   @Singleton
   public ExecutorService alexaBridgeExecutor(VoiceBridgeConfig config) {
      return new ThreadPoolBuilder()
         .withBlockingBacklog()
         .withMaxPoolSize(config.getHandlerMaxThreads())
         .withKeepAliveMs(config.getHandlerThreadKeepAliveMs())
         .withNameFormat("alexa-bridge-handler-%d")
         .withMetrics("alexa.bridge.handler")
         .build();
   }
}

