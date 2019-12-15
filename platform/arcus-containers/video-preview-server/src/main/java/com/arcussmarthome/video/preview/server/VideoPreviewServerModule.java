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
package com.arcussmarthome.video.preview.server;

import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.name.Names;
import com.arcussmarthome.bootstrap.guice.AbstractIrisModule;
import com.arcussmarthome.bridge.metrics.BridgeMetrics;
import com.arcussmarthome.bridge.server.BridgeConfigModule;
import com.arcussmarthome.bridge.server.config.BridgeServerConfig;
import com.arcussmarthome.bridge.server.http.RequestAuthorizer;
import com.arcussmarthome.bridge.server.http.RequestHandler;
import com.arcussmarthome.bridge.server.http.RequestMatcher;
import com.arcussmarthome.bridge.server.http.handlers.CheckPage;
import com.arcussmarthome.bridge.server.http.handlers.IndexPage;
import com.arcussmarthome.bridge.server.http.handlers.RootRedirect;
import com.arcussmarthome.bridge.server.http.impl.auth.SessionAuth;
import com.arcussmarthome.bridge.server.http.impl.matcher.NeverMatcher;
import com.arcussmarthome.bridge.server.netty.Authenticator;
import com.arcussmarthome.bridge.server.netty.BaseWebSocketServerHandlerProvider;
import com.arcussmarthome.bridge.server.session.DefaultSessionFactoryImpl;
import com.arcussmarthome.bridge.server.session.DefaultSessionRegistryImpl;
import com.arcussmarthome.bridge.server.session.SessionFactory;
import com.arcussmarthome.bridge.server.session.SessionListener;
import com.arcussmarthome.bridge.server.session.SessionRegistry;
import com.arcussmarthome.bridge.server.shiro.ShiroAuthenticator;
import com.arcussmarthome.bridge.server.shiro.ShiroModule;
import com.arcussmarthome.bridge.server.ssl.BridgeServerTlsContext;
import com.arcussmarthome.bridge.server.ssl.BridgeServerTlsContextImpl;
import com.arcussmarthome.bridge.server.ssl.BridgeServerTrustManagerFactory;
import com.arcussmarthome.bridge.server.ssl.NullTrustManagerFactoryImpl;
import com.arcussmarthome.client.security.SubscriberAuthorizationContextLoader;
import com.arcussmarthome.core.dao.cassandra.CassandraAuthorizationGrantDAOModule;
import com.arcussmarthome.core.dao.cassandra.CassandraPersonDAOModule;
import com.arcussmarthome.core.dao.cassandra.CassandraResourceBundleDAOModule;
import com.arcussmarthome.core.dao.cassandra.PersonDAOSecurityModule;
import com.arcussmarthome.core.metricsreporter.builder.MetricsTopicReporterBuilderModule;
import com.arcussmarthome.netty.security.IrisNettyAuthorizationContextLoader;
import com.arcussmarthome.platform.partition.cluster.ClusteredPartitionModule;
import com.arcussmarthome.video.PreviewConfig;
import com.arcussmarthome.video.PreviewModule;
import com.arcussmarthome.video.preview.server.handlers.PreviewHandler;
import com.arcussmarthome.video.preview.server.netty.HttpRequestInitializer;

import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

public class VideoPreviewServerModule extends AbstractIrisModule {

   @Inject
   public VideoPreviewServerModule(
         BridgeConfigModule bridge,
         PreviewModule previews,
         ShiroModule shiro,
         CassandraResourceBundleDAOModule resourceDao,
         CassandraPersonDAOModule personDao,
         CassandraAuthorizationGrantDAOModule authGrantDao,
         PersonDAOSecurityModule personDaoSecurity,
         MetricsTopicReporterBuilderModule metrics,
         ClusteredPartitionModule partition
         ) {
   }

   @Override
   protected void configure() {
      bind(BridgeServerConfig.class);
      bind(PreviewConfig.class);
      bind(BridgeServerTlsContext.class).to(BridgeServerTlsContextImpl.class);
      bind(BridgeServerTrustManagerFactory.class).to(NullTrustManagerFactoryImpl.class);
      bind(ChannelInboundHandler.class).toProvider(BaseWebSocketServerHandlerProvider.class);
      bind(new TypeLiteral<ChannelInitializer<SocketChannel>>(){}).to(HttpRequestInitializer.class);
      bind(Authenticator.class).to(ShiroAuthenticator.class);
      bind(SessionFactory.class).to(DefaultSessionFactoryImpl.class);
      bind(SessionRegistry.class).to(DefaultSessionRegistryImpl.class);
      bind(RequestMatcher.class).annotatedWith(Names.named("WebSocketUpgradeMatcher")).to(NeverMatcher.class);
      bind(RequestAuthorizer.class).annotatedWith(Names.named("SessionAuthorizer")).to(SessionAuth.class);
      bind(IrisNettyAuthorizationContextLoader.class).to(SubscriberAuthorizationContextLoader.class);

      // No Session Listeners
      Multibinder<SessionListener> slBindings = Multibinder.newSetBinder(binder(), SessionListener.class);

      // Bind Http Handlers
      Multibinder<RequestHandler> rhBindings = Multibinder.newSetBinder(binder(), RequestHandler.class);
      rhBindings.addBinding().to(RootRedirect.class);
      rhBindings.addBinding().to(IndexPage.class);
      rhBindings.addBinding().to(CheckPage.class);
      rhBindings.addBinding().to(PreviewHandler.class);
   }

   @Provides @Singleton
   public BridgeMetrics provideBridgeMetrics() {
      return new BridgeMetrics("video-preview");
   }
}

