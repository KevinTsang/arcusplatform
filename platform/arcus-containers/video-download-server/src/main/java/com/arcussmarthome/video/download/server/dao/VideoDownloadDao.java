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
package com.arcussmarthome.video.download.server.dao;

import java.net.URI;
import java.util.Date;
import java.util.UUID;

import org.eclipse.jdt.annotation.Nullable;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.arcussmarthome.video.VideoDao;
import com.arcussmarthome.video.VideoRecording;
import com.arcussmarthome.video.storage.VideoStorage;

@Singleton
public class VideoDownloadDao {
   private final VideoDao videoDao;
   private final VideoStorage videoStorage;

   @Inject
   public VideoDownloadDao(VideoDao videoDao, VideoStorage videoStorage) {
      this.videoDao = videoDao;
      this.videoStorage = videoStorage;

   }

   public URI getUri(String storageLocation, Date ts) throws Exception {
      return videoStorage.createPlaybackUri(storageLocation, ts);
   }

   @Nullable
   public VideoDownloadSession session(UUID id) {
      VideoRecording rec = videoDao.getVideoRecordingById(id);
      return rec == null ? null : new VideoDownloadSession(rec);
   }

}

