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
package com.arcussmarthome.capability.definition;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class ParameterizedDefinition extends Definition {
   protected final List<ParameterDefinition> parameters;
   
   protected ParameterizedDefinition(String name, String description, List<ParameterDefinition> parameters) {
      super(name, description);
      this.parameters = Collections.unmodifiableList(parameters);
   }
   
   public List<ParameterDefinition> getParameters() {
      return new ArrayList<ParameterDefinition>(parameters);
   }

}

