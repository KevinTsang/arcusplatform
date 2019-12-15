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
package com.arcussmarthome.video.preview.server;

import com.codahale.metrics.Counter;
import com.codahale.metrics.Histogram;
import com.codahale.metrics.Timer;
import com.arcussmarthome.metrics.IrisMetricSet;
import com.arcussmarthome.metrics.IrisMetrics;

public final class VideoPreviewMetrics {
   public static final IrisMetricSet METRICS = IrisMetrics.metrics("video.preview");

   public static final Timer PREVIEW_SUCCESS = METRICS.timer("success");
   public static final Timer PREVIEW_FAIL = METRICS.timer("fail");
   public static final Timer PREVIEW_READ = METRICS.timer("read.time");

   public static final Counter PREVIEW_STARTED = METRICS.counter("connected");
   public static final Counter PREVIEW_BAD = METRICS.counter("bad.request");
   public static final Counter PREVIEW_UNAUTH = METRICS.counter("unauthorized");
   public static final Counter PREVIEW_NOTFOUND = METRICS.counter("not.found");
   public static final Counter PREVIEW_EMPTY = METRICS.counter("empty");

   public static final Histogram PREVIEW_SIZE = METRICS.histogram("file.bytes");

   private VideoPreviewMetrics() {
   }
}

