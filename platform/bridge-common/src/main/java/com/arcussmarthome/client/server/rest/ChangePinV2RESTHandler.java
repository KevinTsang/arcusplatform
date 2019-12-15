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

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.arcussmarthome.bridge.metrics.BridgeMetrics;
import com.arcussmarthome.bridge.server.client.ClientFactory;
import com.arcussmarthome.bridge.server.config.RESTHandlerConfig;
import com.arcussmarthome.bridge.server.http.HttpSender;
import com.arcussmarthome.bridge.server.http.annotation.HttpPost;
import com.arcussmarthome.bridge.server.http.impl.auth.SessionAuth;
import com.arcussmarthome.core.dao.AuthorizationGrantDAO;
import com.arcussmarthome.core.dao.PersonDAO;
import com.arcussmarthome.core.dao.exception.PinNotUniqueAtPlaceException;
import com.arcussmarthome.core.platform.PlatformMessageBus;
import com.arcussmarthome.messages.ClientMessage;
import com.arcussmarthome.messages.MessageBody;
import com.arcussmarthome.messages.capability.PersonCapability;
import com.arcussmarthome.messages.model.Person;
import com.arcussmarthome.population.PlacePopulationCacheManager;
import com.arcussmarthome.security.authz.AuthorizationGrant;

@Singleton
@HttpPost("/person/ChangePinV2")
public class ChangePinV2RESTHandler extends BasePinRESTHandler {

   @Inject
   public ChangePinV2RESTHandler(
         ClientFactory clientFactory,
         PersonDAO personDao,
         AuthorizationGrantDAO authGrantDao,
         PlatformMessageBus bus,
         PlacePopulationCacheManager populationCacheMgr,
         BridgeMetrics metrics,
         SessionAuth auth,
         RESTHandlerConfig restHandlerConfig,
         @Named("changepin.require.principal.check")
         boolean requirePrincipalCheck
   ) {
      super(clientFactory, personDao, authGrantDao, bus, populationCacheMgr, new HttpSender(ChangePinV2RESTHandler.class, metrics), auth, restHandlerConfig, requirePrincipalCheck);
   }

   @Override
   protected String extractPin(ClientMessage msg) {
      return PersonCapability.ChangePinV2Request.getPin(msg.getPayload());
   }

   @Override
   protected String extractPlaceId(ClientMessage msg, Person person) {
      return PersonCapability.ChangePinV2Request.getPlace(msg.getPayload());
   }

   @Override
   protected MessageBody buildResponseBody(boolean success) {
      return PersonCapability.ChangePinV2Response.builder().withSuccess(success).build();
   }
   
   @Override
   protected MessageBody handlePinOperation(Person person, String newPin, String place, List<AuthorizationGrant> grants,
      ClientMessage clientMessage) throws PinNotUniqueAtPlaceException {

      if (!StringUtils.equals(person.getPinAtPlace(place), newPin))
      {
         Set<String> previousPlacesWithPin = person.getPlacesWithPin();
         boolean hadPinAtPlace = person.hasPinAtPlace(place);

         person = personDao.updatePinAtPlace(person, UUID.fromString(place), newPin);

         broadcastValueChange(person, place, grants, hadPinAtPlace, previousPlacesWithPin);
         notify(person, place);
      }

      return buildResponseBody(true);
   }
}

