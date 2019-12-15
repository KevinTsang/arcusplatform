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
package com.arcussmarthome.netty.bus;

import com.google.inject.Inject;
import com.arcussmarthome.bridge.bus.AbstractPlatformBusService;
import com.arcussmarthome.bridge.bus.PlatformBusListener;
import com.arcussmarthome.bridge.metrics.BridgeMetrics;
import com.arcussmarthome.bridge.server.session.ClientToken;
import com.arcussmarthome.core.platform.PlatformMessageBus;
import com.arcussmarthome.messages.MessageConstants;
import com.arcussmarthome.messages.PlatformMessage;
import com.arcussmarthome.messages.address.AddressMatcher;
import com.arcussmarthome.messages.address.AddressMatchers;
import com.arcussmarthome.netty.server.session.IrisNettyClientClientToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

public class IrisNettyPlatformBusServiceImpl extends AbstractPlatformBusService {
   private static final Logger logger = LoggerFactory.getLogger(IrisNettyPlatformBusServiceImpl.class);

   private static final Set<AddressMatcher> ADDRESSES =
         AddressMatchers.platformNamespaces(
               MessageConstants.BROADCAST, MessageConstants.CLIENT
         );

   @Inject
   public IrisNettyPlatformBusServiceImpl(PlatformMessageBus platformBus, BridgeMetrics metrics, Set<PlatformBusListener> listeners) {
      super(platformBus, metrics, ADDRESSES);
      for(PlatformBusListener listener: listeners) {
         addPlatformListener(listener);
      }
   }

   @Override
   public void handlePlatformMessage(PlatformMessage msg) {
      logger.trace("Handling Platform Msg: {}", msg);
      bridgeMetrics.incPlatformMsgReceivedCounter();

      if (msg.getDestination().isBroadcast()) {
         for (PlatformBusListener listener : listeners) {
            // A null ClientToken indicates the message should be broadcast
            listener.onMessage(null, msg);
         }
      }
      else {
         ClientToken ct = IrisNettyClientClientToken.fromAddress(msg.getDestination());
         if (ct != null) {
            for (PlatformBusListener listener : listeners) {
               listener.onMessage(ct, msg);
            }
         }
         else {
            logger.debug("Ignoring non-client message [{}]", msg);
            bridgeMetrics.incPlatformMsgDiscardedCounter();
         }
      }
   }
}

