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

import java.util.HashMap;
import java.util.Map;

import com.google.inject.Inject;
import com.arcussmarthome.core.dao.PopulationDAO;
import com.arcussmarthome.messages.MessageBody;
import com.arcussmarthome.messages.PlatformMessage;
import com.arcussmarthome.messages.capability.ProductCatalogCapability;
import com.arcussmarthome.prodcat.ProductCatalog;
import com.arcussmarthome.prodcat.ProductCatalogManager;

public class GetProductCatalogHandler extends AbstractProductCatalogHandler {

   @Inject
   public GetProductCatalogHandler(PopulationDAO populationDao, ProductCatalogManager manager) {
      super(populationDao, manager);
   }

	@Override
	public String getMessageType() {
		return ProductCatalogCapability.GetProductCatalogRequest.NAME;
	}

	@Override
   public MessageBody handleRequest(ProductCatalog context, PlatformMessage msg) {
      Map<String, Object> response = new HashMap<>();
      response.put(ProductCatalogCapability.ATTR_FILENAMEVERSION, getManager().getProductCatalogVersion());
      response.put(ProductCatalogCapability.ATTR_PUBLISHER, context.getMetadata().getPublisher());
      response.put(ProductCatalogCapability.ATTR_VERSION, context.getMetadata().getVersion());
      response.put(ProductCatalogCapability.ATTR_BRANDCOUNT, context.getBrandCount());
      response.put(ProductCatalogCapability.ATTR_CATEGORYCOUNT, context.getCategoryCount());
      response.put(ProductCatalogCapability.ATTR_PRODUCTCOUNT, context.getProductCount());
      return ProductCatalogCapability.GetProductCatalogResponse.builder().withCatalog(response).build();
   }

}

