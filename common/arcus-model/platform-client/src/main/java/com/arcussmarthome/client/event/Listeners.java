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
package com.arcussmarthome.client.event;

import org.eclipse.jdt.annotation.NonNull;

/**
 * 
 */
public class Listeners {

   /**
    * Returns an non-null registration for use before a listener has been
    * registered.
    * @return
    */
   public static ListenerRegistration unregistered() {
      return UnregisteredListener.INSTANCE;
   }
   
   public static ListenerRegistration unregister(@NonNull ListenerRegistration registration) {
      if(registration != null) {
         registration.remove();
      }
      return UnregisteredListener.INSTANCE;
   }
   
   private static class UnregisteredListener implements ListenerRegistration {
      private static final UnregisteredListener INSTANCE = new UnregisteredListener();
      
      @Override
      public boolean isRegistered() {
         return false;
      }

      @Override
      public boolean remove() {
         return false;
      }
      
   }
}

