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
package com.arcussmarthome.platform.hub.registration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.arcussmarthome.messages.ErrorEvent;
import com.arcussmarthome.messages.MessageBody;
import com.arcussmarthome.messages.PlatformMessage;
import com.arcussmarthome.messages.capability.HubAdvancedCapability;
import com.arcussmarthome.messages.capability.HubAdvancedCapability.FirmwareUpdateResponse;
import com.arcussmarthome.messages.model.HubRegistrationErrors;

@Singleton
public class HubRegistrationMessageHandlerAdaptor  {
   private static final Logger logger = LoggerFactory.getLogger(HubRegistrationMessageHandlerAdaptor.class);   
   private final HubRegistrationRegistry hubRegistrationRegistry;
   
   @Inject
   public HubRegistrationMessageHandlerAdaptor(
         HubRegistrationRegistry hubRegistrationRegistry
   ) {
	   this.hubRegistrationRegistry = hubRegistrationRegistry;
   }
      
   public void handleErrorEvent(String hubId, PlatformMessage message) {
	   if(message.getValue() instanceof ErrorEvent) {
		   ErrorEvent value = (ErrorEvent) message.getValue();
		   hubRegistrationRegistry.firmwareUpgradeFailed(hubId, value.getCode(), value.getMessage());	
	   }
   }

   public void handleFirmwareUpdateResponse(String hubId, PlatformMessage message) {
	   String status = HubAdvancedCapability.FirmwareUpdateResponse.getStatus(message.getValue());	   
	   if(!FirmwareUpdateResponse.STATUS_OK.equals(status)) {
		   hubRegistrationRegistry.firmwareUpgradeFailed(hubId, HubRegistrationErrors.REFUSED.getCode(), HubRegistrationErrors.REFUSED.getMessage());
	   }
		
   }

   public void handleFirmwareUpgradeProcessEvent(String hubId, PlatformMessage message) {
	   MessageBody request = message.getValue();
	   String upgradeStatus = HubAdvancedCapability.FirmwareUpgradeProcessEvent.getStatus(request);
	   Double percentDone = HubAdvancedCapability.FirmwareUpgradeProcessEvent.getPercentDone(request);
	   hubRegistrationRegistry.updateFirmwareUpgradeProcess(hubId, upgradeStatus, percentDone); 	   
   }

}

