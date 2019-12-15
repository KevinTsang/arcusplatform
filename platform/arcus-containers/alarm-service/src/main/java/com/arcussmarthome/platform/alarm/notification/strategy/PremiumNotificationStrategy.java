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
package com.arcussmarthome.platform.alarm.notification.strategy;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.arcussmarthome.common.alarm.AlertType;
import com.arcussmarthome.messages.address.Address;
import com.arcussmarthome.platform.alarm.history.ModelLoader;
import com.arcussmarthome.platform.alarm.incident.Trigger;
import com.arcussmarthome.platform.alarm.notification.calltree.CallTreeContext;
import com.arcussmarthome.platform.alarm.notification.calltree.CallTreeDAO;
import com.arcussmarthome.platform.alarm.notification.calltree.CallTreeExecutor;

@Singleton
public class PremiumNotificationStrategy extends BaseNotificationStrategy {

	
   @Inject
   public PremiumNotificationStrategy(
      NotificationStrategyConfig config,
      CallTreeExecutor callTreeExecutor,
      CallTreeDAO callTreeDao,
      ModelLoader modelLoader
   ) {
      super(config, callTreeExecutor, callTreeDao, modelLoader);
   }

   @Override
   protected void doNotify(CallTreeContext.Builder contextBuilder, Trigger trigger) {
      switch(trigger.getAlarm()) {
         case CO:
         case SMOKE:
         case WATER:
            getCallTreeExecutor().notifyParallel(contextBuilder.build());
            break;
         case SECURITY:
         case PANIC:
            getCallTreeExecutor().startSequential(contextBuilder.build());
            break;
      }
   }

   @Override
   public boolean doCancel(Address incidentAddress) {
      stopSequential(incidentAddress, AlertType.SECURITY);
      stopSequential(incidentAddress, AlertType.PANIC);
      return true;
   }

   @Override
   public void acknowledge(Address incidentAddress, AlertType type) {
      switch(type) {
         case SECURITY:
         case PANIC:
            stopSequential(incidentAddress, type);
            break;
         default:
            break;
      }
   }

   private void stopSequential(Address incidentAdress, AlertType type) {
      String msgKey = NotificationConstants.SECURITY_KEY;
      if(type == AlertType.PANIC) {
         msgKey = NotificationConstants.PANIC_KEY;
      }

      getCallTreeExecutor().stopSequential(incidentAdress, msgKey);
   }
}

