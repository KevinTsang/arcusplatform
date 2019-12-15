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
package com.arcussmarthome.platform.subsystem.incident;

import com.google.inject.Singleton;
import com.arcussmarthome.common.subsystem.SubsystemExecutor;
import com.arcussmarthome.messages.MessageConstants;
import com.arcussmarthome.messages.PlatformMessage;
import com.arcussmarthome.messages.listener.annotation.Request;

@Singleton
public class AlarmIncidentRequestHandler {
	

	@Request(value = MessageConstants.MSG_ANY_MESSAGE_TYPE, response = false)
	public void handleRequest(SubsystemExecutor executor, PlatformMessage message) {
		// dispatch to subsystem which handles sending the proper response
		executor.onPlatformMessage(message);
	}

}

