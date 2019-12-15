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
package com.arcussmarthome.platform.history.appender;

import java.util.List;

import com.arcussmarthome.messages.PlatformMessage;
import com.arcussmarthome.platform.history.HistoryAppenderDAO;
import com.arcussmarthome.platform.history.HistoryLogEntry;
import com.arcussmarthome.platform.history.appender.matcher.MatchResults;

public abstract class AnnotatedHubValueChangeAppender extends AnnotatedAppender {
	
	protected AnnotatedHubValueChangeAppender(HistoryAppenderDAO appender, ObjectNameCache cache) {
   	super(appender, cache);
   }

	@Override
   protected List<HistoryLogEntry> translate(PlatformMessage message, MessageContext context, MatchResults matchResults) {
		context.setHubId(getHubIdFromSource(message));
		context.setHubName(getHubNameFromSource(message));
		return super.translate(message, context, matchResults);
   }
}

