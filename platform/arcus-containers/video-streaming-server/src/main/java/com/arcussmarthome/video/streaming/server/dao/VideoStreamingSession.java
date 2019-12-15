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
package com.arcussmarthome.video.streaming.server.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.arcussmarthome.video.VideoRecording;

public class VideoStreamingSession {
   private static final Logger log = LoggerFactory.getLogger(VideoStreamingSession.class);

   private final VideoRecording recording;

   VideoStreamingSession(VideoRecording recording) {
      this.recording = recording;

      log.info("starting streaming session with id={}", recording.recordingId);
   }

   public VideoRecording getRecording() {
      return recording;
   }
}

