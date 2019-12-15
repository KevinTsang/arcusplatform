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
package com.arcussmarthome.voice.proactive;

import java.util.Date;

import org.eclipse.jdt.annotation.Nullable;

public class ProactiveCreds {

   private final String access;
   private @Nullable final Date accessExpiry;
   private @Nullable final String refresh;

   public ProactiveCreds(String access) {
      this(access, null, null);
   }

   public ProactiveCreds(String access, @Nullable Date accessExpiry, @Nullable String refresh) {
      this.access = access;
      this.accessExpiry = accessExpiry;
      this.refresh = refresh;
   }

   public String getAccess() {
      return access;
   }

   @Nullable
   public Date getAccessExpiry() {
      return accessExpiry;
   }

   @Nullable
   public String getRefresh() {
      return refresh;
   }

   public boolean expired(long preemptiveTimeMs) {
      if(accessExpiry == null) {
         return false;
      }
      long expiredAt = accessExpiry.getTime() - preemptiveTimeMs;
      return System.currentTimeMillis() >= expiredAt;
   }

   @Override
   public String toString() {
      return "ProactiveCreds{" +
         "access='" + access + '\'' +
         ", accessExpiry=" + accessExpiry +
         ", refresh='" + refresh + '\'' +
         '}';
   }
}

