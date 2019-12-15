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

/**
 * 
 */
public class InvalidRequestException extends ErrorEventException {
   
   public InvalidRequestException() {
      super(Errors.invalidRequest());
   }
   
   public InvalidRequestException(Throwable cause) {
      super(Errors.invalidRequest(), cause);
   }
   
   /**
    * @param code
    *   A message describing why the request was invalid.
    * @param description
    */
   public InvalidRequestException(String message) {
      super(Errors.invalidRequest(message));
   }

   /**
    * @param message
    *   A message describing why the request was invalid.
    * @param cause
    */
   public InvalidRequestException(String message, Throwable cause) {
      super(Errors.invalidRequest(message), cause);
   }

}

