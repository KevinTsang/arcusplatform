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
package com.arcussmarthome.client.impl.json;

import java.io.IOException;
import java.util.Date;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

public class DateTypeAdapter extends TypeAdapter<Date> {

   @Override
   public void write(JsonWriter out, Date value) throws IOException {
   	if(value == null) {
   		out.nullValue();
   	}
   	else {
   		out.value(value.getTime());
   	}
   }

   @Override
   public Date read(JsonReader in) throws IOException {
   	if(JsonToken.NULL.equals(in.peek())) {
   		in.nextNull();
   		return null;
   	}
   	else {
   	   try {
   	      return new Date(in.nextLong());
   	   }
   	   catch(NumberFormatException e) {
   	      return new Date((long) in.nextDouble());
   	   }
   	}
   }
}

