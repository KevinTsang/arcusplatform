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
package com.arcussmarthome.driver.metadata;

import org.apache.commons.lang3.StringUtils;

import com.arcussmarthome.driver.handler.ContextualEventHandler;


/**
 *
 */
public abstract class EventMatcher {
   // true type information is hidden as the event is passed through the
   // local context rather than as an input parameter
   private ContextualEventHandler<Object> handler;

   public ContextualEventHandler<Object> getHandler() {
      return handler;
   }

   public void setHandler(ContextualEventHandler<Object> handler) {
      this.handler = handler;
   }

   protected static String getStringOrWildcard(String value) {
      return StringUtils.isEmpty(value) ? "*" : value;
   }
}

