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
import java.util.Set;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.inject.Inject;
import com.arcussmarthome.prodcat.pairing.serializer.Customization;
import com.arcussmarthome.prodcat.pairing.serializer.CustomizationType;
import com.arcussmarthome.test.IrisMockTestCase;

/**
 * This test uses a test pairing_catalog.xml file so that we can test additional functionalities
 * @author daniellepatrow
 *
 */
public class TestPairingCatalogManagerMoreTests extends IrisMockTestCase {

	@Inject
	private PairingCatalogConfig config;
	
	private PairingCatalogManager manager;
	
	
	
	@Override
	protected Set<String> configs() {
		Set<String> configs = super.configs();
		configs.add("src/test/resources/test-prodcat.properties");
		return configs;
	}

	@Before
	public void init() {
		manager = new PairingCatalogManager(config);
	}
	
	@Test
	public void testGetCatalog() {
		ListMultimap<CustomizationType, Customization> customizationMap = loadCustomizationMap();
		
		//only one CustomizationType.NAME customization
		assertOnlyOne(customizationMap.get(CustomizationType.NAME));		
		assertOnlyOne(customizationMap.get(CustomizationType.FAVORITE));
		assertOnlyOne(customizationMap.get(CustomizationType.SECURITY_MODE));
		assertOnlyOne(customizationMap.get(CustomizationType.UNCERTIFIED));
		
		//assertOnlyOne(customizationMap.get(CustomizationType.BUTTON_ASSIGNMENT));
		assertOneOrMore(customizationMap.get(CustomizationType.CONTACT_USE_HINT));
		assertOneOrMore(customizationMap.get(CustomizationType.HALO_ROOM));
		//assertOneOrMore(customizationMap.get(CustomizationType.STATE_COUNTY_SELECT));
		assertOneOrMore(customizationMap.get(CustomizationType.INFO));
		assertOneOrMore(customizationMap.get(CustomizationType.MULTI_BUTTON_ASSIGNMENT));
		assertOneOrMore(customizationMap.get(CustomizationType.PRESENCE_ASSIGNMENT));
		assertOneOrMore(customizationMap.get(CustomizationType.RULE));
		assertOneOrMore(customizationMap.get(CustomizationType.SCHEDULE));
		assertOneOrMore(customizationMap.get(CustomizationType.OTA_UPGRADE));
		assertOneOrMore(customizationMap.get(CustomizationType.WATER_HEATER));
		assertOneOrMore(customizationMap.get(CustomizationType.IRRIGATION_ZONE));
		assertOneOrMore(customizationMap.get(CustomizationType.MULTI_IRRIGATION_ZONE));
	}
	
	@Test
	public void testInspect() {
		ListMultimap<CustomizationType, Customization> customizationMap = loadCustomizationMap();
		List<Customization> curCustomizations = customizationMap.get(CustomizationType.INFO);
		for(Customization cur : curCustomizations) {
			System.out.println(ReflectionToStringBuilder.toString(cur, ToStringStyle.MULTI_LINE_STYLE));
		}
	}
	
	private ListMultimap<CustomizationType, Customization> loadCustomizationMap() {
		PairingCatalog catalog = manager.getCatalog();
		assertNotNull(catalog);
		List<Customization> customizations = catalog.getCustomizations();
		ArrayListMultimap<CustomizationType, Customization> customizationMap = ArrayListMultimap.create();
		for(Customization cur : customizations) {
			customizationMap.put(cur.getType(), cur);
		}
		return customizationMap;
	}
	
	private void assertOnlyOne(List<Customization> curCustomizationList) {
		assertNotNull(curCustomizationList);
		assertEquals(1, curCustomizationList.size());
	}
	
	private void assertOneOrMore(List<Customization> curCustomizationList) {
		assertNotNull(curCustomizationList);
		assertTrue(curCustomizationList.size() > 0);
	}
}

