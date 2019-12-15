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
package com.arcussmarthome.client.server.rest;

import com.arcussmarthome.messages.errors.ErrorEventException;
import com.arcussmarthome.messages.model.Person;

public class PinErrors {
	public static final String PERSON_NOT_FOUND_CODE = "person.notFound";
	public static final String PERSON_NOT_FOUND_MSG = "Unable to locate record for person";

	public static final String PIN_NOT_UNIQUE_AT_PLACE_CODE = "pin.notUniqueAtPlace";
	public static final String PIN_NOT_UNIQUE_AT_PLACE_MSG = "Pin must be unique at this place";

	public static void assertPersonFound(Person person) {
		if(person == null) {
			throw new ErrorEventException(PERSON_NOT_FOUND_CODE, PERSON_NOT_FOUND_MSG);
		}
	}

}

