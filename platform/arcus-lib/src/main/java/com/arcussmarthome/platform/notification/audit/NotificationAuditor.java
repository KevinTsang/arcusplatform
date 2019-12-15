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
package com.arcussmarthome.platform.notification.audit;

import java.time.Instant;

import com.arcussmarthome.platform.notification.Notification;

public interface NotificationAuditor {

	public void log (Notification notification, AuditEventState state);
	public void log (Notification notification, AuditEventState state, String message);
	public void log (Notification notification, AuditEventState state, Exception exception);
   public void log (String id, Instant rxTimestamp, AuditEventState state, String message);
	
}

