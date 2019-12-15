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
package com.arcussmarthome.core.template;

import java.util.HashMap;
import java.util.Map;

import com.github.jknack.handlebars.Helper;
import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.arcussmarthome.bootstrap.guice.AbstractIrisModule;

public class TemplateModule extends AbstractIrisModule {
		
	@Inject(optional=true) @Named("template.path") String templatePath = "templates";
	@Inject(optional=true) @Named("template.cacheSize") int cacheSize = 5000;
	@Inject(optional=true) Map<String,Helper<? extends Object>> helpers = new HashMap<String,Helper<? extends Object>>();

	@Override
	protected void configure() {
		
	}
		
	@Provides @Singleton TemplateService templateService(){
		HandlebarsTemplateService hbTemplateService = new HandlebarsTemplateService(templatePath, cacheSize);
		if(helpers!=null){
			hbTemplateService.registerHelpers(helpers);
		}
		return hbTemplateService;
	}

}

