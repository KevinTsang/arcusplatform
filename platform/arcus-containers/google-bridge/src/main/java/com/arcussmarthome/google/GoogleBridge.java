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
package com.arcussmarthome.google;

import com.google.inject.Inject;
import com.arcussmarthome.bootstrap.ServiceLocator;
import com.arcussmarthome.bridge.server.BridgeServer;
import com.arcussmarthome.bridge.server.ServerRunner;
import com.arcussmarthome.core.IrisAbstractApplication;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.SecurityManager;

import java.util.Collections;

public class GoogleBridge extends BridgeServer {

   @Inject
   public GoogleBridge(ServerRunner runner) {
      super(runner);
   }

   @Override
   protected void start() throws Exception {
      SecurityUtils.setSecurityManager(ServiceLocator.getInstance(SecurityManager.class));
      super.start();
   }

   public static void main(String[] args) {
      IrisAbstractApplication.exec(GoogleBridge.class, Collections.singletonList(GoogleBridgeModule.class), args);
   }

}

