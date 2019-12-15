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
package com.arcussmarthome.protocol.control;

import com.arcussmarthome.capability.definition.ProtocolDefinition;
import com.arcussmarthome.io.Deserializer;
import com.arcussmarthome.io.Serializer;
import com.arcussmarthome.io.json.JSON;
import com.arcussmarthome.messages.MessageBody;
import com.arcussmarthome.messages.PlatformMessage;
import com.arcussmarthome.protocol.Protocol;
import com.arcussmarthome.protocol.RemoveProtocolRequest;
import com.arcussmarthome.protocol.constants.ControlConstants;

public enum ControlProtocol implements Protocol<MessageBody>, ControlConstants {
   INSTANCE;

   private final Serializer<MessageBody> serializer = JSON.createSerializer(MessageBody.class);
   private final Deserializer<MessageBody> deserializer = JSON.createDeserializer(MessageBody.class);

   @Override
   public String getName() {
      return NAME;
   }

   @Override
   public String getNamespace() {
      return NAMESPACE;
   }

   @Override
   public ProtocolDefinition getDefinition() {
      return DEFINITION;
   }

   @Override
   public Serializer<MessageBody> createSerializer() {
      return serializer;
   }

   @Override
   public Deserializer<MessageBody> createDeserializer() {
      return deserializer;
   }

   @Override
   public boolean isTransientAddress() {
      return false;
   }

   @Override
   public PlatformMessage remove(RemoveProtocolRequest rpd) {
      throw new RuntimeException("control protocol cannot remove devices");
   }

}

