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
import com.arcussmarthome.core.dao.PersonDAO;
import com.netflix.governator.annotations.Modules;

@Modules(include={
   CassandraModule.class,
   CassandraPersonPlaceAssocDAOModule.class
})
public class CassandraPersonDAOModule extends AbstractModule {
   @Override
   protected void configure() {
      bind(PersonDAO.class).to(PersonDAOImpl.class);
   }
}

