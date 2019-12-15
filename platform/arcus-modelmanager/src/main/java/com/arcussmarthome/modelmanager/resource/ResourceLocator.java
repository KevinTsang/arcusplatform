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
package com.arcussmarthome.modelmanager.resource;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;

// TODO:  collapse with platform resource location
public interface ResourceLocator {

   Collection<URL> listDirectory(String directory) throws IOException;
   URL locate(String directory, String name) throws MalformedURLException;

}

