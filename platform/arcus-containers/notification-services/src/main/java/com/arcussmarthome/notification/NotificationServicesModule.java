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
package com.arcussmarthome.notification;

import java.util.concurrent.ExecutorService;

import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import com.arcussmarthome.bootstrap.guice.AbstractIrisModule;
import com.arcussmarthome.core.dao.cassandra.CassandraDAOModule;
import com.arcussmarthome.core.messaging.MessagesModule;
import com.arcussmarthome.core.messaging.kafka.KafkaModule;
import com.arcussmarthome.core.template.TemplateModule;
import com.arcussmarthome.platform.rule.RuleDaoModule;
import com.arcussmarthome.util.ThreadPoolBuilder;

public class NotificationServicesModule extends AbstractIrisModule {

	@Inject
	public NotificationServicesModule(
			NotificationModule notification,
			MessagesModule messages,
			KafkaModule kafka,
			CassandraDAOModule cassandra,
			RuleDaoModule ruleDaoModule,
			TemplateModule template)
	{
	}

	@Override
	protected void configure() {
	   bind(NotificationServiceConfig.class);
	}

	@Provides @Named("notifications.executor")
	public ExecutorService getNotificationsExecutor(NotificationServiceConfig config) {
      return new ThreadPoolBuilder()
         .withMaxPoolSize(config.getMaxThreads())
         .withKeepAliveMs(config.getThreadKeepAliveMs())
         .withNameFormat("notification-dispatcher-%d")
         .withBlockingBacklog()
         .withMetrics("service.notifications")
         .build();
	}
}

