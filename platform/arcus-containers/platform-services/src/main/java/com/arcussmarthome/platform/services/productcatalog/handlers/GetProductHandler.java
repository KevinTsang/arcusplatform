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

import com.google.inject.Inject;
import com.arcussmarthome.Utils;
import com.arcussmarthome.capability.attribute.transform.BeanAttributesTransformer;
import com.arcussmarthome.core.dao.PopulationDAO;
import com.arcussmarthome.messages.MessageBody;
import com.arcussmarthome.messages.PlatformMessage;
import com.arcussmarthome.messages.capability.ProductCatalogCapability;
import com.arcussmarthome.prodcat.ProductCatalog;
import com.arcussmarthome.prodcat.ProductCatalogEntry;
import com.arcussmarthome.prodcat.ProductCatalogManager;

public class GetProductHandler extends AbstractProductCatalogHandler {
   private final BeanAttributesTransformer<ProductCatalogEntry> transformer;

   @Inject
   public GetProductHandler(BeanAttributesTransformer<ProductCatalogEntry> transformer,
         PopulationDAO populationDao,
         ProductCatalogManager manager) {
      super(populationDao, manager);
      this.transformer = transformer;
   }

   @Override
   public String getMessageType() {
      return ProductCatalogCapability.GetProductRequest.NAME;
   }

   @Override
   public MessageBody handleRequest(ProductCatalog context, PlatformMessage msg) {
      Utils.assertNotNull(context, "The product catalog is required");
      MessageBody request = msg.getValue();
      String id = ProductCatalogCapability.GetProductRequest.getId(request);
      ProductCatalogEntry entry = context.getProductById(id);
      if (entry == null) {
         throw new RuntimeException("No product could be found for id: " + id);
      }
      return ProductCatalogCapability.GetProductResponse.builder()
               .withProduct(transformer.transform(entry))
               .build();
   }

}

