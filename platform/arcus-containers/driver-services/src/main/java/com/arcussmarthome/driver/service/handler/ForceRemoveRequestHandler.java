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
package com.arcussmarthome.driver.service.handler;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.arcussmarthome.core.dao.DeviceDAO;
import com.arcussmarthome.core.platform.PlatformMessageBus;
import com.arcussmarthome.driver.groovy.plugin.GroovyDriverPlugin;
import com.arcussmarthome.driver.service.DeviceService;
import com.arcussmarthome.messages.MessageBody;
import com.arcussmarthome.messages.PlatformMessage;
import com.arcussmarthome.messages.capability.DeviceCapability;
import com.arcussmarthome.messages.capability.DeviceCapability.ForceRemoveResponse;
import com.arcussmarthome.messages.model.Device;

public class ForceRemoveRequestHandler extends AbstractRemoveRequestHandler {
   private static final Logger logger = LoggerFactory.getLogger(ForceRemoveRequestHandler.class);
   private final DeviceService service;

   @Inject
   public ForceRemoveRequestHandler(
         DeviceDAO deviceDao,
         PlatformMessageBus platformBus,
         Set<GroovyDriverPlugin> plugins,
         DeviceService service
   ) {
      super(deviceDao, platformBus, plugins);
      this.service = service;
   }

   @Override
   public String getMessageType() {
      return DeviceCapability.ForceRemoveRequest.NAME;
   }

   @Override
   public MessageBody handleMessage(PlatformMessage message) throws Exception {
      logger.debug("received a force remove device request: {}", message);
      Device device = loadDevice(message);
      if(device.isLost()) {
         service.delete(device);
      }
      else {
         try {
            sendRemoveRequest(device, 0L, true);
         }
         catch(Exception e) {
            logger.warn("Unable to cleanly remove device {}", message.getSource());
         }
         service.tombstone(device);
      }
      return ForceRemoveResponse.instance();
   }
}

