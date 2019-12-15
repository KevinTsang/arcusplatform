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
package com.arcussmarthome.firmware;

import java.util.Collections;
import java.util.List;

import com.arcussmarthome.resource.Resource;
import com.arcussmarthome.resource.manager.SingleFileResourceManager;

public class FirmwareManager extends SingleFileResourceManager<List<FirmwareUpdate>> {

   public FirmwareManager(Resource managedResource) {
      super(managedResource, new FirmwareParser());
   }

   @Override
   public List<FirmwareUpdate> getParsedData() {
      List<FirmwareUpdate> cachedData = getCachedData();
      return cachedData != null ? Collections.unmodifiableList(cachedData) : null;
   }
}

