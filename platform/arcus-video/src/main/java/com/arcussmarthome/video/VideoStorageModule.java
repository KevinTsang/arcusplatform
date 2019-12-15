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
package com.arcussmarthome.video;

import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.arcussmarthome.bootstrap.guice.AbstractIrisModule;
import com.arcussmarthome.video.storage.VideoStorage;
import com.arcussmarthome.video.storage.VideoStorageAzure;
import com.arcussmarthome.video.storage.VideoStorageFile;

public class VideoStorageModule extends AbstractIrisModule {

   @Inject
   public VideoStorageModule() {
   }

   @Override
   protected void configure() {
   }

   @Singleton @Provides 
   public VideoStorage provideVideoStorage(VideoConfig config) {
      switch (config.getStorageType()) {
      case VideoConfig.VIDEO_STORAGE_TYPE_FS:
         return new VideoStorageFile(config.getStorageFsBasePath());

      case VideoConfig.VIDEO_STORAGE_TYPE_AZURE:
         return new VideoStorageAzure(config.getStorageAzureAccounts(), config.getStorageAzureContainer(), config.getStorageAzureAccessDuration(),
               config.isStorageAzureInstrument(), config.getStorageAzureBufferSize(), config.getStorageAzureFetchSize());

      default:
         throw new RuntimeException("unknown video storage type: " + config.getStorageType());
      }
   }
}

