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
package com.arcussmarthome.network;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public interface PacketScheduler<T> {
   T take() throws InterruptedException;
   T poll();
   T poll(long timeout, TimeUnit unit) throws InterruptedException;

   Producer<T> attach();
   Producer<T> attach(BlockingQueue<T> queue);
   Producer<T> attach(BlockingQueue<T> queue, RateLimiter rateLimiter);
   void detach(Producer<T> producer);

   RateLimiter getRateLimiter();

   public static interface Producer<T> {
      void send(T packet) throws InterruptedException;
      boolean offer(T packet);
      boolean offer(T packet, long time, TimeUnit unit) throws InterruptedException;

      RateLimiter getRateLimiter();
   }

   public static interface PacketDropHandler<T> {
      void queueDroppedPacket(T packet);
   }

   public static interface QueueStateHandler<T> {
      void queueCapacityBelowWatermark();
      void queueCapacityAboveWatermark();
   }
}

