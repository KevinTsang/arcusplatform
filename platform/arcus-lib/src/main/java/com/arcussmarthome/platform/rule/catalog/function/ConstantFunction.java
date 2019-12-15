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

public class ConstantFunction<I, O> implements Function<I, O>, Serializable {
   private final O value;
   
   public ConstantFunction(O value) {
      this.value = value;
   }

   @Override
   public O apply(I input) {
      return value;
   }

   @Override
   public String toString() {
      return String.valueOf(value);
   }
}

