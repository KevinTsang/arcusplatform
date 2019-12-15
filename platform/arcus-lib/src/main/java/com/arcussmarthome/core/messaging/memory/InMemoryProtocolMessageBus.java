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
package com.arcussmarthome.core.messaging.memory;

import java.util.concurrent.Executors;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.google.inject.Singleton;
import com.arcussmarthome.core.protocol.ProtocolMessageBus;
import com.arcussmarthome.io.json.JSON;
import com.arcussmarthome.protocol.ProtocolMessage;

/**
 *
 */
@Singleton
public class InMemoryProtocolMessageBus extends InMemoryMessageBus<ProtocolMessage> implements ProtocolMessageBus {

   public InMemoryProtocolMessageBus() {
      super(
            "protocol-bus",
            JSON.createSerializer(ProtocolMessage.class),
            JSON.createDeserializer(ProtocolMessage.class),
            Executors.newSingleThreadExecutor(
                  new ThreadFactoryBuilder()
                     .setNameFormat("protocol-bus-dispatcher")
                     .build()
            )
      );
   }
   
}

