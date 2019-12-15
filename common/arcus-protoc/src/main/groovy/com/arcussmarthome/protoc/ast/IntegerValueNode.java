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
package com.arcussmarthome.protoc.ast;

import java.math.BigInteger;

public class IntegerValueNode extends ValueNode {
   private final BigInteger value;
   private final TypeNode type;

   public IntegerValueNode(BigInteger value, TypeNode type) {
      this.value = value;
      this.type = type;
   }

   @Override
   public String getValue() {
      return value.toString();
   }

   @Override
   public TypeNode getType(Aliasing resolver) {
      return resolver.resolve(type);
   }

   @Override
   public String toString() {
      return "" + type + ":" + value;
   }
}

