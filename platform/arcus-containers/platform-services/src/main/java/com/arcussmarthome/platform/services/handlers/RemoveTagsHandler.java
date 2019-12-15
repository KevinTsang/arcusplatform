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
package com.arcussmarthome.platform.services.handlers;

import java.util.HashSet;
import java.util.Set;

import com.google.inject.Inject;
import com.arcussmarthome.core.dao.CRUDDao;
import com.arcussmarthome.core.platform.PlatformMessageBus;
import com.arcussmarthome.messages.capability.Capability;
import com.arcussmarthome.messages.model.BaseEntity;

public class RemoveTagsHandler<T extends BaseEntity<?, T>> extends AbstractTagHandler<T> {

   @Inject
   public RemoveTagsHandler(CRUDDao<?, T> dao, PlatformMessageBus platformBus) {
      super(dao, platformBus);
   }

   @Override
   public String getMessageType() {
      return Capability.RemoveTagsRequest.NAME;
   }

   @Override
   protected Set<String> updateTags(Set<String> curTags, Set<String> updates) {
      Set<String> cur = new HashSet<>(curTags);
      cur.removeAll(updates);
      return cur;
   }
}

