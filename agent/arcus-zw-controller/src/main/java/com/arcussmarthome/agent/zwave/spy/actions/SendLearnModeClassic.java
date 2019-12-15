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
package com.arcussmarthome.agent.zwave.spy.actions;

import javax.servlet.http.HttpServletRequest;

import com.arcussmarthome.agent.spy.SpyPlugIn;
import com.arcussmarthome.agent.zwave.ZWServices;
import com.arcussmarthome.agent.zwave.code.entity.CmdNetMgmtBasicLearnModeSet;
import com.arcussmarthome.agent.zwave.spy.ZWSpy;

public class SendLearnModeClassic implements SpyPlugIn {

   @Override
   public Object apply(HttpServletRequest input) {
      ZWServices.INSTANCE.getNetwork().requestLearnModeSet(CmdNetMgmtBasicLearnModeSet.MODE_CLASSIC);
      ZWSpy.INSTANCE.toolUsed("Request controller to enter classic learn mode");
      return "";
   }

   @Override
   public boolean showLink() {
      return false;
   }

   @Override
   public String pageName() {
      return "ziplearnclassic";
   }

   @Override
   public String title() {
      return null;
   }
}


