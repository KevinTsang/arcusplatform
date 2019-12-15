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
package com.arcussmarthome.video.streaming.server;

import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import com.arcussmarthome.bootstrap.guice.AbstractIrisModule;
import com.arcussmarthome.bridge.metrics.BridgeMetrics;
import com.arcussmarthome.bridge.server.BridgeConfigModule;
import com.arcussmarthome.bridge.server.client.ClientFactory;
import com.arcussmarthome.bridge.server.config.BridgeServerConfig;
import com.arcussmarthome.bridge.server.http.RequestAuthorizer;
import com.arcussmarthome.bridge.server.http.RequestHandler;
import com.arcussmarthome.bridge.server.http.RequestMatcher;
import com.arcussmarthome.bridge.server.http.handlers.CheckPage;
import com.arcussmarthome.bridge.server.http.impl.auth.AlwaysAllow;
import com.arcussmarthome.bridge.server.http.impl.matcher.NeverMatcher;
import com.arcussmarthome.bridge.server.netty.BaseWebSocketServerHandlerProvider;
import com.arcussmarthome.bridge.server.session.SessionFactory;
import com.arcussmarthome.bridge.server.session.SessionListener;
import com.arcussmarthome.bridge.server.session.SessionRegistry;
import com.arcussmarthome.bridge.server.ssl.BridgeServerTlsContext;
import com.arcussmarthome.bridge.server.ssl.BridgeServerTlsContextImpl;
import com.arcussmarthome.bridge.server.ssl.BridgeServerTrustManagerFactory;
import com.arcussmarthome.bridge.server.ssl.NullTrustManagerFactoryImpl;
import com.arcussmarthome.core.dao.cassandra.CassandraPlaceDAOModule;
import com.arcussmarthome.platform.partition.cluster.ClusteredPartitionModule;
import com.arcussmarthome.video.PreviewModule;
import com.arcussmarthome.video.VideoStorageModule;
import com.arcussmarthome.video.cql.v2.CassandraVideoV2Module;
import com.arcussmarthome.video.netty.HttpRequestInitializer;
import com.arcussmarthome.video.netty.NullSessionFactoryImpl;
import com.arcussmarthome.video.netty.NullSessionRegistryImpl;
import com.arcussmarthome.video.streaming.server.http.DashVideoHandler;
import com.arcussmarthome.video.streaming.server.http.HlsHandler;
import com.arcussmarthome.video.streaming.server.http.HlsIFrameHandler;
import com.arcussmarthome.video.streaming.server.http.HlsPlaylistHandler;
import com.arcussmarthome.video.streaming.server.http.HlsVideoHandler;
import com.arcussmarthome.video.streaming.server.http.JPGHandler;
import com.arcussmarthome.video.streaming.server.session.VideoStreamingClientFactory;

import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

public class VideoStreamingServerModule extends AbstractIrisModule {
   public static final String DASH_PROP = "deploy.dash";

   @Inject(optional = true)
   @Named(DASH_PROP)
   private boolean dash = false;

   @Inject
   public VideoStreamingServerModule(
   		BridgeConfigModule bridgeModule, 
   		VideoStorageModule videoStorageModule, 
   		ClusteredPartitionModule partitionModule,
   		CassandraPlaceDAOModule placeModule,
   		CassandraVideoV2Module videoModule,
   		PreviewModule previewModule) {
   }

   @Override
   protected void configure() {
      bind(BridgeServerConfig.class);
      bind(VideoStreamingServerConfig.class);

      bind(BridgeServerTlsContext.class).to(BridgeServerTlsContextImpl.class);
      bind(BridgeServerTrustManagerFactory.class).to(NullTrustManagerFactoryImpl.class);

      bind(SessionFactory.class).to(NullSessionFactoryImpl.class);
      bind(SessionRegistry.class).to(NullSessionRegistryImpl.class);
      bind(ClientFactory.class).to(VideoStreamingClientFactory.class);

      bind(ChannelInboundHandler.class).toProvider(BaseWebSocketServerHandlerProvider.class);
      bind(new TypeLiteral<ChannelInitializer<SocketChannel>>(){}).to(HttpRequestInitializer.class);

      bind(RequestMatcher.class).annotatedWith(Names.named("WebSocketUpgradeMatcher")).to(NeverMatcher.class);
      bind(RequestAuthorizer.class).annotatedWith(Names.named("SessionAuthorizer")).to(AlwaysAllow.class);

      // No Session Listeners
      Multibinder<SessionListener> slBindings = Multibinder.newSetBinder(binder(), SessionListener.class);

      // Bind Http Handlers
      Multibinder<RequestHandler> rhBindings = Multibinder.newSetBinder(binder(), RequestHandler.class);
      rhBindings.addBinding().to(CheckPage.class);
      rhBindings.addBinding().to(HlsHandler.class);
      rhBindings.addBinding().to(HlsPlaylistHandler.class);
      rhBindings.addBinding().to(HlsIFrameHandler.class);
      rhBindings.addBinding().to(HlsVideoHandler.class);
      rhBindings.addBinding().to(JPGHandler.class);

      if (dash) {         
         rhBindings.addBinding().to(DashVideoHandler.class);
      }
   }

   @Provides @Singleton
   public BridgeMetrics provideBridgeMetrics() {
      return new BridgeMetrics("video-streaming");
   }
}

