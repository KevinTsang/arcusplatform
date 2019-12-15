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
package com.arcussmarthome.platform.rule.catalog.condition;

import java.util.Map;

import com.google.common.base.Preconditions;
import com.arcussmarthome.common.rule.condition.Condition;
import com.arcussmarthome.common.rule.time.TimeOfDay;
import com.arcussmarthome.common.rule.trigger.TimeOfDayTrigger;
import com.arcussmarthome.platform.rule.catalog.template.TemplatedValue;

/**
 * 
 */
public class TimeOfDayTemplate extends TriggerTemplate {
   private TemplatedValue<TimeOfDay> time;

   /**
    * @return the time
    */
   public TemplatedValue<TimeOfDay> getTime() {
      return time;
   }

   /**
    * @param time the time to set
    */
   public void setTime(TemplatedValue<TimeOfDay> time) {
      this.time = time;
   }

   /* (non-Javadoc)
    * @see com.iris.platform.rule.catalog.ConditionTemplate#generate(java.util.Map)
    */
   @Override
   public Condition generate(Map<String, Object> values) {
      Preconditions.checkState(time != null, "must specify time");
      return new TimeOfDayTrigger(time.apply(values));
   }

   @Override
   public String toString() {
	   return "TimeOfDayTemplate [time=" + time + "]";
   }

}

