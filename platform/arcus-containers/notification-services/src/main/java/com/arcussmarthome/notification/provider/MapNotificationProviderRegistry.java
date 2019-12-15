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
package com.arcussmarthome.notification.provider;

import java.util.Map;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.arcussmarthome.platform.notification.NotificationMethod;

@Singleton
public class MapNotificationProviderRegistry implements NotificationProviderRegistry {

    @Inject
    private Map<String, NotificationProvider> providerRegistry;

    @Override
    public NotificationProvider getInstanceForProvider(NotificationMethod method) throws NoSuchProviderException {

        if (method == null) {
            throw new IllegalArgumentException("Notification method cannot be null.");
        }

        NotificationProvider providerInstance = providerRegistry.get(method.toString());

        if (providerInstance == null) {
            throw new NoSuchProviderException("No notification provider registered with the name: " + method);
        }

        return providerInstance;
    }
}

