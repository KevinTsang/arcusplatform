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
package com.arcussmarthome.bridge.server.http.handlers;

import com.arcussmarthome.io.json.JSON;
import com.arcussmarthome.messages.ClientMessage;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.util.CharsetUtil;

public class RESTHandlerUtil {
	public static ClientMessage decode(FullHttpRequest request) {
		// TODO assert valid content type, etc?
		String json = request.content().toString(CharsetUtil.UTF_8);
		return JSON.fromJson(json, ClientMessage.class);
	}
}

