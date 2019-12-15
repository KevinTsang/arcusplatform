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
package com.arcussmarthome.platform.alarm.incident;

import java.util.UUID;

import com.arcussmarthome.platform.PagedResults;

public interface AlarmIncidentDAO {

   default AlarmIncident current(UUID placeId) {
      AlarmIncident incident = latest(placeId);
      if(incident == null || incident.isCleared()) {
         return null;
      }
      return incident;
   }

   AlarmIncident latest(UUID placeId);
   
   AlarmIncident findById(UUID placeId, UUID incidentId);

   default PagedResults<AlarmIncident> listIncidentsByPlace(UUID placeId, int limit) {
   	AlarmIncidentQuery query = new AlarmIncidentQuery();
   	query.setPlaceId(placeId);
   	query.setLimit(limit);
   	return listIncidentsByQuery(query);
   }
   
   PagedResults<AlarmIncident> listIncidentsByQuery(AlarmIncidentQuery query);
   
   void upsert(AlarmIncident incident);

   boolean updateMonitoringState(UUID placeId, UUID incidentId, AlarmIncident.MonitoringState state);

   void delete(UUID placeId, UUID incidentId);

}


