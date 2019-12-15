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

import java.util.Arrays;
import java.util.UUID;

import org.easymock.EasyMock;
import org.junit.Test;

import com.google.inject.Inject;
import com.arcussmarthome.bridge.metrics.BridgeMetrics;
import com.arcussmarthome.bridge.server.client.Client;
import com.arcussmarthome.bridge.server.client.ClientFactory;
import com.arcussmarthome.bridge.server.config.RESTHandlerConfig;
import com.arcussmarthome.bridge.server.http.impl.auth.SessionAuth;
import com.arcussmarthome.bridge.server.noauth.NoAuthModule;
import com.arcussmarthome.core.dao.AuthorizationGrantDAO;
import com.arcussmarthome.core.dao.PersonDAO;
import com.arcussmarthome.core.dao.PlaceDAO;
import com.arcussmarthome.core.messaging.memory.InMemoryMessageModule;
import com.arcussmarthome.core.messaging.memory.InMemoryPlatformMessageBus;
import com.arcussmarthome.io.json.JSON;
import com.arcussmarthome.messages.ClientMessage;
import com.arcussmarthome.messages.capability.PersonCapability;
import com.arcussmarthome.messages.errors.Errors;
import com.arcussmarthome.messages.model.Person;
import com.arcussmarthome.population.PlacePopulationCacheManager;
import com.arcussmarthome.security.authz.AuthorizationContext;
import com.arcussmarthome.security.authz.AuthorizationGrant;
import com.arcussmarthome.test.IrisMockTestCase;
import com.arcussmarthome.test.Mocks;
import com.arcussmarthome.test.Modules;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.Attribute;
import io.netty.util.CharsetUtil;

@Mocks({PersonDAO.class, PlaceDAO.class, AuthorizationGrantDAO.class, BridgeMetrics.class, ChannelHandlerContext.class, Channel.class, Client.class, Attribute.class, PlacePopulationCacheManager.class})
@Modules({InMemoryMessageModule.class, NoAuthModule.class})
public class TestVerifyPinRESTHandler extends IrisMockTestCase {

   @Inject private PersonDAO personDao;
   @Inject private AuthorizationGrantDAO grantDao;
   @Inject private InMemoryPlatformMessageBus bus;
   @Inject private BridgeMetrics metrics;
   @Inject private ChannelHandlerContext ctx;
   @Inject private Channel channel;
   @Inject private Client client;
   @Inject private ClientFactory clientFactory;
   @SuppressWarnings("rawtypes")
   @Inject private Attribute clientAttr;
   @Inject private PlacePopulationCacheManager mockPopulationCacheMgr;

   private Person person;
   private VerifyPinRESTHandler handler;
   private AuthorizationGrant grant;

   @SuppressWarnings("unchecked")
   @Override
   public void setUp() throws Exception {
      super.setUp();
      EasyMock.expect(ctx.channel()).andReturn(channel).anyTimes();
      EasyMock.expect(channel.attr(Client.ATTR_CLIENT)).andReturn(clientAttr).anyTimes();
      EasyMock.expect(clientAttr.get()).andReturn(client).anyTimes();
      person = new Person();
      person.setId(UUID.randomUUID());
      handler = new VerifyPinRESTHandler(clientFactory, personDao, grantDao, bus, mockPopulationCacheMgr, metrics, new SessionAuth(metrics, null, clientFactory) {
         @Override
         public boolean isAuthorized(ChannelHandlerContext ctx, FullHttpRequest req) {
            return true;
         }
      }, new RESTHandlerConfig());
      person.setCurrPlace(UUID.randomUUID());

      grant = new AuthorizationGrant();
      grant.setPlaceId(person.getCurrPlace());
      grant.setEntityId(person.getId());
      person.setPinAtPlace(person.getCurrPlace(), "1111");
      grant.addPermissions("*:*:*");
   }

   @Test
   public void testMissingPinErrors() throws Exception {     
      EasyMock.expect(personDao.findById(person.getId())).andReturn(person);
      EasyMock.expect(grantDao.findForEntity(person.getId())).andReturn(Arrays.asList(grant));
      EasyMock.expect(client.getPrincipalId()).andReturn(person.getId()).anyTimes();
      
      replay();
      FullHttpRequest req = createRequest(null, person.getCurrPlace().toString());
      FullHttpResponse res = handler.respond(req, ctx);
      assertError(res, Errors.CODE_MISSING_PARAM);
   }

   @Test
   public void testMalformedPinTooShortErrors() throws Exception {
      Person saved = person.copy();
      
      EasyMock.expect(personDao.findById(person.getId())).andReturn(person);
      EasyMock.expect(grantDao.findForEntity(person.getId())).andReturn(Arrays.asList(grant));
      EasyMock.expect(client.getPrincipalId()).andReturn(saved.getId()).anyTimes();
      
      replay();
      FullHttpRequest req = createRequest("111", person.getCurrPlace().toString());
      FullHttpResponse res = handler.respond(req, ctx);
      assertError(res, Errors.CODE_INVALID_REQUEST);
   }

   @Test
   public void testMalformedPinTooLongErrors() throws Exception {
      Person saved = person.copy();
      
      EasyMock.expect(personDao.findById(person.getId())).andReturn(person);
      EasyMock.expect(grantDao.findForEntity(person.getId())).andReturn(Arrays.asList(grant));
      EasyMock.expect(client.getPrincipalId()).andReturn(saved.getId()).anyTimes();
      
      replay();
      FullHttpRequest req = createRequest("11111", person.getCurrPlace().toString());
      FullHttpResponse res = handler.respond(req, ctx);
      assertError(res, Errors.CODE_INVALID_REQUEST);
   }

