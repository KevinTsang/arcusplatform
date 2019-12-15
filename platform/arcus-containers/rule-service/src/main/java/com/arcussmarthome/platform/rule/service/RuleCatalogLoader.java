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
package com.arcussmarthome.platform.rule.service;

import java.util.UUID;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.arcussmarthome.platform.rule.catalog.RuleCatalog;
import com.arcussmarthome.platform.rule.catalog.RuleCatalogManager;
import com.arcussmarthome.population.PlacePopulationCacheManager;

/**
 * 
 */
@Singleton
public class RuleCatalogLoader {
   private final RuleCatalogManager manager;
   private final PlacePopulationCacheManager populationCacheMgr;

   @Inject
   public RuleCatalogLoader(
         RuleCatalogManager manager,
         PlacePopulationCacheManager populationCacheMgr
   ) {
      this.manager = manager;
      this.populationCacheMgr = populationCacheMgr;
   }

   public RuleCatalog getCatalogForPlace(UUID placeId) {
      String population = populationCacheMgr.getPopulationByPlaceId(placeId);
      return getCatalogForPopulation(population);
   }

   private RuleCatalog getCatalogForPopulation(String population) {
       return manager.getCatalog(population);
   }

}

