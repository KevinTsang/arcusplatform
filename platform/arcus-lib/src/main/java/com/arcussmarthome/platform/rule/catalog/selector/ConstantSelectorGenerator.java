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

import com.arcussmarthome.common.rule.RuleContext;

/**
 * 
 */
public class ConstantSelectorGenerator implements SelectorGenerator {
   private Selector selector;

   /**
    * 
    */
   public ConstantSelectorGenerator(SelectorType type) {
      this(new SimpleSelector(type));
   }

   public ConstantSelectorGenerator(Selector selector) {
      this.selector = selector;
   }

   /* (non-Javadoc)
    * @see com.iris.platform.rule.catalog.selector.SelectorGenerator#isSatisfiable(com.iris.common.rule.RuleContext)
    */
   @Override
   public boolean isSatisfiable(RuleContext context) {
      return true;
   }

   /* (non-Javadoc)
    * @see com.iris.platform.rule.catalog.selector.SelectorGenerator#generate(com.iris.common.rule.RuleContext)
    */
   @Override
   public Selector generate(RuleContext environment) {
      return selector;
   }

}