   @Test
   public void testMalformedPinCharsErrors() throws Exception {
      Person saved = person.copy();
      EasyMock.expect(personDao.findById(person.getId())).andReturn(person);
      EasyMock.expect(grantDao.findForEntity(person.getId())).andReturn(Arrays.asList(grant));
      EasyMock.expect(client.getPrincipalId()).andReturn(saved.getId()).anyTimes();
      replay();
      FullHttpRequest req = createRequest("1111a", person.getCurrPlace().toString());
      FullHttpResponse res = handler.respond(req, ctx);
      assertError(res, Errors.CODE_INVALID_REQUEST);
   }

   @Test
   public void testNoPersonErrors() throws Exception {
      EasyMock.expect(personDao.findById(person.getId())).andReturn(null);
      replay();
      FullHttpRequest req = createRequest("1111", person.getCurrPlace().toString());
      FullHttpResponse res = handler.respond(req, ctx);
      assertError(res, PinErrors.PERSON_NOT_FOUND_CODE);
   }

   @Test
   public void testVerifyPinSuccess() throws Exception {
      Person saved = person.copy();
      saved.setPinAtPlace(person.getCurrPlace(), "1111");

      AuthorizationGrant grant = new AuthorizationGrant();
      grant.setPlaceId(person.getCurrPlace());
      grant.setEntityId(person.getId());
      grant.addPermissions("*:*:*");

      AuthorizationContext ctxt = new AuthorizationContext(null, null, Arrays.asList(grant));

      EasyMock.expect(personDao.findById(person.getId())).andReturn(person).anyTimes();
      EasyMock.expect(grantDao.findForEntity(person.getId())).andReturn(Arrays.asList(grant)).anyTimes();
      EasyMock.expect(client.getAuthorizationContext()).andReturn(ctxt).anyTimes();
      EasyMock.expect(client.getPrincipalId()).andReturn(saved.getId()).anyTimes();
      replay();

      FullHttpRequest req = createRequest("1111", person.getCurrPlace().toString());
      FullHttpResponse res = handler.respond(req, ctx);
      assertOk(res, true);
   }
   
   @Test
   public void testVerifyPinError() throws Exception {
      Person saved = person.copy();
      saved.setPinAtPlace(person.getCurrPlace(), "1111");

      AuthorizationGrant grant = new AuthorizationGrant();
      grant.setPlaceId(person.getCurrPlace());
      grant.setEntityId(person.getId());
      grant.addPermissions("*:*:*");

      AuthorizationContext ctxt = new AuthorizationContext(null, null, Arrays.asList(grant));

      EasyMock.expect(personDao.findById(person.getId())).andReturn(person).anyTimes();
      EasyMock.expect(grantDao.findForEntity(person.getId())).andReturn(Arrays.asList(grant)).anyTimes();
      EasyMock.expect(client.getAuthorizationContext()).andReturn(ctxt).anyTimes();
      EasyMock.expect(client.getPrincipalId()).andReturn(saved.getId()).anyTimes();
      replay();

      FullHttpRequest req = createRequest("1112", person.getCurrPlace().toString());
      FullHttpResponse res = handler.respond(req, ctx);
      assertError(res, "MismatchedPins");
   }  
   
   private FullHttpRequest createRequest(String pin, String place) {
      PersonCapability.VerifyPinRequest.Builder builder = PersonCapability.VerifyPinRequest.builder();
      
      if(pin != null) {
         builder.withPin(pin);
      }
      
      if (place != null) {
         builder.withPlace(place);
      }

      ClientMessage msg = ClientMessage.builder()
            .withCorrelationId("correlationid")
            .withDestination(person.getAddress())
            .withPayload(builder.build())
            .create();

      FullHttpRequest req = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.POST, "/person/VerifyPin");
      req.headers().add(HttpHeaders.Names.CONTENT_TYPE, "application/json");

      ByteBuf buffer = Unpooled.copiedBuffer(JSON.toJson(msg), CharsetUtil.UTF_8);
      req.headers().add(HttpHeaders.Names.CONTENT_LENGTH, buffer.readableBytes());
      req.content().clear().writeBytes(buffer);
      return req;
   }

   private void assertOk(FullHttpResponse res, boolean success) {
      assertEquals(success ? HttpResponseStatus.OK : HttpResponseStatus.BAD_REQUEST, res.getStatus());
      String json = res.content().toString(CharsetUtil.UTF_8);
      ClientMessage clientMessage = JSON.fromJson(json, ClientMessage.class);
      assertEquals(PersonCapability.VerifyPinResponse.NAME, clientMessage.getType());
      assertEquals(success, PersonCapability.VerifyPinResponse.getSuccess(clientMessage.getPayload()));
   }

   private void assertError(FullHttpResponse res, String code) {
      assertEquals(HttpResponseStatus.INTERNAL_SERVER_ERROR, res.getStatus());
      String json = res.content().toString(CharsetUtil.UTF_8);
      ClientMessage clientMessage = JSON.fromJson(json, ClientMessage.class);
      assertEquals("Error", clientMessage.getType());
      assertEquals(code, clientMessage.getPayload().getAttributes().get("code"));
   }

}

