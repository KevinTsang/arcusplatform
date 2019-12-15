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
package com.arcussmarthome.messages.errors;

import org.apache.commons.lang3.StringUtils;

import com.arcussmarthome.messages.address.Address;

/**
 * 
 */
public class UnauthorizedRequestException extends ErrorEventException {
   private String message;
   
   public UnauthorizedRequestException(Address destination) {
      this(destination, "Unauthorized request");
   }

   public UnauthorizedRequestException(Address destination, String message) {
      this(destination, message, null);
   }

   public UnauthorizedRequestException(Address destination, Throwable cause) {
      this(destination, "Unauthorized request", cause);
   }

   public UnauthorizedRequestException(Address destination, String message, Throwable cause) {
      // this is encoded as a not found in order to not leak the true cause
      super(Errors.notFound(destination));
      this.message = StringUtils.isEmpty(message) ? "Unauthorized" : message;
   }

   /* (non-Javadoc)
    * @see java.lang.Throwable#getMessage()
    */
   @Override
   public String getMessage() {
      return message;
   }

}

