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
package com.arcussmarthome.type.handler;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class IntegerHandler extends TypeHandlerImpl<Integer> {

   @Inject
   public IntegerHandler() {
      super(Integer.class, Number.class, String.class);
   }

   @Override
   protected Integer convert(Object value) {   
      if(value instanceof Number) {
         double dbl = ((Number) value).doubleValue();
         if(dbl % 1 == 0 && dbl >= Integer.MIN_VALUE && dbl <= Integer.MAX_VALUE) {
            return (int) dbl;
         }
         throw new IllegalArgumentException("Numerical value " + value + " could not be coerced to " + targetType.getName() + " without data loss");
      } else {
         return Integer.parseInt((String) value);
      }
   }

}

