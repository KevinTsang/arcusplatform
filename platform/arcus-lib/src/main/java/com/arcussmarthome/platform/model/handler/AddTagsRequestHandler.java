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
package com.arcussmarthome.platform.model.handler;

import java.util.HashSet;
import java.util.Set;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.arcussmarthome.messages.capability.Capability;
import com.arcussmarthome.messages.capability.Capability.AddTagsRequest;
import com.arcussmarthome.messages.listener.annotation.Request;
import com.arcussmarthome.messages.model.PersistentModel;
import com.arcussmarthome.platform.model.PersistentModelWrapper;
import com.arcussmarthome.util.TypeMarker;

@Singleton
public class AddTagsRequestHandler {

	@Request(AddTagsRequest.NAME)
	public void addTags(PersistentModelWrapper<? extends PersistentModel> wrapper, @Named(AddTagsRequest.ATTR_TAGS) Set<String> tags) {
		Set<String> updated = new HashSet<>(wrapper.model().getAttribute(TypeMarker.setOf(String.class), Capability.ATTR_TAGS, ImmutableSet.<String>of()));
		updated.addAll(tags);
		wrapper.model().setAttribute(Capability.ATTR_TAGS, updated);
		wrapper.save();
	}
}

