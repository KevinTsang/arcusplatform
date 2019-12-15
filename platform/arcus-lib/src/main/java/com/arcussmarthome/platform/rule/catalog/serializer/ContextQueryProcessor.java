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
package com.arcussmarthome.platform.rule.catalog.serializer;

import org.xml.sax.Attributes;

import com.arcussmarthome.platform.rule.catalog.condition.config.ConditionConfig;
import com.arcussmarthome.platform.rule.catalog.condition.config.ContextQueryConfig;
import com.arcussmarthome.validators.Validator;

public class ContextQueryProcessor extends ConditionsContainerProcessor {

	public static final String TAG = "context-query";
	public static final String ATTR_QUERY = "query";
	public static final String ATTR_SATISFIABLE = "satisfiable-if";

	ContextQueryConfig condition;
	
	protected ContextQueryProcessor(Validator validator) {
		super(validator);
	}
	
	@Override
	public boolean isFilter() {
		return true;
	}

	@Override
	public ConditionConfig getCondition() {
		return condition;
	}
	
   @Override
   protected void setCondition(ConditionConfig condition) {
      if (this.condition.getCondition() != null) {
         validator.error(TAG + " may only contain a single condition");
      }
      this.condition.setCondition(condition);
   }

   @Override
	public void enterTag(String qName, Attributes attributes) {
		if (TAG.equals(qName)) {
			condition = new ContextQueryConfig();
			condition.setSelectorExpression(super.getTemplatedExpression(ATTR_SATISFIABLE, null, attributes));
			condition.setMatcherExpression(super.getTemplatedExpression(ATTR_QUERY, attributes));
		}
		super.enterTag(qName, attributes);
	}

}

