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
package com.arcussmarthome.hubcom.server.ssl;

import java.util.Properties;

import com.google.inject.Inject;
import com.google.inject.name.Named;

public class TrustConfig {
   @Inject(optional = true) @Named("tls.server.truststore.filepath")
   private String tlsServerTruststoreFilepath = null;

   @Inject(optional = true) @Named("tls.server.truststore.password")
   private String tlsServerTruststorePassword = null;

   public String getTlsServerTruststoreFilepath() {
      return tlsServerTruststoreFilepath;
   }

   public void setTlsServerTruststoreFilepath(String tlsServerTruststoreFilepath) {
      this.tlsServerTruststoreFilepath = tlsServerTruststoreFilepath;
   }

   public String getTlsServerTruststorePassword() {
      return tlsServerTruststorePassword;
   }

   public void setTlsServerTruststorePassword(String tlsServerTruststorePassword) {
      this.tlsServerTruststorePassword = tlsServerTruststorePassword;
   }

   public Properties toProperties() {
      Properties props = new Properties();

      String fp = getTlsServerTruststoreFilepath();
      String ps = getTlsServerTruststorePassword();

      if (fp != null && ps != null) {
         props.put("tls.server.truststore.filepath", getTlsServerTruststoreFilepath());
         props.put("tls.server.truststore.password", getTlsServerTruststorePassword());
      }

      return props;
   }
}

