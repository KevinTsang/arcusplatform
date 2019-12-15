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
package com.arcussmarthome.client.server.rest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.arcussmarthome.bridge.metrics.BridgeMetrics;
import com.arcussmarthome.bridge.server.config.RESTHandlerConfig;
import com.arcussmarthome.bridge.server.http.HttpSender;
import com.arcussmarthome.bridge.server.http.annotation.HttpPost;
import com.arcussmarthome.bridge.server.http.handlers.RESTHandler;
import com.arcussmarthome.bridge.server.http.impl.auth.AlwaysAllow;
import com.arcussmarthome.client.nws.SameCodeManager;
import com.arcussmarthome.client.nws.SameState;
import com.arcussmarthome.messages.ClientMessage;
import com.arcussmarthome.messages.MessageBody;
import com.arcussmarthome.messages.service.NwsSameCodeService;

@Singleton
@HttpPost("/" + NwsSameCodeService.NAMESPACE + "/ListSameStates")
public class ListSameStatesRESTHandler extends RESTHandler {

   private final SameCodeManager manager;

   @Inject
   public ListSameStatesRESTHandler(AlwaysAllow alwaysAllow, BridgeMetrics metrics, SameCodeManager manager, RESTHandlerConfig restHandlerConfig) {
      super(alwaysAllow, new HttpSender(ListSameStatesRESTHandler.class, metrics), restHandlerConfig);
      this.manager = manager;
   }

   @Override
   protected MessageBody doHandle(ClientMessage request) throws Exception {
      List<SameState> sameStates = manager.listSameStates();
      
      return NwsSameCodeService.ListSameStatesResponse.builder()
            .withSameStates(sameStates.stream().map((s) -> sameStatesToMap(s)).collect(Collectors.toList()))
            .build();
   }

   private Map<String, Object> sameStatesToMap(SameState sameState) {
      Map<String, Object> asMap = new HashMap<>();
      asMap.put(com.arcussmarthome.messages.type.SameState.ATTR_STATECODE, sameState.getStateCode());
      asMap.put(com.arcussmarthome.messages.type.SameState.ATTR_STATE, sameState.getState());
      return asMap;
   }
}

