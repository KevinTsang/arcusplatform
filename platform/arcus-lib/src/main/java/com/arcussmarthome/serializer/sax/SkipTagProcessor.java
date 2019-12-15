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
package com.arcussmarthome.serializer.sax;

import org.xml.sax.Attributes;

import com.arcussmarthome.platform.rule.catalog.serializer.BaseCatalogProcessor;
import com.arcussmarthome.validators.Validator;


/**
 * 
 */
public class SkipTagProcessor extends BaseCatalogProcessor {

   public SkipTagProcessor(Validator v) {
      super(v);
   }

   /* (non-Javadoc)
    * @see com.iris.platform.rule.catalog.serializer.SAXTagHandler#getHandler(java.lang.String, com.iris.capability.attribute.Attributes)
    */
   @Override
   public TagProcessor getHandler(String qName, Attributes attributes) {
      return this;
   }

}

