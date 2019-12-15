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
package com.arcussmarthome.common.subsystem.lawnngarden.model.schedules.odd;

import java.util.List;
import java.util.Map;

import com.arcussmarthome.common.subsystem.lawnngarden.model.schedules.ScheduleEvent;
import com.arcussmarthome.common.subsystem.lawnngarden.model.schedules.ScheduleMode;
import com.arcussmarthome.common.subsystem.lawnngarden.model.schedules.SimpleScheduleEvent;
import com.arcussmarthome.common.subsystem.lawnngarden.model.schedules.Transition;
import com.arcussmarthome.common.time.TimeOfDay;
import com.arcussmarthome.messages.capability.IrrigationSchedulableCapability;

public class OddScheduleEvent extends SimpleScheduleEvent<OddScheduleEvent> {

   private OddScheduleEvent(String eventId, TimeOfDay timeOfDay, List<Transition> transitions, EventStatus status) {
      super(eventId, timeOfDay, transitions, ScheduleMode.ODD, status);
   }

   @Override
   protected OddScheduleEvent.Builder createEventBuilder(OddScheduleEvent event) {
      return builder(event);
   }

   @Override
   protected String setScheduleMessage() {
      return IrrigationSchedulableCapability.SetEvenOddScheduleRequest.NAME;
   }

   public static class Builder extends ScheduleEvent.Builder<Builder, OddScheduleEvent> {
      private Builder() {
      }

      @Override
      protected OddScheduleEvent doBuild() {
         return new OddScheduleEvent(eventId, timeOfDay, sortTransitions(), status);
      }
   }

   public static Builder builder() {
      return new Builder();
   }

   public static Builder builder(OddScheduleEvent event) {
      Builder builder = builder();
      builder.copyFrom(event);
      return builder;
   }

   @SuppressWarnings("serial")
   private static class TypeHandler extends ScheduleEvent.TypeHandler<Builder, OddScheduleEvent> {

      private TypeHandler() {
         super(OddScheduleEvent.class);
      }

      @Override
      protected void populate(Builder builder, Map<String, Object> map) {
         // no op
      }

      @Override
      protected Builder getBuilder() {
         return builder();
      }

   }

   public static TypeHandler typeHandler() {
      return new TypeHandler();
   }
}

