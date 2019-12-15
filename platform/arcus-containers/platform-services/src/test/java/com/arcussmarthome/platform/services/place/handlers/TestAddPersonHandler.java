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
package com.arcussmarthome.platform.services.place.handlers;

import java.util.UUID;

import org.easymock.EasyMock;
import org.junit.Test;

import com.google.inject.Inject;
import com.arcussmarthome.capability.attribute.transform.AttributeMapTransformModule;
import com.arcussmarthome.capability.attribute.transform.BeanAttributesTransformer;
import com.arcussmarthome.core.dao.AuthorizationGrantDAO;
import com.arcussmarthome.core.dao.PersonDAO;
import com.arcussmarthome.core.messaging.memory.InMemoryMessageModule;
import com.arcussmarthome.core.messaging.memory.InMemoryPlatformMessageBus;
import com.arcussmarthome.messages.MessageBody;
import com.arcussmarthome.messages.PlatformMessage;
import com.arcussmarthome.messages.address.Address;
import com.arcussmarthome.messages.capability.Capability;
import com.arcussmarthome.messages.capability.PersonCapability;
import com.arcussmarthome.messages.capability.PlaceCapability;
import com.arcussmarthome.messages.model.Fixtures;
import com.arcussmarthome.messages.model.Person;
import com.arcussmarthome.messages.model.Place;
import com.arcussmarthome.security.authz.AuthorizationGrant;
import com.arcussmarthome.test.IrisMockTestCase;
import com.arcussmarthome.test.Mocks;
import com.arcussmarthome.test.Modules;

@Mocks({PersonDAO.class, AuthorizationGrantDAO.class})
@Modules({InMemoryMessageModule.class, AttributeMapTransformModule.class})
public class TestAddPersonHandler extends IrisMockTestCase {

   @Inject PersonDAO personDaoMock;
   @Inject AuthorizationGrantDAO authGrantDaoMock;
   @Inject InMemoryPlatformMessageBus messageBus;
   @Inject BeanAttributesTransformer<Person> personTransformer;

   private AddPersonHandler handler;
   private Place place;

   @Override
   public void setUp() throws Exception {
      super.setUp();
      handler = new AddPersonHandler(personDaoMock, authGrantDaoMock, personTransformer, messageBus);
      place = new Place();
      place.setAccount(UUID.randomUUID());
      place.setId(UUID.randomUUID());
      place.setName("My Home");
   }

   @Override
   public void tearDown() throws Exception {
      verify();
      super.tearDown();
   }

   @Test
   public void testAddPersonFailsNoFirstOrLastName() {
      replay();
      Person person = new Person();

      PlatformMessage msg = PlatformMessage.buildMessage(
            PlaceCapability.AddPersonRequest.builder().withPerson(personTransformer.transform(person)).build(),
            Address.clientAddress("test", "1"),
            Address.fromString(place.getAddress()))
            .create();

      MessageBody body = handler.handleRequest(place, msg);
      assertEquals("Error", body.getMessageType());
      assertEquals("request.param.missing", body.getAttributes().get("code"));
   }

   @Test
   public void testAddPerson() throws Exception {
   	Person inviterPerson = Fixtures.createPerson();
   	inviterPerson.setId(UUID.randomUUID());
   	
   	
      Person person = new Person();
      person.setFirstName("Foobar");
      person.setLastName("Baz");
      person.setEmail("foo@foo.com");
      person.setMobileNumber("555-555-5555");
      person.setAccountId(place.getAccount());
      person.setCurrPlace(place.getId());
      person.setHasLogin(false);

      Person returned = person.copy();
      returned.setId(UUID.randomUUID());
      

      AuthorizationGrant expectedGrant = new AuthorizationGrant();
      expectedGrant.setAccountId(place.getAccount());
      expectedGrant.setAccountOwner(false);
      expectedGrant.setEntityId(returned.getId());
      expectedGrant.setPlaceId(place.getId());
      expectedGrant.setPlaceName(place.getName());

      EasyMock.expect(personDaoMock.createPersonWithNoLogin(person)).andReturn(returned);
      EasyMock.expect(personDaoMock.findByAddress(Address.fromString(inviterPerson.getAddress()))).andReturn(inviterPerson);
      authGrantDaoMock.save(expectedGrant);
      EasyMock.expectLastCall();

      replay();

      PlatformMessage msg = PlatformMessage.buildMessage(
            PlaceCapability.AddPersonRequest.builder().withPerson(personTransformer.transform(person)).build(),
            Address.clientAddress("test", "1"),
            Address.fromString(place.getAddress()))
            .withActor(Address.fromString(inviterPerson.getAddress()))
            .create();

      MessageBody body = handler.handleRequest(place, msg);
      assertEquals(PlaceCapability.AddPersonResponse.NAME, body.getMessageType());

      PlatformMessage added = messageBus.take();
      assertEquals(Capability.EVENT_ADDED, added.getMessageType());
      assertEquals(Address.fromString(returned.getAddress()), added.getSource());
      String personAddress = PlaceCapability.AddPersonResponse.getNewPerson(body);
      assertNotNull(personAddress);
      assertTrue(personAddress.endsWith(returned.getId().toString()));
      
      assertEquals(inviterPerson.getAddress(), added.getActor().toString());
      MessageBody addedEvent = added.getValue();
      assertFalse(PersonCapability.getOwner(addedEvent));
   }

}

