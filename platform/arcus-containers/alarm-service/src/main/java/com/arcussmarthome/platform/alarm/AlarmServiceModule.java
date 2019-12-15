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
package com.arcussmarthome.platform.alarm;

import java.util.concurrent.ExecutorService;

import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.arcussmarthome.bootstrap.guice.AbstractIrisModule;
import com.arcussmarthome.core.dao.cassandra.CassandraPlaceDAOModule;
import com.arcussmarthome.core.dao.cassandra.CassandraResourceBundleDAOModule;
import com.arcussmarthome.core.messaging.kafka.KafkaModule;
import com.arcussmarthome.platform.alarm.incident.AlarmIncidentDAO;
import com.arcussmarthome.platform.alarm.incident.CassandraAlarmIncidentDAO;
import com.arcussmarthome.platform.alarm.notification.calltree.cassandra.CassandraNotificationDaoModule;
import com.arcussmarthome.platform.alarm.service.AlarmService;
import com.arcussmarthome.platform.history.cassandra.CassandraHistoryDAOModule;
import com.arcussmarthome.platform.model.ModelDaoModule;
import com.arcussmarthome.platform.rule.RuleDaoModule;
import com.arcussmarthome.platform.subscription.IrisSubscriptionModule;
import com.arcussmarthome.population.PlacePopulationCacheModule;
import com.arcussmarthome.util.ThreadPoolBuilder;
import com.netflix.governator.annotations.Modules;

@Modules(include = {
      KafkaModule.class,
      CassandraResourceBundleDAOModule.class,
      CassandraPlaceDAOModule.class,
      ModelDaoModule.class,
      CassandraNotificationDaoModule.class,
      CassandraHistoryDAOModule.class,
      RuleDaoModule.class,
      IrisSubscriptionModule.class,
      PlacePopulationCacheModule.class
})
public class AlarmServiceModule extends AbstractIrisModule {

   @Override
   protected void configure() {
      bind(AlarmIncidentDAO.class).to(CassandraAlarmIncidentDAO.class);
      bind(AlarmService.class).asEagerSingleton();
   }

   @Provides
   @Singleton
   @Named(AlarmService.NAME_EXECUTOR_POOL)
   public ExecutorService alarmPool() {
      return
            new ThreadPoolBuilder()
                  .withBlockingBacklog()
                  .withMaxPoolSize(100)
                  .withKeepAliveMs(10000)
                  .withMetrics("alarm")
                  .withNameFormat("alarm-%d")
                  .build();
   }
}

