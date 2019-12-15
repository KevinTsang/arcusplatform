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
package com.arcussmarthome.security.authz.filter;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.google.inject.Singleton;
import com.arcussmarthome.messages.MessageBody;
import com.arcussmarthome.messages.PlatformMessage;
import com.arcussmarthome.security.authz.AuthorizationContext;
import com.arcussmarthome.security.authz.permission.PermissionCode;

@Singleton
public class DefaultMessageFilter extends MessageFilter {

   @Override
   public Set<String> getSupportedMessageTypes() {
      return Collections.<String>emptySet();
   }

   @Override
   public PlatformMessage filter(AuthorizationContext context, UUID place, PlatformMessage message) {
      MessageBody body = message.getValue();
      String objectId = message.getSource().getId() == null ? "*" : String.valueOf(message.getSource().getId());
      Map<String,Object> filteredAttributes = filterAttributes(context, place, PermissionCode.r, objectId, body.getAttributes());
      return createNewMessage(MessageBody.buildMessage(body.getMessageType(), filteredAttributes), message);
   }
}

