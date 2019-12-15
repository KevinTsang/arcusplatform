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
package com.arcussmarthome.driver.service.matcher;

import com.arcussmarthome.device.attributes.AttributeMap;
import com.arcussmarthome.messages.model.DriverId;

/**
 * Algorithm for selecting the best driver for a given device
 * based on the attributes provided by the protocol controller.
 */
public interface DiscoveryAlgorithm {

   /**
    * Finds the "best" driver for the given set of attributes.
    * This should return {@code null} if no drivers are
    * appropriate.
    * @param population
    * @param protocolAttributes
    * @return
    */
   public DriverId discover(String population, AttributeMap protocolAttributes, Integer maxReflexVersion);
}

