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
package com.arcussmarthome.driver.service;

import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.name.Named;
import com.arcussmarthome.bootstrap.guice.AbstractIrisModule;
import com.arcussmarthome.common.scheduler.ExecutorScheduler;
import com.arcussmarthome.common.scheduler.Scheduler;
import com.arcussmarthome.driver.platform.PlatformDriverExecutorRegistry;
import com.arcussmarthome.driver.platform.PlatformDriverService;
import com.arcussmarthome.driver.service.executor.DriverExecutorRegistry;
import com.arcussmarthome.driver.service.handler.DriverServiceRequestHandler;
import com.arcussmarthome.driver.service.handler.ForceRemoveRequestHandler;
import com.arcussmarthome.driver.service.handler.ListHistoryEntriesHandler;
import com.arcussmarthome.driver.service.handler.LostRequestHandler;
import com.arcussmarthome.driver.service.handler.MessageHandler;
import com.arcussmarthome.driver.service.handler.RemoveRequestHandler;
import com.arcussmarthome.driver.service.handler.UpgradeDriverRequestHandler;
import com.arcussmarthome.driver.service.init.DefaultNameInitializer;
import com.arcussmarthome.driver.service.registry.CompositeDriverRegistry;
import com.arcussmarthome.driver.service.registry.DriverRegistry;
import com.arcussmarthome.messages.MessageConstants;
import com.arcussmarthome.messages.address.Address;
import com.arcussmarthome.messages.address.AddressMatcher;
import com.arcussmarthome.messages.address.AddressMatchers;
import com.arcussmarthome.prodcat.ProductCatalogModule;
import com.arcussmarthome.prodcat.ProductCatalogReloadListener;
import com.arcussmarthome.util.ThreadPoolBuilder;
import com.netflix.governator.annotations.Modules;

/**
 * Basic driver configuration bits.
 */
@Modules(include = ProductCatalogModule.class)
public class DriverModule extends AbstractIrisModule {

	/* (non-Javadoc)
	 * @see com.google.inject.AbstractModule#configure()
	 */
	@Override
	protected void configure() {
		bind(DriverService.class).to(PlatformDriverService.class).asEagerSingleton();

		Multibinder<DriverServiceRequestHandler> handlers = bindSetOf(DriverServiceRequestHandler.class);
		handlers.addBinding().to(UpgradeDriverRequestHandler.class);
		handlers.addBinding().to(RemoveRequestHandler.class);
		handlers.addBinding().to(ForceRemoveRequestHandler.class);
      handlers.addBinding().to(LostRequestHandler.class);
		handlers.addBinding().to(MessageHandler.class);
		handlers.addBinding().to(ListHistoryEntriesHandler.class);

      bind(DeviceInitializer.class)
         .to(DefaultNameInitializer.class);

		bind(DriverExecutorRegistry.class).to(PlatformDriverExecutorRegistry.class);
	}

	@Provides @Singleton @Named(DriverConfig.NAMED_EXECUTOR)
	public ThreadPoolExecutor driverExecutor(DriverConfig config) {
	   return
            new ThreadPoolBuilder()
               .withBlockingBacklog()
               .withMaxPoolSize(config.getDriverThreadPoolSize())
               .withNameFormat("driver-thread-%d")
               .withMetrics("driver.service")
               .build()
               ;
	}
	
	@Provides @Singleton
	public Scheduler driverScheduler(DriverConfig config, @Named(DriverConfig.NAMED_EXECUTOR) ThreadPoolExecutor workerPool) {
	   ScheduledExecutorService schedulerPool =
	         Executors.newScheduledThreadPool(
	               config.getSchedulerThreadPoolSize(),
	               ThreadPoolBuilder
	                  .defaultFactoryBuilder()
	                  .setNameFormat("driver-scheduler-%d")
	                  .build()
            );
	   return new ExecutorScheduler(schedulerPool, workerPool);
	}
	
	@Provides @Singleton @Named("ProtocolMatchers")
	public Set<AddressMatcher> provideProtocolAddressMatchers() {
	   return AddressMatchers.platformNamespaces(MessageConstants.BROADCAST, MessageConstants.DRIVER);
	}

	@Provides @Singleton @Named("PlatformMatchers")
	public Set<AddressMatcher> providePlatformAddressMatchers() {
	   return AddressMatchers.platformNamespaces(MessageConstants.DRIVER);
	}

	@Provides @Singleton
	public DriverRegistry provideDriverRegistry(Set<DriverRegistry> registries) {
	   return new CompositeDriverRegistry(registries.toArray(new DriverRegistry[registries.size()]));
	}
	
   @Provides
   @Singleton
   @Named(ProductCatalogReloadListener.GENERIC_MESSAGE_BUS_ACTOR_ADDRESS)
   public Address provideMessageBusSrcAddress() {
      return Address.fromString(MessageConstants.SERVICE + ":" + Address.PLATFORM_DRIVER_GROUP + ":");
   }

}

