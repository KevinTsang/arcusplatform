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
package com.arcussmarthome.prodcat.customization;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.arcussmarthome.prodcat.pairing.serializer.Customization;
import com.arcussmarthome.prodcat.pairing.serializer.Customizations;

public class PairingCatalog {
	private final List<Customization> customizations;
	
	public PairingCatalog(Customizations customizations) {
		// FIXME store metadata as well
		this.customizations = ImmutableList.copyOf(customizations.getCustomization());
	}
	
	public List<Customization> getCustomizations() {
		return customizations;
	}
}

