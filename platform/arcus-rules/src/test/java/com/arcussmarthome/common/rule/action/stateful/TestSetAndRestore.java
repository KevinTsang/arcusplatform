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
package com.arcussmarthome.common.rule.action.stateful;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.Serializable;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.LoggerFactory;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.base.Predicate;
import com.arcussmarthome.common.rule.action.ActionContext;
import com.arcussmarthome.common.rule.event.AttributeValueChangedEvent;
import com.arcussmarthome.common.rule.event.ScheduledEvent;
import com.arcussmarthome.common.rule.simple.SimpleContext;
import com.arcussmarthome.messages.PlatformMessage;
import com.arcussmarthome.messages.address.Address;
import com.arcussmarthome.messages.capability.Capability;
import com.arcussmarthome.messages.capability.DeviceCapability;
import com.arcussmarthome.messages.capability.MotionCapability;
import com.arcussmarthome.messages.capability.SwitchCapability;
import com.arcussmarthome.messages.model.Model;
import com.arcussmarthome.messages.model.SimpleModel;
import com.arcussmarthome.model.query.expression.ExpressionCompiler;

public class TestSetAndRestore {

   SimpleContext context;
   Address source;
   Model switchDevice;
   Model motionDevice;
 

   @Before
   public void setUp() throws Exception {
      this.source = Address.platformService(UUID.randomUUID(), "rule");
      this.switchDevice = createDeviceModel();
      switchDevice.setAttribute(SwitchCapability.ATTR_STATE, SwitchCapability.STATE_OFF);
      
      this.motionDevice = createDeviceModel();
      motionDevice.setAttribute(MotionCapability.ATTR_MOTION, MotionCapability.MOTION_NONE);
      
      this.context = new SimpleContext(UUID.randomUUID(), this.source, LoggerFactory.getLogger(TestSetAndRestore.class));
      context.putModel(switchDevice);
      context.putModel(motionDevice);
   }
   
   
   protected SimpleModel createDeviceModel() {
      UUID id = UUID.randomUUID();
      String type = DeviceCapability.NAMESPACE;
      SimpleModel model = new SimpleModel();
      model.setAttribute(Capability.ATTR_ID, id.toString());
      model.setAttribute(Capability.ATTR_TYPE, type);
      model.setAttribute(Capability.ATTR_ADDRESS, Address.platformDriverAddress(id).getRepresentation());
      return model;
   }
   
   /**
    * Delay of 0,
    * No condition,
    * execute should set the value and return IDLE
    * @throws Exception
    */
   @Test
   public void testNoDelayNoCondition() throws Exception {
	  SetAndRestore curAction = createAction(0, TimeUnit.MILLISECONDS, null);	      
      assertEquals(ActionState.FIRING, curAction.execute(context));
      PlatformMessage msg = context.getMessages().poll();
      assertNotNull(msg);
      assertEquals(ActionState.IDLE, curAction.keepFiring(context, new ScheduledEvent(), true));      
   }
   
   /**
    * - Delay of 1000 ms
    * - No condition
    * - execute should set the value, schedule an event, return FIRING
    * - keepFiring with the event that was scheduled should reset the value, return IDLE
    * @throws Exception
    */
   @Test
   public void test1000DelayNoCondition() throws Exception {
	   SetAndRestore curAction = createAction(1000, TimeUnit.MILLISECONDS, null);
	   
      assertEquals(ActionState.FIRING, curAction.execute(context));
      PlatformMessage msg = context.getMessages().poll();
      assertNotNull(msg);
      ScheduledEvent event = context.getEvents().poll();
      assertNotNull(event);
      assertEquals(ActionState.FIRING, curAction.keepFiring(context, new ScheduledEvent(), true));
      assertEquals(ActionState.IDLE, curAction.keepFiring(context, event, true));
      msg = context.getMessages().poll();
      assertNotNull(msg);
   }
   
   /**
    * - Delay of 1000 ms
    * - Condition which is initially true
    * - execute should set the value, schedule an event, return FIRING
    * - a valuechange which makes the condition false should cancel the event and return FIRING
    * @throws Exception
    */
   
   @Test
   public void test1000DelayConditionInitiallyTrue() throws Exception {
	   SetAndRestore curAction = createAction(1000, TimeUnit.MILLISECONDS, createConditionQuery(SwitchCapability.STATE_OFF));
	   
      assertEquals(ActionState.FIRING, curAction.execute(context));
      PlatformMessage msg = context.getMessages().poll();
      assertNotNull(msg);
      ScheduledEvent event = context.getEvents().poll();
      assertNotNull(event);
      assertEquals(ActionState.FIRING, curAction.keepFiring(context, new ScheduledEvent(), true));
      
      switchDevice.setAttribute(SwitchCapability.ATTR_STATE, SwitchCapability.STATE_ON);      
      AttributeValueChangedEvent valueChangeEvent = AttributeValueChangedEvent.create(
              switchDevice.getAddress(),
              SwitchCapability.ATTR_STATE, 
              SwitchCapability.STATE_ON, 
              SwitchCapability.STATE_OFF
        );
      
      assertEquals(ActionState.FIRING, curAction.keepFiring(context, valueChangeEvent, true));
      event = context.getEvents().poll();
      assertEquals(null, event);
      
      
   }
   
