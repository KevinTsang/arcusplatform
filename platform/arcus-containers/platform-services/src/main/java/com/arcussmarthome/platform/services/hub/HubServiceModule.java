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
package com.arcussmarthome.platform.services.hub;

import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.Multibinder;
import com.arcussmarthome.bootstrap.guice.AbstractIrisModule;
import com.arcussmarthome.core.platform.ContextualEventMessageHandler;
import com.arcussmarthome.core.platform.ContextualRequestMessageHandler;
import com.arcussmarthome.core.platform.PlatformService;
import com.arcussmarthome.messages.model.Hub;
import com.arcussmarthome.messages.model.Place;
import com.arcussmarthome.platform.services.hub.handlers.FirmwareUpdateProgressMessageHandler;
import com.arcussmarthome.platform.services.hub.handlers.HubDeleteHandler;
import com.arcussmarthome.platform.services.hub.handlers.HubEventListener;
import com.arcussmarthome.platform.services.hub.handlers.HubGetAttributesHandler;
import com.arcussmarthome.platform.services.hub.handlers.HubHeartbeatListener;
import com.arcussmarthome.platform.services.hub.handlers.OfflineHubRequestHandler;
import com.arcussmarthome.platform.services.hub.handlers.PlaceDeletedListener;
import com.arcussmarthome.platform.services.hub.handlers.PlaceValueChangeListener;

public class HubServiceModule extends AbstractIrisModule {

   @Override
   protected void configure() {
      bind(HubHeartbeatListener.class).asEagerSingleton();
      bind(HubEventListener.class).asEagerSingleton();
      bind(OfflineHubRequestHandler.class).asEagerSingleton();
      
      Multibinder<ContextualRequestMessageHandler<Hub>> handlerBinder = bindSetOf(new TypeLiteral<ContextualRequestMessageHandler<Hub>>() {});
      handlerBinder.addBinding().to(HubGetAttributesHandler.class);
      handlerBinder.addBinding().to(FirmwareUpdateProgressMessageHandler.class);
      handlerBinder.addBinding().to(HubDeleteHandler.class);

      Multibinder<ContextualEventMessageHandler<Place>> placeEventBinder = bindSetOf(new TypeLiteral<ContextualEventMessageHandler<Place>>() {});
      placeEventBinder.addBinding().to(PlaceValueChangeListener.class);

      Multibinder<ContextualEventMessageHandler<Hub>> eventBinder = bindSetOf(new TypeLiteral<ContextualEventMessageHandler<Hub>>() {});
      eventBinder.addBinding().to(PlaceDeletedListener.class);

      Multibinder<PlatformService> services = bindSetOf(PlatformService.class);
      services.addBinding().to(HubService.class);
      services.addBinding().to(HubHeartbeatListener.class);
      services.addBinding().to(HubEventListener.class);
   }
   
}

