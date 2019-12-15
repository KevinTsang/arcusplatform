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
package com.arcussmarthome.oculus.menu;

import javax.inject.Inject;
import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import com.arcussmarthome.oculus.modules.log.EventLogController;
import com.arcussmarthome.oculus.util.Actions;
import com.arcussmarthome.oculus.util.BaseComponentWrapper;

/**
 * 
 */
public class WindowsMenu extends BaseComponentWrapper<JMenu> {
   private String label = "Windows";
   
   private Action showEventLog;
   
   @Inject
   public WindowsMenu(EventLogController events) {
      this.showEventLog = Actions.build("Message Log", events, EventLogController::showEventLogs);
   }
   
   
   @Override
   protected JMenu createComponent() {
      JMenu menu = new JMenu(label);
      menu.add(new JMenuItem(showEventLog));
      return menu;
   }

}

