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
package com.arcussmarthome.core.dao.file;

import com.google.inject.Inject;
import com.arcussmarthome.bootstrap.guice.AbstractIrisModule;
import com.arcussmarthome.capability.attribute.transform.AttributeMapTransformModule;
import com.arcussmarthome.core.dao.AccountDAO;
import com.arcussmarthome.core.dao.AuthorizationGrantDAO;
import com.arcussmarthome.core.dao.HubBlacklistDAO;

public class FileDAOModule extends AbstractIrisModule {

   @Inject
   public FileDAOModule(AttributeMapTransformModule attributeTranformer) {
   }

	@Override
	protected void configure() {
		bind(AccountDAO.class).to(AccountDAOFileImpl.class);
		bind(HubBlacklistDAO.class).to(HubBlacklistDAOFileImpl.class);
		bind(AuthorizationGrantDAO.class).to(AuthorizationGrantFileDAOImpl.class);
	}

}

