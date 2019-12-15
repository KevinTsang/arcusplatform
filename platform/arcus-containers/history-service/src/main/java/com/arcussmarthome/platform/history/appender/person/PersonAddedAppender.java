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
package com.arcussmarthome.platform.history.appender.person;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.arcussmarthome.messages.MessageBody;
import com.arcussmarthome.messages.PlatformMessage;
import com.arcussmarthome.messages.capability.Capability;
import com.arcussmarthome.messages.capability.PersonCapability;
import com.arcussmarthome.platform.history.HistoryAppenderDAO;
import com.arcussmarthome.platform.history.HistoryLogEntry;
import com.arcussmarthome.platform.history.appender.MessageContext;
import com.arcussmarthome.platform.history.appender.ObjectNameCache;
import com.arcussmarthome.platform.history.appender.annotation.Event;
import com.arcussmarthome.platform.history.appender.annotation.Group;
import com.arcussmarthome.platform.history.appender.matcher.MatchResults;

@Singleton
@Group(PersonCapability.NAMESPACE)
@Event(event = Capability.EVENT_ADDED)
public class PersonAddedAppender extends AnnotatedPersonAppender {
	private static final Logger logger = LoggerFactory.getLogger(PersonAddedAppender.class);
	
	private static final String KEY_HOBBIT_ADDED  = "person.hobbit.added";
	private static final String KEY_FULLACCESS_ADDED  = "person.fullaccess.added";
	
	@Inject
	public PersonAddedAppender(HistoryAppenderDAO appender, ObjectNameCache cache) {
		super(appender, cache);
	}	

	@Override
	protected List<HistoryLogEntry> translate(PlatformMessage message, MessageContext context, MatchResults matchResults) {
		boolean isFullAccess = PersonCapability.getHasLogin(message.getValue(), Boolean.FALSE);
		String msgKey = KEY_HOBBIT_ADDED;
		if(isFullAccess) {
			msgKey =  KEY_FULLACCESS_ADDED;
		}
		// {0} = person name  {1} = place name	{2} = inviter name
		MessageBody msgBody = message.getValue();
		String personName = getPersonName(PersonCapability.getFirstName(msgBody), PersonCapability.getLastName(msgBody));	
		String placeName = getPlaceNameFromHeader(message);
		String inviterName = getActorName(message);
		
		return creteLogEntries(message, context, msgKey, new String[] {personName, placeName, inviterName});
		
	}	

}

