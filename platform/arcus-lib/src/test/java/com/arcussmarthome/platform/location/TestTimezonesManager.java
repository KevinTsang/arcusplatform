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
package com.arcussmarthome.platform.location;

import org.junit.Before;
import org.junit.Test;

import com.google.inject.Inject;
import com.arcussmarthome.messages.model.Place;
import com.arcussmarthome.messages.type.TimeZone;
import com.arcussmarthome.test.IrisTestCase;
import com.arcussmarthome.test.Modules;

@Modules({TimezonesModule.class})
public class TestTimezonesManager extends IrisTestCase {
   
   @Inject private TimezonesManager tzMgr;
   
   private Place place;

   @Before
   public void setup(){
      place=new Place();
      place.setZipCode("66044");
   }
   
   @Test
   public void testChicago(){
      TimeZone tz = tzMgr.getTimeZoneById("America/Chicago");
      assertNotNull(tz);
      assertEquals("Central", tz.getName());

   }
   
   @Test
   public void testIndianapolis() {
	   TimeZone tz = tzMgr.getTimeZoneById("America/Indiana/Indianapolis");
	   assertNotNull(tz);
	   assertEquals("Indiana/Indianapolis", tz.getName());	   
   }
   
   @Test
   public void testNonExist() {
	   TimeZone tz = tzMgr.getTimeZoneById("America/NotValid");
	   assertNull(tz);   
   }
   
   
}

