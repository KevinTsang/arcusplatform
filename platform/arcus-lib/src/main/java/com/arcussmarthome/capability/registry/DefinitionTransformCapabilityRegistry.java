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
package com.arcussmarthome.capability.registry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jdt.annotation.Nullable;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.arcussmarthome.Utils;
import com.arcussmarthome.capability.Capabilities;
import com.arcussmarthome.capability.attribute.Attributes;
import com.arcussmarthome.capability.attribute.ChainedAttributeDefinitionBuilder;
import com.arcussmarthome.capability.builder.CapabilityDefinitionBuilder;
import com.arcussmarthome.capability.builder.CapabilityDefinitionBuilder.CapabilityCommandBuilder;
import com.arcussmarthome.capability.builder.CapabilityDefinitionBuilder.CapabilityEventBuilder;
import com.arcussmarthome.capability.definition.DefinitionRegistry;
import com.arcussmarthome.capability.definition.MethodDefinition;
import com.arcussmarthome.capability.definition.ParameterDefinition;
import com.arcussmarthome.device.attributes.AttributeKey;
import com.arcussmarthome.device.model.AttributeDefinition;
import com.arcussmarthome.device.model.CapabilityDefinition;
import com.arcussmarthome.device.model.CommandDefinition;
import com.arcussmarthome.device.model.EventDefinition;
import com.arcussmarthome.model.type.AnyType;
import com.arcussmarthome.model.type.AttributeType;
import com.arcussmarthome.model.type.BooleanType;
import com.arcussmarthome.model.type.ByteType;
import com.arcussmarthome.model.type.DoubleType;
import com.arcussmarthome.model.type.EnumType;
import com.arcussmarthome.model.type.IntType;
import com.arcussmarthome.model.type.ListType;
import com.arcussmarthome.model.type.LongType;
import com.arcussmarthome.model.type.MapType;
import com.arcussmarthome.model.type.SetType;
import com.arcussmarthome.model.type.StringType;
import com.arcussmarthome.model.type.TimestampType;
import com.arcussmarthome.model.type.VoidType;

@Singleton
public class DefinitionTransformCapabilityRegistry implements CapabilityRegistry {
   private final List<CapabilityDefinition> capabilities;
	private final Map<String, CapabilityDefinition> capabilitiesByName;
	private final Map<String, CapabilityDefinition> capabilitiesByNamespace;

	@Inject
	public DefinitionTransformCapabilityRegistry(DefinitionRegistry registry) {
		capabilitiesByName = new HashMap<String, CapabilityDefinition>();
		capabilitiesByNamespace = new HashMap<String, CapabilityDefinition>();
		capabilities = new ArrayList<>();
		ingest(registry.getCapabilities());
	}

   @Override
   @Nullable
   public AttributeDefinition getAttributeDefinition(String name) {
      if(StringUtils.isEmpty(name)) {
         return null;
      }
      String namespace = Utils.getNamespace(name);
      CapabilityDefinition definition = getCapabilityDefinitionByNamespace(namespace);
      if(definition == null) {
         return null;
      }
      return definition.getAttributes().get(name);
   }

	@Override
	public CapabilityDefinition getCapabilityDefinitionByName(String name) {
		return capabilitiesByName.get(name);
	}

	@Override
	public CapabilityDefinition getCapabilityDefinitionByNamespace(String namespace) {
		return capabilitiesByNamespace.get(namespace);
	}

	@Override
	public List<CapabilityDefinition> listCapabilityDefinitions() {
		return capabilities;
	}

	private void ingest(Collection<com.arcussmarthome.capability.definition.CapabilityDefinition> definitions) {
	   definitions
	      .forEach((def) -> {
	         CapabilityDefinition value = transform(def);
	         capabilities.add(value);
	         capabilitiesByName.put(value.getCapabilityName(), value);
	         capabilitiesByNamespace.put(value.getNamespace(), value);
	      });
	}

