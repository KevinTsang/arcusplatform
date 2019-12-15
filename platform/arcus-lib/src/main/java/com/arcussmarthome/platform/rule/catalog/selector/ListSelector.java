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
package com.arcussmarthome.platform.rule.catalog.selector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 
 */
public class ListSelector implements Selector {
   public static final String TYPE = "selector";
   private List<Option> options = new ArrayList<>();

   public ListSelector() {
      
   }
   
   @Override
   public SelectorType getType() {
      return SelectorType.LIST;
   }
   
   public List<Option> getOptions() {
      return options;
   }
   
   public void setOptions(Collection<Option> options) {
      if(options == null) {
         this.options = new ArrayList<>();
      }
      else {
         this.options = new ArrayList<>(options);
      }
   }
}

