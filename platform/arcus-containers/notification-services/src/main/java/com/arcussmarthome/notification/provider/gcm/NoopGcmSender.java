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
package com.arcussmarthome.notification.provider.gcm;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.arcussmarthome.platform.notification.Notification;

public class NoopGcmSender implements GcmSender {
    private static final Logger logger = LoggerFactory.getLogger(NoopGcmSender.class);

    @Override
    public void sendMessage(Notification notification, String toDevice, Map<String, Object> payload) {
       logger.warn("noop apns sender dropping notification: {} - {}", notification, payload);
    }
}

