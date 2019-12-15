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
package com.arcussmarthome.platform.model;

import com.arcussmarthome.messages.MessageConstants;
import com.arcussmarthome.messages.address.AddressMatcher;
import com.arcussmarthome.messages.address.AddressMatchers;

/**
 * Marks the type() as SERVICE and sets up a default matcher based on
 * the assumption that name() is the namespace.
 * @author tweidlin
 *
 */
public abstract class ServiceDispatcherModule extends CapabilityDispatcherModule {
	
	@Override
	protected AddressMatcher matcher() {
		return AddressMatchers.platformService(MessageConstants.SERVICE, name());
	}

	@Override
	public Type type() {
		return Type.SERVICE;
	}
}

