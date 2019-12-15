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

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.arcussmarthome.bridge.metrics.BridgeMetrics;
import com.arcussmarthome.bridge.server.http.HttpSender;
import com.arcussmarthome.bridge.server.http.annotation.HttpPost;
import com.arcussmarthome.bridge.server.http.impl.HttpResource;
import com.arcussmarthome.bridge.server.http.impl.auth.AlwaysAllow;
import com.arcussmarthome.core.dao.PersonDAO;
import com.arcussmarthome.core.notification.Notifications;
import com.arcussmarthome.core.platform.PlatformMessageBus;
import com.arcussmarthome.io.json.JSON;
import com.arcussmarthome.messages.ClientMessage;
import com.arcussmarthome.messages.MessageBody;
import com.arcussmarthome.messages.PlatformMessage;
import com.arcussmarthome.messages.address.Address;
import com.arcussmarthome.messages.capability.NotificationCapability;
import com.arcussmarthome.messages.model.Person;
import com.arcussmarthome.messages.service.PersonService;
import com.arcussmarthome.messages.services.PlatformConstants;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

@Singleton
@HttpPost("/person/SendPasswordReset")
public class SendPasswordResetRESTHandler extends HttpResource {
   private final PersonDAO personDao;
   private final PlatformMessageBus platformBus;

   @Inject
   public SendPasswordResetRESTHandler(PersonDAO personDao, PlatformMessageBus platformBus, BridgeMetrics metrics, AlwaysAllow alwaysAllow) {
      super(alwaysAllow, new HttpSender(SendPasswordResetRESTHandler.class, metrics));
      this.personDao = personDao;
      this.platformBus = platformBus;
   }

   @Override
   public FullHttpResponse respond(FullHttpRequest req, ChannelHandlerContext ctx) throws Exception {
      String json = req.content().toString(CharsetUtil.UTF_8);
      ClientMessage clientMessage = JSON.fromJson(json, ClientMessage.class);

      MessageBody body = clientMessage.getPayload();

      String email = PersonService.SendPasswordResetRequest.getEmail(body);
      String method = PersonService.SendPasswordResetRequest.getMethod(body);

      Person person = personDao.findByEmail(email);

      if(person != null) {
         String token = personDao.generatePasswordResetToken(email);
         notify(person, method, token);
      }

      ClientMessage response = ClientMessage.builder()
            .withCorrelationId(clientMessage.getCorrelationId())
            .withSource(Address.platformService(PersonService.NAMESPACE).getRepresentation())
            .withPayload(PersonService.SendPasswordResetResponse.instance())
            .create();
      return new DefaultFullHttpResponse(
            HttpVersion.HTTP_1_1,
            HttpResponseStatus.OK,
            Unpooled.copiedBuffer(JSON.toJson(response), CharsetUtil.UTF_8)
      );
   }

   private void notify(Person person, String method, String token) {
      PlatformMessage msg = Notifications.builder()
            .withPersonId(person.getId())
            .withSource(Address.platformService(PlatformConstants.SERVICE_PEOPLE))
            .withPriority(PersonService.SendPasswordResetRequest.METHOD_EMAIL.equals(method) 
                  ? NotificationCapability.NotifyRequest.PRIORITY_LOW
                  : NotificationCapability.NotifyRequest.PRIORITY_HIGH)
            .withMsgKey(Notifications.PasswordReset.KEY)
            .addMsgParam(Notifications.PasswordReset.PARAM_TOKEN, token)
            .create();
      platformBus.send(msg);
   }
}

