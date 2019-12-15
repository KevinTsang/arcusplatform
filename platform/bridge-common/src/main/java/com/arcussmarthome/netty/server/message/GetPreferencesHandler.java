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
package com.arcussmarthome.netty.server.message;

import static com.arcussmarthome.messages.MessageBody.emptyMessage;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.arcussmarthome.bridge.server.session.Session;
import com.arcussmarthome.core.dao.PreferencesDAO;
import com.arcussmarthome.messages.ClientMessage;
import com.arcussmarthome.messages.MessageBody;
import com.arcussmarthome.messages.address.Address;
import com.arcussmarthome.messages.service.SessionService;
import com.arcussmarthome.messages.service.SessionService.GetPreferencesRequest;
import com.arcussmarthome.messages.service.SessionService.GetPreferencesResponse;
import com.arcussmarthome.messages.type.CardPreference;
import com.arcussmarthome.messages.type.Preferences;

@Singleton
public class GetPreferencesHandler extends BaseClientRequestHandler
{
   public static final String NAME_EXECUTOR = "executor.getpreferences";

   private DefaultPreferencesConfig config;
   private final PreferencesDAO preferencesDao;

   @Inject
   public GetPreferencesHandler(
      DefaultPreferencesConfig config, 
      PreferencesDAO preferencesDao,
      @Named(NAME_EXECUTOR) Executor executor
   ) {
      super(executor);

      this.config = config; 
      this.preferencesDao = preferencesDao;
   }

   @Override
   protected Address address() {
      return SessionService.ADDRESS;
   }

   @Override
   public String getRequestType() {
      return GetPreferencesRequest.NAME;
   }

   @Override
   protected MessageBody doHandle(ClientMessage request, Session session) {
      UUID personUuid = session.getClient().getPrincipalId();
      String placeId = session.getActivePlace();

      if (StringUtils.isEmpty(placeId)) {
         return emptyMessage();
      }
      else {
         UUID placeUuid = UUID.fromString(placeId);

         Preferences preferences = new Preferences( preferencesDao.findById(personUuid, placeUuid) );
         addDefaultCards(preferences);
         if(preferences.getHideTutorials() == null) {
            preferences.setHideTutorials(config.isHideTutorials());
         }

         return GetPreferencesResponse.builder()
            .withPrefs(preferences.toMap())
            .build();
      }
   }

   private void addDefaultCards(Preferences preferences) {
      Map<String, Boolean> cards = new LinkedHashMap<>();
      if(preferences.getDashboardCards() != null) {
         preferences
            .getDashboardCards()
            .stream()
            .map(CardPreference::new)
            .forEach((p) -> cards.put(p.getServiceName(), Optional.ofNullable(p.getHideCard()).orElse(config.isDashboardCardsHidden())));
      }
      boolean changes = false;
      for(String serviceName: config.getDashboardCardOrderList()) {
         changes |= (cards.putIfAbsent(serviceName, config.isDashboardCardsHidden()) == null);
      }
      if(changes) {
         preferences.setDashboardCards(cards.entrySet().stream().map(this::toCardPreference).collect(Collectors.toList()));
      }
   }
   
   private Map<String, Object> toCardPreference(Map.Entry<String, Boolean> e) {
      return ImmutableMap.of(CardPreference.ATTR_SERVICENAME, e.getKey(), CardPreference.ATTR_HIDECARD, e.getValue());
   }

}

