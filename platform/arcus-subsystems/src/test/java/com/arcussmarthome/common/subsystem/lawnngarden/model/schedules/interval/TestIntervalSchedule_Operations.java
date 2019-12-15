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
package com.arcussmarthome.common.subsystem.lawnngarden.model.schedules.interval;

import java.util.Calendar;
import java.util.UUID;

import com.arcussmarthome.common.subsystem.lawnngarden.LawnNGardenFixtures;
import com.arcussmarthome.common.subsystem.lawnngarden.model.schedules.Schedule.Status;
import com.arcussmarthome.common.subsystem.lawnngarden.model.schedules.SimpleScheduleOperationsTestCase;
import com.arcussmarthome.messages.address.Address;

public class TestIntervalSchedule_Operations extends SimpleScheduleOperationsTestCase<IntervalSchedule, IntervalScheduleEvent> {

   @Override
   protected IntervalSchedule createSchedule() {
      Calendar cal = LawnNGardenFixtures.createCalendar(1, 0, 0);
      return IntervalSchedule.builder()
            .withController(Address.platformDriverAddress(UUID.randomUUID()))
            .withDays(2)
            .withStartDate(cal.getTime())
            .withStatus(Status.APPLIED)
            .build();
   }

   @Override
   protected int defaultOpCount() {
      return 3;
   }
}

