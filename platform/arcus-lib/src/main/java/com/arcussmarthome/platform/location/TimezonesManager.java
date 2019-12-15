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
package com.arcussmarthome.platform.location;

import static com.arcussmarthome.platform.location.TimezonesModule.TIMEZONE_RESOURCE;
import static java.util.Collections.unmodifiableMap;
import static java.util.stream.Collectors.toMap;

import java.util.List;
import java.util.Map;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.arcussmarthome.messages.type.TimeZone;
import com.arcussmarthome.resource.Resource;
import com.arcussmarthome.resource.manager.BaseJsonParser;
import com.arcussmarthome.resource.manager.DefaultSingleFileResourceManager;
import com.arcussmarthome.util.TypeMarker;

@Singleton
public class TimezonesManager extends DefaultSingleFileResourceManager<List<Map<String, Object>>>
{
   private volatile Map<String, TimeZone> timeZonesById;

   @Inject
   public TimezonesManager(
      @Named(TIMEZONE_RESOURCE)
      Resource managedResource)
   {
      super(
         managedResource,
         new BaseJsonParser<List<Map<String, Object>>>()
         {
            @Override
            protected TypeMarker<List<Map<String, Object>>> getTypeMarker()
            {
               return new TypeMarker<List<Map<String, Object>>>() { };
            }
         });
   }

   @Override
   protected void loadCache()
   {
      super.loadCache();

      this.timeZonesById = unmodifiableMap(
         getCachedData().stream().collect(toMap(m -> (String) m.get(TimeZone.ATTR_ID), m -> new TimeZone(m))));
   }

   public TimeZone getTimeZoneById(String tzId)
   {
      return timeZonesById.get(tzId);
   }
}

