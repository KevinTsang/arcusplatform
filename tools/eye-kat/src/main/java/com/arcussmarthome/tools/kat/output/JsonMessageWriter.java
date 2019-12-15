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
/**
 * 
 */
package com.arcussmarthome.tools.kat.output;

import java.io.IOException;
import java.io.Writer;

import com.arcussmarthome.io.json.JSON;
import com.arcussmarthome.tools.kat.message.Entry;

/**
 * 
 */
public class JsonMessageWriter implements MessageWriter {
   private final Writer writer;
   
   /**
    * 
    */
   public JsonMessageWriter(Writer writer) {
      this.writer = writer;
   }

   @Override
   public void write(Entry entry) throws IOException {
      JSON.toJson(entry.getPayload(), writer);
      writer.write("\n");
      writer.flush();
   }

   @Override
   public void close() {
      try {
         writer.close();
      }
      catch(IOException e) {
         // ignore
      }
   }

}

