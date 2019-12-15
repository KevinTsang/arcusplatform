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

import java.io.Serializable;

import com.google.common.base.Function;
import com.arcussmarthome.common.rule.Context;
import com.arcussmarthome.messages.address.Address;
import com.arcussmarthome.type.TypeCoercer;

public class GetAttributeValue<T> implements Function<Context, T>, Serializable {
   private final Address address;
   private final String attribute;
   private final Function<Object, T> transformer;
   
   public GetAttributeValue(Class<T> target, Address address, String attribute, TypeCoercer typeCoercer) {
      this.address = address;
      this.attribute = attribute;
      this.transformer = typeCoercer.createTransformer(target);
   }

   @Override
   public T apply(Context input) {
      return transformer.apply(input.getAttributeValue(address, attribute));
   }

   @Override
   public String toString() {
      return attribute + " from " + address;
   }
}

