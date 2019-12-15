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
package com.arcussmarthome.bridge.server.http.impl.auth;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

import com.google.inject.Singleton;
import com.arcussmarthome.bridge.server.http.RequestAuthorizer;

@Singleton
public class AlwaysAllow implements RequestAuthorizer {
   
   @Override
   public boolean isAuthorized(ChannelHandlerContext ctx, FullHttpRequest req) {
      return true;
   }

   @Override
   public boolean handleFailedAuth(ChannelHandlerContext ctx, FullHttpRequest req) {
      return false;
   }
}

