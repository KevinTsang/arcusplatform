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
package com.arcussmarthome.platform.history.appender.devvc;

import static com.arcussmarthome.messages.capability.TiltCapability.ATTR_TILTSTATE;
import static com.arcussmarthome.messages.capability.TiltCapability.TILTSTATE_FLAT;
import static com.arcussmarthome.messages.capability.TiltCapability.TILTSTATE_UPRIGHT;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.arcussmarthome.messages.capability.DeviceCapability;
import com.arcussmarthome.platform.history.HistoryAppenderDAO;
import com.arcussmarthome.platform.history.appender.AnnotatedDeviceValueChangeAppender;
import com.arcussmarthome.platform.history.appender.ObjectNameCache;
import com.arcussmarthome.platform.history.appender.annotation.AutoTranslate;
import com.arcussmarthome.platform.history.appender.annotation.EnumValue;
import com.arcussmarthome.platform.history.appender.annotation.Group;

@Singleton
@Group(DeviceCapability.NAMESPACE)
@AutoTranslate()
@EnumValue(attr=ATTR_TILTSTATE, val=TILTSTATE_FLAT,    tpl="device.tilt.flat",    critical=true)
@EnumValue(attr=ATTR_TILTSTATE, val=TILTSTATE_UPRIGHT, tpl="device.tilt.upright", critical=true)
public class DeviceTiltAppender extends AnnotatedDeviceValueChangeAppender {
	
	@Inject
	public DeviceTiltAppender(HistoryAppenderDAO appender, ObjectNameCache cache) {
		super(appender, cache);
	}

}

