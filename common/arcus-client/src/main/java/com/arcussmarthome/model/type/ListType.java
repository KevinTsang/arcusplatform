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
package com.arcussmarthome.model.type;

import java.lang.reflect.Array;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ListType implements CollectionType {

   private final AttributeType containedType;
   private final String typeName;

   public ListType(AttributeType containedType) {
      this.containedType = containedType;
      this.typeName = "list<" + containedType.getTypeName() + ">";
   }

   @SuppressWarnings("rawtypes")
   @Override
   public Class<List> getJavaType() {
      return List.class;
   }

   @Override
   public AttributeType getContainedType() {
      return containedType;
   }

   @Override
   public String getTypeName() {
      return this.typeName;
   }

   @SuppressWarnings({ "rawtypes", "unchecked" })
   @Override
   public List coerce(Object obj) {
      if(obj == null) {
         return null;
      }

      List list = new ArrayList();

      if(obj instanceof Iterable) {
         Iterable iter = (Iterable) obj;
         for(Object o : iter) {
            list.add(containedType.coerce(o));
         }
      }
      else if(obj.getClass().isArray()) {
         for(int i = 0; i < Array.getLength(obj); i++) {
            list.add(containedType.coerce(Array.get(obj, i)));
         }
      }
      else {
         throw new IllegalArgumentException("Cannot coerce object of type " + obj.getClass() + " to " + getTypeName());
      }

      return list;
   }

   @Override
   public boolean isAssignableFrom(Type type) {
      if(type == null) {
         return false;
      }
      if(type instanceof Class) {
         return Iterable.class.isAssignableFrom((Class<?>) type);
      }
      if(type instanceof ParameterizedType) {
         ParameterizedType pType = (ParameterizedType) type;
         return 
               Iterable.class.isAssignableFrom((Class<?>) pType.getRawType()) &&
               pType.getActualTypeArguments().length == 1 &&
               containedType.isAssignableFrom(pType.getActualTypeArguments()[0]);
      }
      return false;
   }

   @Override
   public String toString() {
      return "list<" + containedType + ">";
   }

   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result
            + ((containedType == null) ? 0 : containedType.hashCode());
      result = prime * result + ((typeName == null) ? 0 : typeName.hashCode());
      return result;
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (obj == null)
         return false;
      if (getClass() != obj.getClass())
         return false;
      ListType other = (ListType) obj;
      if (containedType == null) {
         if (other.containedType != null)
            return false;
      } else if (!containedType.equals(other.containedType))
         return false;
      if (typeName == null) {
         if (other.typeName != null)
            return false;
      } else if (!typeName.equals(other.typeName))
         return false;
      return true;
   }
}

