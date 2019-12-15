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
package com.arcussmarthome.driver.unit.cucumber;

import com.arcussmarthome.capability.registry.CapabilityRegistry;
import com.arcussmarthome.core.messaging.memory.InMemoryPlatformMessageBus;
import com.arcussmarthome.core.messaging.memory.InMemoryProtocolMessageBus;
import com.arcussmarthome.driver.DeviceDriver;
import com.arcussmarthome.driver.DeviceDriverContext;
import com.arcussmarthome.driver.service.executor.DriverExecutor;
import com.arcussmarthome.messages.address.Address;
import com.arcussmarthome.messages.address.ClientAddress;
import com.arcussmarthome.protocol.Protocol;
import com.arcussmarthome.protocol.ProtocolMessage;

public interface DriverTestContext<ProtocolMessageFormat> {

    public Object getDevice();
    public Address getProtocolAddress();
    public Address getDriverAddress();
    public Protocol<ProtocolMessageFormat> getProtocol();
    public DeviceDriverContext getDeviceDriverContext();
    public DeviceDriver getDeviceDriver();
    public ClientAddress getClientAddress();
    public InMemoryPlatformMessageBus getPlatformBus();
    public InMemoryProtocolMessageBus getProtocolBus();
    public CapabilityRegistry getCapabilityRegistry();
    public DriverExecutor getDriverExecutor();

    public void tearDown() throws Exception;
    public void setUp() throws Exception;

    public void initializeDriver(String driverScriptResource) throws Exception;
    
    public void checkTimeoutSeconds(ProtocolMessage Message, Integer expectedTimeoutSeconds )throws java.io.IOException;
    
    public void checkSentParameter(String parameterName, String parameterValue);

    public void validateProtocolMessage(ProtocolMessage protocolMsg, String type, String subType);
    
    public CommandBuilder getCommandBuilder();

}

