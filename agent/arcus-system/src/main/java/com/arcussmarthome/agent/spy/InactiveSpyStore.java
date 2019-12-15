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
package com.arcussmarthome.agent.spy;

import java.util.stream.Stream;

import com.arcussmarthome.messages.PlatformMessage;

/**
 * An inactive implementation of the SpyStore interface that does 
 * nothing.
 * 
 * @author Erik Larson
 *
 */
public class InactiveSpyStore implements SpyStore {

	@Override
	public void storeIncomingPlatformMsg(PlatformMessage msg) {
		// No-op
		
	}

	@Override
	public void storeOutgoingPlatformMsg(PlatformMessage msg) {
		// No-op
	}

	@Override
	public Stream<PlatformMessage> streamIncomingPlatformMsgs() {
		return Stream.empty();
	}

	@Override
	public Stream<PlatformMessage> streamOutgoingPlatformMsgs() {
		return Stream.empty();
	}

}

