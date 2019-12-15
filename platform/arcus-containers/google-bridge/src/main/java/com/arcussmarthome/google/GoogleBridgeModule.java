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
package com.arcussmarthome.google;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;

import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import com.arcussmarthome.bootstrap.guice.AbstractIrisModule;
import com.arcussmarthome.bridge.metrics.BridgeMetrics;
import com.arcussmarthome.bridge.server.BridgeConfigModule;
import com.arcussmarthome.bridge.server.http.RequestAuthorizer;
import com.arcussmarthome.bridge.server.http.RequestHandler;
import com.arcussmarthome.core.dao.cassandra.CassandraPersonDAOModule;
import com.arcussmarthome.core.dao.cassandra.CassandraPersonPlaceAssocDAOModule;
import com.arcussmarthome.core.dao.cassandra.CassandraResourceBundleDAOModule;
import com.arcussmarthome.core.metricsreporter.builder.MetricsTopicReporterBuilderModule;
import com.arcussmarthome.google.handlers.GoogleHomeHandler;
import com.arcussmarthome.google.model.Request;
import com.arcussmarthome.google.model.Response;
import com.arcussmarthome.messages.address.Address;
import com.arcussmarthome.messages.service.VoiceService;
import com.arcussmarthome.oauth.auth.BearerAuth;
import com.arcussmarthome.oauth.dao.OAuthDAO;
import com.arcussmarthome.util.ThreadPoolBuilder;
import com.arcussmarthome.voice.VoiceBridgeConfig;
import com.arcussmarthome.voice.VoiceBridgeMetrics;
import com.arcussmarthome.voice.VoiceBridgeModule;
import com.netflix.governator.annotations.Modules;

import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

@Modules(include={
   CassandraPersonDAOModule.class,
   CassandraPersonPlaceAssocDAOModule.class,
   CassandraResourceBundleDAOModule.class,
   VoiceBridgeModule.class
})
public class GoogleBridgeModule extends AbstractIrisModule {

   @Inject
   public GoogleBridgeModule(
      BridgeConfigModule bridge,
      MetricsTopicReporterBuilderModule metrics
   ) {}

   @Override
   protected void configure() {

      bind(Address.class).annotatedWith(Names.named(VoiceBridgeConfig.NAME_BRIDGEADDRESS)).toInstance(Constants.BRIDGE_ADDRESS);
      bind(String.class).annotatedWith(Names.named(VoiceBridgeConfig.NAME_BRIDGEASSISTANT)).toInstance(VoiceService.StartPlaceRequest.ASSISTANT_GOOGLE);

      // Bind Http Handlers
      Multibinder<RequestHandler> rhBindings = Multibinder.newSetBinder(binder(), RequestHandler.class);
      rhBindings.addBinding().to(GoogleHomeHandler.class);

      VoiceBridgeMetrics metrics = new VoiceBridgeMetrics("google-bridge","google.bridge", "intent");
      bind(BridgeMetrics.class).toInstance(metrics);
      bind(VoiceBridgeMetrics.class).toInstance(metrics);
   }

   @Provides
   @Named(VoiceBridgeConfig.NAME_EXECUTOR)
   @Singleton
   public ExecutorService googleBridgeExecutor(GoogleBridgeConfig config) {
      return new ThreadPoolBuilder()
         .withBlockingBacklog()
         .withMaxPoolSize(config.getHandlerMaxThreads())
         .withKeepAliveMs(config.getHandlerThreadKeepAliveMs())
         .withNameFormat("google-bridge-handler-%d")
         .withMetrics("google.bridge.handler")
         .build();
   }

   @Provides
   @Named(GoogleHomeHandler.BEARER_AUTH_NAME)
   @Singleton
   public RequestAuthorizer bearerAuth(
      OAuthDAO oauthDao,
      BridgeMetrics metrics,
      GoogleBridgeConfig config
   ) {
      return new BearerAuth(oauthDao, metrics, config.getOauthAppId(), (req) -> {
         Request request = Transformers.GSON.fromJson(req.content().toString(StandardCharsets.UTF_8), Request.class);
         Response res = new Response();
         res.setRequestId(request.getRequestId());
         res.setPayload(ImmutableMap.of(Constants.Response.ERROR_CODE, Constants.Error.AUTH_EXPIRED));
         DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
         response.content().writeBytes(Transformers.GSON.toJson(res).getBytes(StandardCharsets.UTF_8));
         return response;
      });
   }

}

