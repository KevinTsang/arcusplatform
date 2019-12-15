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
package com.arcussmarthome.core.messaging.kafka;

import java.util.Map;

import org.apache.kafka.common.serialization.Serializer;

import com.arcussmarthome.platform.partition.PlatformPartition;
import com.arcussmarthome.platform.partition.io.PlatformPartitionSerializer;

import kafka.utils.VerifiableProperties;

public class KafkaPlatformPartitionSerializer implements Serializer<PlatformPartition> {
	public static KafkaPlatformPartitionSerializer instance() {
		return Holder.Instance;
	}

	private final PlatformPartitionSerializer delegate = new PlatformPartitionSerializer();

	public KafkaPlatformPartitionSerializer() { }

	public KafkaPlatformPartitionSerializer(VerifiableProperties props) {
	}

	@Override
	public void configure(Map<String, ?> configs, boolean isKey) {
		// no-op
	}

	@Override
	public byte[] serialize(String topic, PlatformPartition data) {
		if (data == null) {
			return new byte[] {};
		}
		return delegate.serialize(data);
	}

	@Override
	public void close() {
		// no-op
	}

	private static final class Holder {
		private static final KafkaPlatformPartitionSerializer Instance = new KafkaPlatformPartitionSerializer();
	}
}

