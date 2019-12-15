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
package com.arcussmarthome.platform.partition.cluster;

import java.util.Set;

import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.OptionalBinder;
import com.arcussmarthome.bootstrap.guice.AbstractIrisModule;
import com.arcussmarthome.platform.cluster.ClusterModule;
import com.arcussmarthome.platform.cluster.ClusterServiceListener;
import com.arcussmarthome.platform.partition.PartitionListener;
import com.arcussmarthome.platform.partition.Partitioner;
import com.netflix.governator.annotations.Modules;

@Modules(include = { ClusterModule.class })
public class ClusteredPartitionModule extends AbstractIrisModule {

   @Override
   protected void configure() {
      bind(Partitioner.class).to(DynamicPartitioner.class);
      bindSetOf(ClusterServiceListener.class).addBinding().to(DynamicPartitioner.class);
      OptionalBinder.newOptionalBinder(binder(), new TypeLiteral<Set<PartitionListener>>() {});
   }

}

