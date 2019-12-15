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
package com.arcussmarthome.common.scene;

import com.arcussmarthome.common.rule.action.Action;
import com.arcussmarthome.messages.address.Address;

public class SceneImpl implements Scene {
   private final Address address;
   private final SceneContext context;
   private final Action action;

   public SceneImpl(
         Address address,
         SceneContext context,
         Action action
   ) {
      this.address = address;
      this.context = context;
      this.action = action;
   }

   @Override
   public Address getAddress() {
      return address;
   }

   @Override
   public SceneContext getContext() {
      return context;
   }

   // FIXME update actions to have satisfiability
   @Override
   public boolean isSatisfiable() {
//      return action.isSatisfiable(context);
      return action != null;
   }

   @Override
   public void execute() {
      this.action.execute(context);
   }

}

