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
package com.arcussmarthome.platform.rule.environment;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.arcussmarthome.bootstrap.ServiceLocator;
import com.arcussmarthome.capability.definition.DefinitionRegistry;
import com.arcussmarthome.common.rule.event.AttributeValueChangedEvent;
import com.arcussmarthome.common.rule.event.ModelAddedEvent;
import com.arcussmarthome.common.rule.event.ModelRemovedEvent;
import com.arcussmarthome.common.rule.event.RuleEvent;
import com.arcussmarthome.messages.address.Address;
import com.arcussmarthome.messages.capability.AccountCapability;
import com.arcussmarthome.messages.capability.DeviceCapability;
import com.arcussmarthome.messages.capability.HubCapability;
import com.arcussmarthome.messages.capability.PersonCapability;
import com.arcussmarthome.messages.capability.PlaceCapability;
import com.arcussmarthome.messages.capability.SceneCapability;
import com.arcussmarthome.messages.capability.SubsystemCapability;
import com.arcussmarthome.messages.model.Model;
import com.arcussmarthome.messages.model.SimpleModelStore;
import com.arcussmarthome.util.Subscription;

/**
 */
// TODO deprecate custom rule events and remove the custom RuleModelStore
public class RuleModelStore extends SimpleModelStore {
   private static final Logger logger = LoggerFactory.getLogger(RuleModelStore.class);
   
   public static final Set<String> TRACKED_TYPES =
         ImmutableSet.<String>builder()
            .add(AccountCapability.NAMESPACE)
            .add(DeviceCapability.NAMESPACE)
            .add(PlaceCapability.NAMESPACE)
            .add(PersonCapability.NAMESPACE)
            .add(HubCapability.NAMESPACE)
            .add(SceneCapability.NAMESPACE)
            .add(SubsystemCapability.NAMESPACE)
            .build();
            
   private Set<Consumer<RuleEvent>> listeners = new LinkedHashSet<>();
   private DefinitionRegistry registry;
   
   public RuleModelStore() {
      setTrackedTypes(TRACKED_TYPES);
   }
   
   public DefinitionRegistry getRegistry() {
      if(registry == null) {
         this.registry = ServiceLocator.getInstance(DefinitionRegistry.class);
      }
      return registry;
   }
   
   public void setRegistry(DefinitionRegistry registry) {
      this.registry = registry;
   }
   
   public Subscription addListener(Consumer<RuleEvent> listener) {
      Preconditions.checkNotNull(listener, "listener may not be null");
      listeners.add(listener);
      return () -> { listeners.remove(listener); };
   }

   protected void fire(RuleEvent event) {
      for(Consumer<RuleEvent> c: listeners) {
         try {
            c.accept(event);
         }
         catch(Exception e) {
            logger.debug("Error notifying rule of event [{}]", event);
         }
      }
   }
   
   protected void fireModelAdded(Address source) {
      if(listeners.isEmpty()) {
         return;
      }
      
      fire(ModelAddedEvent.create(source));
   }
   
   protected void fireModelRemoved(Model source) {
      if(listeners.isEmpty()) {
         return;
      }
      
      fire(ModelRemovedEvent.create(source));
   }
   
   protected void fireAttributeValueChanged(Address source, String attributeName, Object newValue, Object oldValue) {
      if(listeners.isEmpty()) {
         return;
      }
      
      fire(AttributeValueChangedEvent.create(source, attributeName, newValue, oldValue));
   }
   
}

