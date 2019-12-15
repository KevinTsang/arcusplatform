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
package com.arcussmarthome.platform.rule;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

import javax.annotation.PreDestroy;

import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.arcussmarthome.bootstrap.guice.AbstractIrisModule;
import com.arcussmarthome.core.dao.cassandra.CassandraDAOModule;
import com.arcussmarthome.core.messaging.kafka.KafkaModule;
import com.arcussmarthome.platform.model.ModelDaoModule;
import com.arcussmarthome.platform.rule.catalog.RuleCatalogModule;
import com.arcussmarthome.platform.rule.environment.ContextLoader;
import com.arcussmarthome.platform.rule.environment.DefaultPlaceExecutorFactory;
import com.arcussmarthome.platform.rule.environment.DefaultPlaceExecutorRegistry;
import com.arcussmarthome.platform.rule.environment.PlaceExecutorFactory;
import com.arcussmarthome.platform.rule.environment.PlaceExecutorRegistry;
import com.arcussmarthome.platform.rule.environment.SimpleContextLoader;
import com.arcussmarthome.platform.rule.service.RuleService;
import com.arcussmarthome.platform.rule.service.SceneRequestHandler;
import com.arcussmarthome.platform.rule.service.SceneServiceHandler;
import com.arcussmarthome.platform.rule.service.SceneServiceModule;
import com.arcussmarthome.platform.rule.service.SceneTemplateRequestHandler;
import com.arcussmarthome.platform.scene.SceneTemplateManager;
import com.arcussmarthome.platform.scene.SceneTemplateManagerImpl;
import com.arcussmarthome.platform.subsystem.SubsystemDaoModule;
import com.arcussmarthome.population.PlacePopulationCacheModule;
import com.arcussmarthome.util.ThreadPoolBuilder;

/**
 * 
 */
public class RuleServiceModule extends AbstractIrisModule {
   
   @Inject(optional = true) @Named("rule.service.threads.max")
   private int threads = 100;
   @Inject(optional = true) @Named("rule.service.threads.keepAliveMs")
   private int keepAliveMs = 10000;

   private ExecutorService serviceExecutor;
   
   @Inject
   public RuleServiceModule(
         KafkaModule kafka,
         CassandraDAOModule cassandra,
         RuleDaoModule ruleDaos,
         SceneServiceModule sceneRequestHandlers,
         PlatformRuleModule platformRules,
         RuleCatalogModule catalog,
         SubsystemDaoModule subsystems,
         ModelDaoModule models,
         PlacePopulationCacheModule populationCahe
   ) {
   }
   
   @PreDestroy
   public void shutdown() {
      serviceExecutor.shutdown();
   }
   
   @Override
   protected void configure() {
      serviceExecutor =
            new ThreadPoolBuilder()
               .withBlockingBacklog()
               .withMaxPoolSize(threads)
               .withNameFormat("rule-service-%d")
               .withMetrics("service.rule")
               .build();
      
      bind(RuleService.class);
      bind(PlaceExecutorFactory.class).to(DefaultPlaceExecutorFactory.class);
      bind(PlaceExecutorRegistry.class).to(DefaultPlaceExecutorRegistry.class);
      bind(SceneTemplateManager.class).to(SceneTemplateManagerImpl.class);
      bind(ContextLoader.class).to(SimpleContextLoader.class);
   }
   
   @Provides @Singleton @Named(RuleService.PROP_THREADPOOL)
   public Executor ruleExecutor() {
      return serviceExecutor;
   }

   @Provides @Singleton @Named(SceneRequestHandler.PROP_THREADPOOL)
   public Executor sceneRequestExecutor() {
      return serviceExecutor;
   }

   @Provides @Singleton @Named(SceneServiceHandler.PROP_THREADPOOL)
   public Executor sceneServiceExecutor() {
      return serviceExecutor;
   }

   @Provides @Singleton @Named(SceneTemplateRequestHandler.PROP_THREADPOOL)
   public Executor sceneTemplateExecutor() {
      return serviceExecutor;
   }

}

