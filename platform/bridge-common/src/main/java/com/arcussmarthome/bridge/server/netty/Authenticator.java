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
package com.arcussmarthome.bridge.server.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.cookie.DefaultCookie;

/**
 * Handles authentication and authorization of WebRequests.
 */
public interface Authenticator {
   
   public FullHttpResponse authenticateRequest(Channel channel, FullHttpRequest req);
   
   public FullHttpResponse authenticateRequest(Channel channel, String username, String password, String isPublic, ByteBuf responseContentIfSuccess);

   public DefaultCookie createCookie(String value);

   public DefaultCookie expireCookie();
}

