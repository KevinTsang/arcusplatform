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
package com.arcussmarthome.platform.ivr.pronounce;

import com.arcussmarthome.platform.location.UspsDataService;

public class StatePronouncer
{
   private final UspsDataService uspsDataService;

   public StatePronouncer(UspsDataService uspsDataService)
   {
      this.uspsDataService = uspsDataService;
   }

   public String pronounce(String state)
   {
      if (state == null) return state;

      String normalizedState = state.trim().toUpperCase();

      String pronouncedState = uspsDataService.getStates().get(normalizedState);

      if (pronouncedState == null)
      {
         return state;
      }
      else
      {
         return pronouncedState;
      }
   }
}

