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
package com.arcussmarthome.core.dao.cassandra;

import com.google.inject.AbstractModule;
import com.arcussmarthome.core.dao.file.HubBlacklistDAOModule;
import com.arcussmarthome.core.dao.file.PopulationDAOModule;
import com.arcussmarthome.platform.history.cassandra.CassandraHistoryDAOModule;
import com.netflix.governator.annotations.Modules;

@Modules(include={
      CassandraModule.class,
      CassandraAccountDAOModule.class,
      CassandraPersonDAOModule.class,
      CassandraPlaceDAOModule.class,
      CassandraDeviceDAOModule.class,
      CassandraHubDAOModule.class,
      HubBlacklistDAOModule.class,  //TODO - should not be here probably, but need to find out all the references of HubBlacklistDAO
      CassandraAuthorizationGrantDAOModule.class,
      CassandraResourceBundleDAOModule.class,
      CassandraMobileDeviceDAOModule.class,
      PopulationDAOModule.class,  //TODO - should not be here probably, but need to find out all the references of PopulationDAO
      CassandraHistoryDAOModule.class,
      CassandraInvitationDAOModule.class,
      CassandraPersonPlaceAssocDAOModule.class,
      CassandraPreferencesDAOModule.class
})
public class CassandraDAOModule extends AbstractModule {
   @Override
   protected void configure() {
   }
}

