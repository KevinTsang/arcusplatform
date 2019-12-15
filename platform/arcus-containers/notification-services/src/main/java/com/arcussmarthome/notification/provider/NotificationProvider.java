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

import com.arcussmarthome.notification.dispatch.DispatchException;
import com.arcussmarthome.notification.dispatch.DispatchUnsupportedByUserException;
import com.arcussmarthome.platform.notification.Notification;

/**
 * Represents a provider of notification delivery services; a means of sending an asynchronous message to a customer, typically in
 * the form of a push notification, interactive voice call or email
 *
 */
public interface NotificationProvider {

    /**
     * Sends the given notification to the customer using an endpoint and process that is specific to each provider
     * implementation.
     *
     * @param notification
     *            The notification to deliver.
     * @throws DispatchException
     *             Thrown to indicate an immediate, synchronous error occurred trying to deliver the notification. Even when no
     *             exception is thrown, the delivery may still fail. In this case, errors will be reported via the
     *             {@link com.arcussmarthome.notification.upstream.UpstreamNotificationResponder} object.
     * @throws DispatchUnsupportedByUserException
     *             Thrown to indicate that an unrecoverable dispatch exception has occurred and that the notification should not
     *             try to be re-delivered.
     * @throws NotificationSplitException
     *             Thrown to indicate the notification could not be delivered as is, but was split into one or more child
     *             notifications for future delivery.
     */
    public void notifyCustomer(Notification notification) throws DispatchException, DispatchUnsupportedByUserException;
    
    default public boolean supportedByUser(Notification notification) {
       return true;
    }
    
}

