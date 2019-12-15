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
package com.arcussmarthome.driver.service.registry;

import com.arcussmarthome.messages.model.DriverId;

public class DriverScriptInfo {
   private final String scriptName;
   private final DriverId driverId;
   private final Exception loadingEx;

   public DriverScriptInfo(String scriptName, DriverId driverId) {
      this(scriptName, driverId, null);
   }

   public DriverScriptInfo(String scriptName, Exception loadingEx) {
      this(scriptName, null, loadingEx);
   }

   public DriverScriptInfo(String scriptName, DriverId driverId, Exception loadingEx) {
      this.scriptName = scriptName;
      this.driverId = driverId;
      this.loadingEx = loadingEx;
   }

   public String getScriptName() {
      return scriptName;
   }

   public DriverId getDriverId() {
      return driverId;
   }

   public String getDriverName() {
      return driverId != null ? driverId.getName() : "";
   }

   public String getDriverVersion() {
      return driverId != null
            ? (driverId.getVersion() != null ? driverId.getVersion().getRepresentation() : "")
                  : "";
   }

   public String getValidationStatus() {
      return loadingEx != null ? loadingEx.getMessage() : "Success";
   }

   public Exception getLoadingEx() {
      return loadingEx;
   }
}

