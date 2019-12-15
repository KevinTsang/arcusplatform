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
package com.arcussmarthome.platform.services.productcatalog.handlers;

import com.arcussmarthome.core.dao.PopulationDAO;
import com.arcussmarthome.core.platform.ContextualRequestMessageHandler;
import com.arcussmarthome.messages.MessageBody;
import com.arcussmarthome.messages.PlatformMessage;
import com.arcussmarthome.messages.type.Population;
import com.arcussmarthome.prodcat.ProductCatalog;
import com.arcussmarthome.prodcat.ProductCatalogManager;

public abstract class AbstractProductCatalogHandler implements ContextualRequestMessageHandler<ProductCatalog> {
   private final PopulationDAO populationDao;
   private final ProductCatalogManager manager;

   public AbstractProductCatalogHandler(PopulationDAO populationDao, ProductCatalogManager manager) {
      this.populationDao = populationDao;
      this.manager = manager;
   }

   @Override
   public MessageBody handleStaticRequest(PlatformMessage msg) {
      Population population = populationDao.getDefaultPopulation();
      if (population == null) {
         throw new RuntimeException("No default population defined.");
      }
      return handleRequest(manager.getCatalog(population.getName()), msg);
   }

   public ProductCatalogManager getManager() {
      return this.manager;
   }
}

