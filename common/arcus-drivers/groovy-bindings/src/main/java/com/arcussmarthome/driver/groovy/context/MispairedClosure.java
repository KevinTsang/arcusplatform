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
package com.arcussmarthome.driver.groovy.context;

import java.util.HashMap;
import java.util.Map;

import com.arcussmarthome.driver.DeviceDriverContext;
import com.arcussmarthome.driver.groovy.GroovyContextObject;
import com.arcussmarthome.messages.capability.DeviceAdvancedCapability;

import groovy.lang.Closure;

public class MispairedClosure extends Closure<Object> {

   public MispairedClosure(Object owner) {
      super(owner);
   }

   protected void doCall() {
      doCall("Unable to fully configure device, some operations may not work");
   }
   
   protected void doCall(String message) {
      DeviceDriverContext context = GroovyContextObject.getContext();
      context.setAttributeValue(DeviceAdvancedCapability.KEY_DRIVERSTATE, DeviceAdvancedCapability.DRIVERSTATE_UNSUPPORTED);
      Map<String, String> errors = context.getAttributeValue(DeviceAdvancedCapability.KEY_ERRORS);
      errors = errors != null ? new HashMap<String, String>(errors) : new HashMap<String, String>();
      errors.put(DeviceAdvancedCapability.PairingFailedException.CODE_PAIRING_FAILED, message);
      errors.remove(DeviceAdvancedCapability.PairingMisconfiguredException.CODE_PAIRING_MISCONFIGURED);
      context.setAttributeValue(DeviceAdvancedCapability.KEY_ERRORS, errors);
   }

}

