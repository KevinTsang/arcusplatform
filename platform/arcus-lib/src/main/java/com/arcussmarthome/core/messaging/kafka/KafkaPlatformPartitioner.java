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
package com.arcussmarthome.core.messaging.kafka;

import java.util.Map;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;

import com.google.inject.Singleton;
import com.arcussmarthome.platform.partition.PlatformPartition;

/**
 * 
 */
@Singleton
public class KafkaPlatformPartitioner implements Partitioner {

	public KafkaPlatformPartitioner() {
	}

	@Override
	public void configure(Map<String, ?> configs) {
		// no-op
	}

	@Override
	public int partition(String topic, Object partition, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
		return ((PlatformPartition) partition).getId() % cluster.partitionCountForTopic(topic);
	}

	@Override
	public void close() {
		// no-op
	}

}

