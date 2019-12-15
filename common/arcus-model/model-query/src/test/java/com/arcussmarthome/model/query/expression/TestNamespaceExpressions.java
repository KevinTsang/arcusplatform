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
package com.arcussmarthome.model.query.expression;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.arcussmarthome.messages.capability.Capability;
import com.arcussmarthome.messages.model.Model;
import com.arcussmarthome.messages.model.SimpleModel;

/**
 * 
 */
public class TestNamespaceExpressions {

   @Test
   public void testIsA() {
      Predicate<Model> predicate = ExpressionCompiler.compile("is test");
      
      assertFalse(predicate.apply(new SimpleModel(ImmutableMap.<String, Object>of())));
      assertFalse(predicate.apply(new SimpleModel(ImmutableMap.<String, Object>of(Capability.ATTR_CAPS, ImmutableSet.of()))));
      assertFalse(predicate.apply(new SimpleModel(ImmutableMap.<String, Object>of(Capability.ATTR_CAPS, ImmutableSet.of("base")))));
      assertTrue(predicate.apply(new SimpleModel(ImmutableMap.<String, Object>of(Capability.ATTR_CAPS, ImmutableSet.of("base", "test")))));
   }

   @Test
   public void testHasA() {
      Predicate<Model> predicate = ExpressionCompiler.compile("has test");
      
      assertFalse(predicate.apply(new SimpleModel(ImmutableMap.<String, Object>of())));
      assertFalse(predicate.apply(new SimpleModel(ImmutableMap.<String, Object>of(Capability.ATTR_INSTANCES, ImmutableMap.of()))));
      assertFalse(predicate.apply(new SimpleModel(ImmutableMap.<String, Object>of(Capability.ATTR_INSTANCES, ImmutableMap.of("i1", ImmutableSet.of("base"))))));
      assertTrue(predicate.apply(new SimpleModel(ImmutableMap.<String, Object>of(Capability.ATTR_INSTANCES, ImmutableMap.of("i1", ImmutableSet.of("base", "test"))))));
   }

}

