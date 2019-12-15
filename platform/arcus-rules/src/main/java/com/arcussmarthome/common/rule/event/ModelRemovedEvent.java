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
package com.arcussmarthome.common.rule.event;

import com.arcussmarthome.messages.model.Model;

/**
 * Fired *after* a model has been removed from
 * the context.  Attempting to retrieve the
 * associated model from the context will fail.
 */
public class ModelRemovedEvent extends RuleEvent {
   private final Model model;
   
   public static ModelRemovedEvent create(Model model) {
      return new ModelRemovedEvent(model);
   }
   
   private ModelRemovedEvent(Model model) {
      this.model = model;
   }

   @Override
   public RuleEventType getType() {
      return RuleEventType.MODEL_REMOVED;
   }

   /**
    * The model that was removed
    * @return
    */
   public Model getModel() {
      return model;
   }

   @Override
   public String toString() {
      return "ModelRemovedEvent [model=" + model + "]";
   }

   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((model == null) ? 0 : model.hashCode());
      return result;
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj) return true;
      if (obj == null) return false;
      if (getClass() != obj.getClass()) return false;
      ModelRemovedEvent other = (ModelRemovedEvent) obj;
      if (model == null) {
         if (other.model != null) return false;
      }
      else if (!model.equals(other.model)) return false;
      return true;
   }

}

