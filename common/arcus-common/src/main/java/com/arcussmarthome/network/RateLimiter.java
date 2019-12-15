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

import java.util.concurrent.TimeUnit;

public interface RateLimiter {
   void acquire(double size, NetworkClock clock) throws InterruptedException;
   boolean tryAcquire(double size, NetworkClock clock);
   long tryAcquireOrGetWait(double size, NetworkClock clock);
   boolean tryAcquire(double size, long timeout, TimeUnit unit, NetworkClock clock) throws InterruptedException;
   
   long getApproximateWaitTimeInNs(double size, NetworkClock clock);
}

