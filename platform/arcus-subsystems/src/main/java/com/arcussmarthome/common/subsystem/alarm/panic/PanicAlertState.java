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
package com.arcussmarthome.common.subsystem.alarm.panic;

import com.arcussmarthome.common.subsystem.SubsystemContext;
import com.arcussmarthome.common.subsystem.alarm.RecordOnSecurityAdapter;
import com.arcussmarthome.common.subsystem.alarm.generic.AlertState;
import com.arcussmarthome.messages.model.subs.AlarmSubsystemModel;
import com.arcussmarthome.messages.model.subs.SubsystemModel;

public class PanicAlertState extends AlertState {

   private static final PanicAlertState INSTANCE = new PanicAlertState();

   public static PanicAlertState instance() {
      return INSTANCE;
   }

   private PanicAlertState() {
   }


   @Override
   public String onEnter(SubsystemContext<? extends SubsystemModel> context, String name)
   {
      if(context.model() instanceof AlarmSubsystemModel) {
         RecordOnSecurityAdapter adapter = new RecordOnSecurityAdapter((SubsystemContext<? extends AlarmSubsystemModel>) context);
         adapter.sendRecordMessageIfNecessary();
      }
      return super.onEnter(context, name);      
      
   }   
   
   
   
}

