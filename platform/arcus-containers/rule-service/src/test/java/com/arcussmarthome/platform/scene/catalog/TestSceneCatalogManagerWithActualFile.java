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
package com.arcussmarthome.platform.scene.catalog;

import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.inject.Inject;
import com.arcussmarthome.capability.registry.CapabilityRegistry;
import com.arcussmarthome.capability.registry.CapabilityRegistryModule;
import com.arcussmarthome.core.dao.PopulationDAO;
import com.arcussmarthome.core.dao.file.PopulationDAOModule;
import com.arcussmarthome.messages.type.Population;
import com.arcussmarthome.platform.scene.SceneConfig;
import com.arcussmarthome.test.IrisMockTestCase;
import com.arcussmarthome.test.Modules;

@Modules({CapabilityRegistryModule.class, PopulationDAOModule.class})
public class TestSceneCatalogManagerWithActualFile extends IrisMockTestCase
{
   @Inject
   private CapabilityRegistry registry;
   @Inject
   private PopulationDAO populationDao;
   
   @Override
   protected Set<String> configs() {
      Set<String> configs = super.configs();
      //configs.add("src/test/resources/test.properties");
      return configs;
   }

   private SceneCatalogManager manager;
   private SceneConfig config;

   @Before
   public void setUp() throws Exception
   {
      super.setUp();
      config = new SceneConfig();
      config.setCatalogPath("classpath:/scene-catalog.xml");
      
      
   }

   @After
   public void tearDown() throws Exception
   {
      super.tearDown();
   }

   @Test
   public void testGetCatalog()
   {      
      manager = new SceneCatalogManager(config, registry, populationDao);
      manager.init();
      
      //Make sure all other population has the same # of entries as general
      SceneCatalog generalCatalog = manager.getCatalog(Population.NAME_GENERAL);
      assertNotNull(generalCatalog);
      populationDao.listPopulations().forEach(curPop -> {
      	SceneCatalog curCatalog = manager.getCatalog(curPop.getName());
      	assertNotNull(curCatalog);
      	generalCatalog.getTemplates().forEach(curTemp -> {
      		SceneTemplate temp = curCatalog.getById(curTemp.getId());
      		assertNotNull(temp);
      	});
      });
      
   }

}

