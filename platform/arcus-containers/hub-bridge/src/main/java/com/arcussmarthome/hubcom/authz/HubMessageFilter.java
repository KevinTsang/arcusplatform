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
package com.arcussmarthome.hubcom.authz;

import com.arcussmarthome.hubcom.server.session.HubSession;
import com.arcussmarthome.messages.PlatformMessage;
import com.arcussmarthome.protocol.ProtocolMessage;

public interface HubMessageFilter {

   // accept incoming messages
   boolean acceptFromHub(HubSession session, PlatformMessage msg);
   boolean acceptFromHub(HubSession session, ProtocolMessage msg);

   // handlers for out going messages to allow updating state based on messages from the platform
   boolean acceptFromPlatform(HubSession session, PlatformMessage msg);
   boolean acceptFromProtocol(HubSession session, ProtocolMessage msg);

}

