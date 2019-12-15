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
package com.arcussmarthome.platform.services.population.handlers;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.arcussmarthome.core.dao.PlaceDAO;
import com.arcussmarthome.core.dao.PopulationDAO;
import com.arcussmarthome.core.platform.ContextualRequestMessageHandler;
import com.arcussmarthome.core.platform.PlatformMessageBus;
import com.arcussmarthome.messages.MessageBody;
import com.arcussmarthome.messages.PlatformMessage;
import com.arcussmarthome.messages.model.Place;
import com.arcussmarthome.messages.service.PopulationService;
import com.arcussmarthome.messages.service.PopulationService.RemovePlacesRequest;
import com.arcussmarthome.messages.type.Population;
import com.arcussmarthome.population.PopulationUtils;

public class RemovePlacesFromPopulationHandler implements ContextualRequestMessageHandler<Population> {
   private static final Logger logger = LoggerFactory.getLogger(RemovePlacesFromPopulationHandler.class);
   
   private final PopulationDAO populationDao;
   private final PlaceDAO placeDao;
   private final PlatformMessageBus platformBus;

   @Inject
   public RemovePlacesFromPopulationHandler(PopulationDAO populationDao, PlaceDAO placeDao, PlatformMessageBus platformBus) {
      this.populationDao = populationDao;
      this.placeDao = placeDao;
      this.platformBus = platformBus;
   }

   @Override
   public String getMessageType() {
      return RemovePlacesRequest.NAME;
   }

   @Override
   public MessageBody handleRequest(Population context, PlatformMessage msg) {
      return doHandleRequest(context, msg);
   }

   @Override
   public MessageBody handleStaticRequest(PlatformMessage msg) {
      return doHandleRequest(null, msg);
   }
   
   private MessageBody doHandleRequest(Population context, PlatformMessage msg) {
      MessageBody bodyMsg = msg.getValue();
      Population curPopulation = PopulationUtils.validateAndGetPopulationFromRequest(context, RemovePlacesRequest.ATTR_POPULATION, bodyMsg, populationDao);      
      Set<UUID> placeIDs = PopulationUtils.validateAndGetPlacesFromRequest(RemovePlacesRequest.ATTR_PLACES, bodyMsg);
      List<Place> places = placeDao.findByPlaceIDIn(placeIDs);
      if(places != null && !places.isEmpty()) {
         for(Place curPlace : places) {
            //Make sure the current population is indeed the given population
            if(curPopulation.getName().equals(curPlace.getPopulation())) {
               curPlace.setPopulation(null);
               placeDao.save(curPlace);
               PopulationUtils.emitValueChangeForPopulation(null, curPlace.getId(), platformBus);
            }else{
               logger.warn("Fail to remove place [{}] from population [{}] because its current population does not match.", curPlace.getId(), curPopulation.getName() );
            }
         }
      }
      
      return PopulationService.RemovePlacesResponse.instance();
   }
   
   

}

