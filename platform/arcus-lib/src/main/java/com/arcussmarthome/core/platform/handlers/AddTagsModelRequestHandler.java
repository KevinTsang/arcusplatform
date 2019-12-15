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
/**
 * 
 */
package com.arcussmarthome.core.platform.handlers;

import java.util.HashSet;
import java.util.Set;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Singleton;
import com.arcussmarthome.capability.definition.AttributeType;
import com.arcussmarthome.capability.definition.AttributeTypes;
import com.arcussmarthome.core.platform.ContextualRequestMessageHandler;
import com.arcussmarthome.messages.MessageBody;
import com.arcussmarthome.messages.PlatformMessage;
import com.arcussmarthome.messages.capability.Capability;
import com.arcussmarthome.messages.model.Model;

/**
 * 
 */
@Singleton
public class AddTagsModelRequestHandler implements ContextualRequestMessageHandler<Model> {
   private static final AttributeType TYPE_STRING_SET = 
         AttributeTypes.parse("set<string>");
   
   @Override
   public String getMessageType() {
      return Capability.AddTagsRequest.NAME;
   }

   @Override
   public MessageBody handleRequest(Model model, PlatformMessage message) {
      MessageBody request = message.getValue();
      Set<String> tagsToAdd = Capability.AddTagsRequest.getTags(request);
      Set<String> currentTags = (Set<String>) TYPE_STRING_SET.coerce( model.getAttribute(Capability.ATTR_TAGS) );
      if(currentTags == null) {
         currentTags = ImmutableSet.of();
      }
      // make it modifiable
      Set<String> newTags = new HashSet<>(currentTags);
      newTags.addAll(tagsToAdd);
      model.setAttribute(Capability.ATTR_TAGS, newTags);
      return Capability.AddTagsResponse.instance();
   }

}

