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

import java.io.Serializable;
import java.lang.reflect.Type;

import org.apache.commons.lang3.reflect.TypeUtils;

public class EnumHandler implements Serializable {
   
   public boolean isSupportedType(Class<?> clazz, Type type) {
      if (type == null) {
         return false;
      }
      return TypeUtils.isAssignable(type, clazz) || String.class.equals(type);
   }
   
   public boolean isCoercible(Class<?> clazz, Object value) {
      if (clazz.isInstance(value)) {
         return true;
      }
      return value instanceof String;
   }
   
   @SuppressWarnings("unchecked")
   public <T> T coerce(Class<T> clazz, Object value) {
      if (clazz.isInstance(value)) {
         return (T)value;
      }
      if (value instanceof String) {
         return fromStringUnbound(clazz, (String)value);
      }
      throw new IllegalArgumentException("Object of class " + value.getClass().getName() + " cannot be coerced to " + clazz.getName());
   }
   
   public static <T> T fromStringUnbound(Class<T> c, String s) {
      if (s == null) {
         return null;
      }         
      for (T enumValue : c.getEnumConstants()) {
         if (((Enum<?>)enumValue).name().equalsIgnoreCase(s)) {
            return enumValue;
         }
      }
      return null;      
   }
}

