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
package com.arcussmarthome.video.netty;

import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.arcussmarthome.bridge.server.client.ClientFactory;
import com.arcussmarthome.bridge.server.session.ClientToken;
import com.arcussmarthome.bridge.server.session.Session;
import com.arcussmarthome.bridge.server.session.SessionRegistry;

@Singleton
public class NullSessionRegistryImpl implements SessionRegistry {
   private final ClientFactory clientFactory;
   
   @Inject
   public NullSessionRegistryImpl(ClientFactory clientFactory) {
      this.clientFactory = clientFactory;
   }

   @Override
   public Session getSession(ClientToken ct) {
      return null;
   }

   @Override
   public void putSession(Session session) {
   }

   @Override
   public void destroySession(Session session) {
   }

   @Override
   public Iterable<Session> getSessions() {
      return ImmutableList.of();
   }

   @Override
   public ClientFactory getClientFactory() {
      return clientFactory;
   }
}

