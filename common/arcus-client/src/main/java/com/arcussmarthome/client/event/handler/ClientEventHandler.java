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
package com.arcussmarthome.client.event.handler;

import com.arcussmarthome.client.event.ClientEvent;
import com.arcussmarthome.messages.ClientMessage;

public interface ClientEventHandler<E extends ClientEvent> {
	/**
	 * 
	 * Handle the client message coming in.  This could include such activities
	 * as caching the results, updating the UI, etc.
	 * 
	 * @param message deserialized message.
	 */
	public void handleMessage(ClientMessage message);
	
	/**
	 * 
	 * Method used to publish the event within the client.  Override to send to your
	 * destination of choice.
	 * 
	 * @param clientEvent
	 */
	public void publishEvent(E clientEvent);
}