   /**
    * - Delay of 1000 ms
    * - Condition which is initially false
    * -> execute should set the value, return FIRING (note no event scheduled)
    * -> modify the environment to make the condition true, and pass the associated valuechange into keepFiring, an event should be scheduled and return FIRING
    * -> send the scheduled event to keepFiring and the original value should be reset and return IDLE
    * 
    */
   @Test
   public void test1000DelayConditionInitiallyFalse() throws Exception {
	   SetAndRestore curAction = createAction(1000, TimeUnit.MILLISECONDS, createConditionQuery(SwitchCapability.STATE_ON));
	   
      assertEquals(ActionState.FIRING, curAction.execute(context));
      PlatformMessage msg = context.getMessages().poll();
      assertNotNull(msg);
      ScheduledEvent event = context.getEvents().poll();
      assertEquals(null, event);
      assertEquals(ActionState.FIRING, curAction.keepFiring(context, new ScheduledEvent(), true));
      
      switchDevice.setAttribute(SwitchCapability.ATTR_STATE, SwitchCapability.STATE_ON);      
      AttributeValueChangedEvent valueChangeEvent = AttributeValueChangedEvent.create(
              switchDevice.getAddress(),
              SwitchCapability.ATTR_STATE, 
              SwitchCapability.STATE_ON, 
              SwitchCapability.STATE_OFF
        );
      
      assertEquals(ActionState.FIRING, curAction.keepFiring(context, valueChangeEvent, true));
      event = context.getEvents().poll();
      assertNotNull(event);
      
      assertEquals(ActionState.FIRING, curAction.keepFiring(context, new ScheduledEvent(), true));
      
      assertEquals(ActionState.IDLE, curAction.keepFiring(context, event, true));
      msg = context.getMessages().poll();
      assertNotNull(msg);
   }
   
   /**
    * - Delay of 1000 ms
    * - Condition which is initially true
    * -> execute should set the value, schedule the event and return FIRING
    * -> valuechange on the attribute being edited should result in the event being cancelled and keepFiring returning IDLE
    * 
    */
   @Test
   public void test1000DelayConditionInitiallyTrueManualAttributeUpdate() throws Exception {
	   SetAndRestore curAction = createAction(1000, TimeUnit.MILLISECONDS, createConditionQuery(SwitchCapability.STATE_OFF));
	   
      assertEquals(ActionState.FIRING, curAction.execute(context));
      PlatformMessage msg = context.getMessages().poll();
      assertNotNull(msg);
      ScheduledEvent event = context.getEvents().poll();
      assertNotNull(event);
      assertEquals(ActionState.FIRING, curAction.keepFiring(context, new ScheduledEvent(), true));
      
      switchDevice.setAttribute(SwitchCapability.ATTR_STATE, SwitchCapability.STATE_ON);      
      AttributeValueChangedEvent valueChangeEvent = AttributeValueChangedEvent.create(
              motionDevice.getAddress(),
              MotionCapability.ATTR_MOTION, 
              MotionCapability.MOTION_NONE, 
              MotionCapability.MOTION_DETECTED
        );
      
      assertEquals(ActionState.IDLE, curAction.keepFiring(context, valueChangeEvent, true));
      event = context.getEvents().poll();
      assertEquals(null, event);
      
      msg = context.getMessages().poll();
      assertEquals(null, msg);
   }
   
   
   
   
     
   
   protected SetAndRestore createAction(int duration, TimeUnit unit, String conditionStr) {
	   
	   Predicate<Model> conditionQuery = (Predicate<Model>) (StringUtils.isNotBlank(conditionStr)?ExpressionCompiler.compile(conditionStr):com.google.common.base.Predicates.alwaysTrue());
	   SetAndRestore curAction = 
			    new SetAndRestore(
			        new MockFunction(Functions.constant(motionDevice.getAddress())),
			        new MockFunction(Functions.constant(MotionCapability.ATTR_MOTION)),
			        new MockFunction(Functions.constant(MotionCapability.MOTION_DETECTED)),			        
			        duration,
			        conditionQuery			        
			       );	  
	  context.setVariable("motion", motionDevice.getAddress().getRepresentation());
	  context.setVariable("switch", switchDevice.getAddress().getRepresentation());
	  context.setVariable("state", MotionCapability.MOTION_DETECTED);
	  return curAction;
   }
   
   private String createConditionQuery(String state) {
	   return "base:address == '"+ switchDevice.getAddress().getRepresentation() +"' AND swit:state == '"+ state +"'";
   }
   
   
   private static class MockFunction<O> implements Function<ActionContext, O>, Serializable {
	private static final long serialVersionUID = 6155405544061221102L;
	private final Function<Object, O> function;

	   public MockFunction(Function<Object, O> func) {
	      this.function = func;
	   }
	   
		@Override
		public O apply(ActionContext input) {
			return (O) function.apply(input);
		}
	   
   }
   
   
   
   
}

