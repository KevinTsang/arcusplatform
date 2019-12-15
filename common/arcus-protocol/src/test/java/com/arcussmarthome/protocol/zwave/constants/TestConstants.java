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
package com.arcussmarthome.protocol.zwave.constants;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestConstants {

	@Test
	public void test() {
		String jasco 	= ZWaveManufactures.get(0x0063);
		int jascoId 	= ZWaveManufactures.get("Jasco Products");
		assertTrue(jasco.equals("Jasco Products"));
		assertTrue(jascoId == 0x0063);
	}

}

