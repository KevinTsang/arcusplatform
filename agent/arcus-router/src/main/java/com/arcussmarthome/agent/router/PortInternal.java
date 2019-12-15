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
package com.arcussmarthome.agent.router;

import org.eclipse.jdt.annotation.Nullable;

import com.arcussmarthome.agent.addressing.HubAddr;
import com.arcussmarthome.messages.PlatformMessage;
import com.arcussmarthome.protocol.ProtocolMessage;

interface PortInternal extends Port {
   void handle(HubAddr addr, PlatformMessage message);
   void handle(HubAddr addr, ProtocolMessage message);
   void handle(Object destination, Object message);
   void queue(Object destination, Object message);

   boolean isListenerOnly();
   boolean isListenAll();

   void enqueue(@Nullable HubAddr addr, Message msg, boolean snoop) throws InterruptedException;

   String getName();
   @Nullable String getServiceId();
   @Nullable String getProtocolId();
}

