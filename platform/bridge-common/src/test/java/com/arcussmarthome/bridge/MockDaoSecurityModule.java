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
package com.arcussmarthome.bridge;

import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.easymock.EasyMock;

import com.google.inject.Inject;
import com.google.inject.binder.AnnotatedBindingBuilder;
import com.arcussmarthome.bridge.metrics.BridgeMetrics;
import com.arcussmarthome.bridge.server.http.RequestAuthorizer;
import com.arcussmarthome.bridge.server.http.impl.auth.SessionAuth;
import com.arcussmarthome.netty.security.IrisNettyAuthorizationContextLoader;
import com.arcussmarthome.netty.security.IrisNettyNoopAuthorizationContextLoader;
import com.arcussmarthome.security.SecurityModule;
import com.arcussmarthome.security.SessionConfig;
import com.arcussmarthome.security.credentials.CredentialsHashingStrategy;
import com.arcussmarthome.security.credentials.PlainCredentialsHashingStrategy;
import com.arcussmarthome.security.dao.AppHandoffDao;
import com.arcussmarthome.security.dao.AuthenticationDAO;
import com.arcussmarthome.security.handoff.AppHandoffRealm;

public class MockDaoSecurityModule extends SecurityModule {
   
   @Inject
   public MockDaoSecurityModule(SessionConfig config) {
      super(config);
   }

   @Override
   public void configure() {
      super.configure();
      bind(BridgeMetrics.class).toInstance(new BridgeMetrics("test"));
      bind(RequestAuthorizer.class).to(SessionAuth.class);
      bind(IrisNettyAuthorizationContextLoader.class).to(IrisNettyNoopAuthorizationContextLoader.class);
      expose(BridgeMetrics.class);
      expose(RequestAuthorizer.class);
      expose(IrisNettyAuthorizationContextLoader.class);
      
      bindRealm().to(AppHandoffRealm.class);
      bind(AppHandoffDao.class).toInstance(EasyMock.createMock(AppHandoffDao.class));
      expose(AppHandoffDao.class);
   }

   @Override
   protected void bindAuthenticationDAO(AnnotatedBindingBuilder<AuthenticationDAO> bind) {
      bind.toInstance(EasyMock.createMock(AuthenticationDAO.class));
      expose(AuthenticationDAO.class);
   }

   @Override
   protected void bindSessionDAO(AnnotatedBindingBuilder<SessionDAO> bind) {
      bind.toInstance(EasyMock.createMock(SessionDAO.class));
      expose(SessionDAO.class);
   }

   // THIS IS ONLY SAFE FOR A TEST-CASE
   @Override
   protected void bindCredentialsHashingStrategy(
         AnnotatedBindingBuilder<CredentialsHashingStrategy> bind) {
      bind.to(PlainCredentialsHashingStrategy.class).asEagerSingleton();
   }

}

