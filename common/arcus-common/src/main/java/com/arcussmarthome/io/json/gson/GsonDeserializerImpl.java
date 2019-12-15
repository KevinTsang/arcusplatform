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
package com.arcussmarthome.io.json.gson;

import java.io.IOException;
import java.io.Reader;

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.arcussmarthome.gson.GsonFactory;
import com.arcussmarthome.io.json.JsonDeserializer;
import com.arcussmarthome.util.TypeMarker;

@Singleton
public class GsonDeserializerImpl implements JsonDeserializer {

   private final Gson gson;

   @Inject
   public GsonDeserializerImpl(GsonFactory gsonFactory) {
      this.gson = gsonFactory.get();
   }

   @Override
   public <T> T fromJson(String json, Class<T> clazz) {
      return gson.fromJson(json, clazz);
   }

	@Override
   public <T> T fromJson(Reader json, Class<T> clazz) throws IOException {
	   return gson.fromJson(json, clazz);
   }

   @SuppressWarnings("unchecked")
   @Override
   public <T> T fromJson(String json, TypeMarker<T> type) {
      return (T) gson.fromJson(json, type.getType());
   }

   @SuppressWarnings("unchecked")
   @Override
   public <T> T fromJson(Reader json, TypeMarker<T> type) throws IOException {
      return (T) gson.fromJson(json, type.getType());
   }

}

