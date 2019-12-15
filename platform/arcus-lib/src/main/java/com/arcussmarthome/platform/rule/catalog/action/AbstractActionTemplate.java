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
package com.arcussmarthome.platform.rule.catalog.action;

import java.util.Map;
import java.util.Set;

import com.google.common.base.Function;
import com.arcussmarthome.common.rule.action.ActionContext;
import com.arcussmarthome.platform.rule.catalog.ActionTemplate;
import com.arcussmarthome.platform.rule.catalog.function.FunctionFactory;
import com.arcussmarthome.platform.rule.catalog.template.TemplatedValue;

public abstract class AbstractActionTemplate implements ActionTemplate {
   private final Set<String> contextVariables;
   
   protected AbstractActionTemplate(Set<String> contextVariables) {
      this.contextVariables = contextVariables;
   }
   
   protected Set<String> getContextVariables() {
      return contextVariables;
   }
   
   // If the templated value resolves with variables available in the context, then resolve it with the ActionContext later.
   // If the templated value resolves with variables available now, then turn it into a function that returns a constant
   public <O> Function<ActionContext, O> generateContextFunction(final TemplatedValue<O> value, Map<String, Object> variables) {
      if (value.hasContextVariables(contextVariables)) {
         return FunctionFactory.INSTANCE.createGetTemplatedValueFromActionContext(value);
      }
      else {
         O resolvedValue = value.apply(variables);
         return FunctionFactory.INSTANCE.createConstant(ActionContext.class, resolvedValue);
      }
   }

}

