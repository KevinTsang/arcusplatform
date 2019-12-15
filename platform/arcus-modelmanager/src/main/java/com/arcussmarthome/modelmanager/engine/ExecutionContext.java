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
package com.arcussmarthome.modelmanager.engine;

import com.datastax.driver.core.Session;
import com.arcussmarthome.modelmanager.changelog.dao.ChangeSetDAO;
import com.arcussmarthome.modelmanager.context.ManagerContext;
import com.arcussmarthome.modelmanager.version.dao.VersionHistoryDAO;

public class ExecutionContext {

   private final Session session;
   private final ManagerContext managerContext;
   private final ChangeSetDAO changesetDao;
   private final VersionHistoryDAO historyDao;

   public ExecutionContext(Session session, ManagerContext managerContext) {
      this.session = session;
      this.managerContext = managerContext;
      this.changesetDao = new ChangeSetDAO(this.session);
      this.historyDao = new VersionHistoryDAO(this.session);
   }

   public Session getSession() {
      return session;
   }

   public ManagerContext getManagerContext() {
      return managerContext;
   }

   public ChangeSetDAO getChangeSetDAO() {
      return changesetDao;
   }

   public VersionHistoryDAO getVersionHistoryDAO() {
      return historyDao;
   }
}