   private CapabilityDefinition transform(com.arcussmarthome.capability.definition.CapabilityDefinition def) {
      CapabilityDefinitionBuilder builder =
            Capabilities
               .define()
               .withDescription(def.getDescription())
               .withName(def.getName())
               .withNamespace(def.getNamespace())
               .withEnhances(def.getEnhances())
               ;
      for(com.arcussmarthome.capability.definition.AttributeDefinition attribute: def.getAttributes()) {
         transform(def.getNamespace(), builder, attribute);
      }
      for(com.arcussmarthome.capability.definition.MethodDefinition method: def.getMethods()) {
         transform(builder, method);
      }
      for(com.arcussmarthome.capability.definition.EventDefinition event: def.getEvents()) {
         transform(builder, event);
      }
      return builder.create();
   }

   private AttributeDefinition transform(String namespace, CapabilityDefinitionBuilder builder, com.arcussmarthome.capability.definition.AttributeDefinition attribute) {
//      Capability

      ChainedAttributeDefinitionBuilder<CapabilityDefinitionBuilder> attrBuilder =
         builder
            .buildAttribute(
               AttributeKey
                  .createType(namespace + ":" + attribute.getName(), attribute.getType().getJavaType())
             )
             .withAttributeType(transform(attribute.getType()))
             .withDescription(attribute.getDescription())
             .withUnits(attribute.getUnit());

      if(attribute.isOptional()) {
         attrBuilder.optional();
      } else {
         attrBuilder.required();
      }

      if(attribute.isReadable()) {
         if(attribute.isWritable()) {
            attrBuilder.readWrite();
         } else {
            attrBuilder.readOnly();
         }
      } else if(attribute.isWritable()) {
         attrBuilder.writeOnly();
      }

      return attrBuilder.addAndGet();
   }

   private CommandDefinition transform(CapabilityDefinitionBuilder builder, MethodDefinition method) {
      CapabilityCommandBuilder commandBuilder =
            builder
               .buildCommand(method.getName())
               .withDescription(method.getDescription())
               ;
      for(ParameterDefinition parameter: method.getParameters()) {
         AttributeDefinition ad = transform(parameter);
         commandBuilder.addInputArgument(ad);
      }
      for(ParameterDefinition parameter: method.getReturnValues()) {
         AttributeDefinition ad = transform(parameter);
         commandBuilder.addReturnParameter(ad);
      }
      return commandBuilder.addAndGet();
   }

   private EventDefinition transform(CapabilityDefinitionBuilder builder, com.arcussmarthome.capability.definition.EventDefinition event) {
      CapabilityEventBuilder eventBuilder =
            builder.buildEvent(event.getName());
      for(ParameterDefinition parameter: event.getParameters()) {
         AttributeDefinition ad = transform(parameter);
         eventBuilder.addAttribute(ad);
      }

      return eventBuilder.addAndGet();
   }

   private AttributeDefinition transform(ParameterDefinition parameter) {
      return
         Attributes
            .build(AttributeKey.createType(parameter.getName(), parameter.getType().getJavaType()))
            .withAttributeType(transform(parameter.getType()))
            .withDescription(parameter.getDescription())
            .create()
            ;
   }

   private AttributeType transform(com.arcussmarthome.capability.definition.AttributeType type) {
      switch(type.getRawType()) {
      case BOOLEAN:
         return BooleanType.INSTANCE;
      case BYTE:
         return ByteType.INSTANCE;
      case INT:
         return IntType.INSTANCE;
      case LONG:
         return LongType.INSTANCE;
      case DOUBLE:
         return DoubleType.INSTANCE;
      case STRING:
         return StringType.INSTANCE;
      case TIMESTAMP:
         return TimestampType.INSTANCE;
      case VOID:
         return VoidType.INSTANCE;
      case ANY:
         return AnyType.INSTANCE;

      case ENUM:
         return new EnumType(type.asEnum().getValues());

      case MAP:
         return new MapType(transform(type.asCollection().getContainedType()));
      case LIST:
         return new ListType(transform(type.asCollection().getContainedType()));
      case SET:
         return new SetType(transform(type.asCollection().getContainedType()));

      // TODO better support here
      case OBJECT:
      case ATTRIBUTES:
         return new MapType(AnyType.INSTANCE);

      default:
         throw new IllegalArgumentException("Unrecognized type " + type);
      }
   }

}

