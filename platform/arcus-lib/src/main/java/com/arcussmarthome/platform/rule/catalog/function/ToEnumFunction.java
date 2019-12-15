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
package com.arcussmarthome.platform.rule.catalog.function;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Function;

public class ToEnumFunction<E extends Enum<E>> implements Function<String, E> {
   private final Class<E> type;
   
   public ToEnumFunction(Class<E> type) {
      this.type = type;
   }

   @Override
   public E apply(String value) {
      if(StringUtils.isEmpty(value)) {
         return null;
      }
      return Enum.valueOf(type, value);
   }
}

