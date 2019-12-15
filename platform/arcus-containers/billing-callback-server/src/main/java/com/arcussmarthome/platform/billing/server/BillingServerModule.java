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
/**
 *
 */
package com.arcussmarthome.platform.billing.server;

import io.netty.channel.ChannelInboundHandler;

import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.MapBinder;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.name.Names;
import com.arcussmarthome.billing.client.BillingModule;
import com.arcussmarthome.bootstrap.guice.AbstractIrisModule;
import com.arcussmarthome.bridge.server.ServerModule;
import com.arcussmarthome.bridge.server.auth.basic.BasicAuthModule;
import com.arcussmarthome.bridge.server.http.RequestAuthorizer;
import com.arcussmarthome.bridge.server.http.RequestHandler;
import com.arcussmarthome.bridge.server.http.RequestMatcher;
import com.arcussmarthome.bridge.server.http.handlers.CheckPage;
import com.arcussmarthome.bridge.server.http.handlers.IndexPage;
import com.arcussmarthome.bridge.server.http.handlers.RootRedirect;
import com.arcussmarthome.bridge.server.http.impl.auth.AlwaysAllow;
import com.arcussmarthome.bridge.server.http.impl.matcher.NeverMatcher;
import com.arcussmarthome.bridge.server.netty.BaseWebSocketServerHandlerProvider;
import com.arcussmarthome.bridge.server.session.DefaultSessionModule;
import com.arcussmarthome.bridge.server.session.SessionListener;
import com.arcussmarthome.core.dao.cassandra.CassandraResourceBundleDAOModule;
import com.arcussmarthome.core.messaging.kafka.KafkaModule;
import com.arcussmarthome.core.template.TemplateModule;
import com.arcussmarthome.platform.billing.server.recurly.ClosedInvoiceWebhookHandler;
import com.arcussmarthome.platform.billing.server.recurly.RecurlyCallbackHttpHandler;
import com.arcussmarthome.platform.billing.server.recurly.WebhookHandler;
import com.netflix.governator.annotations.Modules;

/**
 *
 */
// TODO replace NoAuthModule with a ReCurlyAuthModule (basic auth? ip range filtering?)
@Modules(include={
      KafkaModule.class,
      ServerModule.class,
      BasicAuthModule.class,
      TemplateModule.class,
      CassandraResourceBundleDAOModule.class,
      DefaultSessionModule.class, // why do we need sessions here?
      BillingModule.class
})
public class BillingServerModule extends AbstractIrisModule {

   @Override
   protected void configure() {
      // No Session Listeners
      Multibinder<SessionListener> slBindings = Multibinder.newSetBinder(binder(), SessionListener.class);

      // Bind Http Handlers
      Multibinder<RequestHandler> rhBindings = Multibinder.newSetBinder(binder(), RequestHandler.class);
      rhBindings.addBinding().to(RootRedirect.class);
      rhBindings.addBinding().to(IndexPage.class);
      rhBindings.addBinding().to(CheckPage.class);
      rhBindings.addBinding().to(RecurlyCallbackHttpHandler.class);

      bind(RequestAuthorizer.class)
         .annotatedWith(Names.named("SessionAuthorizer"))
         .to(AlwaysAllow.class);

      MapBinder<String,WebhookHandler<? extends Object>> actionBinder = MapBinder.newMapBinder(binder(),new TypeLiteral<String>(){},new TypeLiteral<WebhookHandler<? extends Object>>(){});
      actionBinder.addBinding(RecurlyCallbackHttpHandler.TRANS_TYPE_CLOSED_INVOICE_NOTIFICATION).to(ClosedInvoiceWebhookHandler.class);

      // use the BaseHandler because it isn't abstract as the name implies, its just doesn't fully support websockets
      bind(ChannelInboundHandler.class).toProvider(BaseWebSocketServerHandlerProvider.class);

      // TODO bind this up into a WebSocket / NoWebSocket module...
      bind(RequestMatcher.class).annotatedWith(Names.named("WebSocketUpgradeMatcher")).to(NeverMatcher.class);
   }
}

