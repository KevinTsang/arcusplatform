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
package com.arcussmarthome.platform.pairing;

import com.arcussmarthome.messages.address.AddressMatchers;
import com.arcussmarthome.messages.capability.PairingDeviceCapability;
import com.arcussmarthome.messages.service.DeviceService;
import com.arcussmarthome.platform.model.ServiceDispatcherModule;
import com.arcussmarthome.platform.pairing.handler.CreateMockRequestHandler;
import com.arcussmarthome.platform.pairing.handler.DeviceAddedListener;
import com.arcussmarthome.platform.pairing.handler.DeviceChangeListener;
import com.arcussmarthome.platform.pairing.handler.DeviceDeletedListener;
import com.arcussmarthome.platform.pairing.handler.DeviceFoundListener;
import com.arcussmarthome.platform.pairing.handler.DeviceRemovedListener;
import com.arcussmarthome.platform.pairing.handler.HubDeletedListener;
import com.arcussmarthome.platform.pairing.handler.PlaceDeletedListener;
import com.netflix.governator.annotations.Modules;

/**
 * @author tweidlin
 *
 */
@Modules(include=PairingDeviceDaoModule.class)
public class PairingDeviceServiceModule extends ServiceDispatcherModule {

	/* (non-Javadoc)
	 * @see com.iris.model.CapabilityDispatcherModule#name()
	 */
	@Override
	protected String name() {
		return PairingDeviceCapability.NAMESPACE;
	}

	@Override
	protected void configure() {
		super.configure();
		
		// request handlers
		annotatedObjects().addBinding().to(CreateMockRequestHandler.class);

		// listeners
		addBroadcastListener(); // FIXME this should really be detected by the fact that there are @OnMessage handlers
		addListener(AddressMatchers.equals(DeviceService.ADDRESS)); // snoop DeviceRemovedEvent
		annotatedObjects().addBinding().to(DeviceFoundListener.class);
		annotatedObjects().addBinding().to(DeviceAddedListener.class);
		annotatedObjects().addBinding().to(DeviceChangeListener.class);
		annotatedObjects().addBinding().to(DeviceDeletedListener.class);
		annotatedObjects().addBinding().to(DeviceRemovedListener.class);
		annotatedObjects().addBinding().to(HubDeletedListener.class);
		annotatedObjects().addBinding().to(PlaceDeletedListener.class);
	}

}

