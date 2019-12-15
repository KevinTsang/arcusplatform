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
package com.arcussmarthome.oculus.modules.incident;

import java.util.Collection;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.arcussmarthome.capability.definition.DefinitionRegistry;
import com.arcussmarthome.client.ClientRequest;
import com.arcussmarthome.client.IrisClientFactory;
import com.arcussmarthome.client.capability.AlarmSubsystem;
import com.arcussmarthome.client.event.ClientFuture;
import com.arcussmarthome.client.model.AlarmIncidentModel;
import com.arcussmarthome.oculus.modules.BaseController;

/**
 * 
 */
@Singleton
public class IncidentController extends BaseController<AlarmIncidentModel> {

   @Inject
   public IncidentController(DefinitionRegistry registry) {
      super(AlarmIncidentModel.class);
   }

   @Override
   protected ClientFuture<? extends Collection<Map<String, Object>>> doLoad() {
      ClientRequest request = new ClientRequest();
      request.setAddress("SERV:" + AlarmSubsystem.NAMESPACE + ":" + getPlaceId());
      request.setCommand(AlarmSubsystem.ListIncidentsRequest.NAME);
      request.setRestfulRequest(false);
      return
         IrisClientFactory
            .getClient()
            .request(request)
            .transform((response) -> new AlarmSubsystem.ListIncidentsResponse(response).getIncidents());
   }

}

