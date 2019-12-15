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
package com.arcussmarthome.common.rule.type;

import com.arcussmarthome.common.rule.time.TimeOfDay;
import com.arcussmarthome.type.handler.TypeHandlerImpl;

@SuppressWarnings("serial")
public class TimeOfDayHandler extends TypeHandlerImpl<TimeOfDay> {

   public TimeOfDayHandler() {
      super(TimeOfDay.class, String.class);
   }

   @Override
   protected TimeOfDay convert(Object value) {
      return TimeOfDay.fromString((String) value);
   }

}

