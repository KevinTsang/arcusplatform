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
package com.arcussmarthome.capability.attribute.transform;

import java.util.HashMap;
import java.util.Map;

import com.arcussmarthome.device.attributes.AttributeMap;

public abstract class AbstractAttributeMapTransformer implements AttributeMapTransformer {

   @Override
   public Map<String, Object> transformFromAttributeMap(AttributeMap attributes) {
      if(attributes == null) {
         return null;
      }

      Map<String, Object> attributeMap = new HashMap<>();
      attributes.entries().forEach((attrValue) -> {
         attributeMap.put(attrValue.getKey().getName(), attrValue.getValue());
      });

      return attributeMap;
   }

}

