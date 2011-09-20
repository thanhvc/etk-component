/*
 * Copyright (C) 2003-2011 eXo Platform SAS.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.etk.sandbox.entity.config;

import java.net.URL;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javolution.util.FastList;

import org.etk.common.logging.Logger;
import org.etk.kernel.test.spi.AbstractApplicationTest;
import org.etk.sandbox.entity.BaseEntityTestCase;
import org.etk.sandbox.entity.plugins.config.DatasourceConfig;
import org.etk.sandbox.entity.plugins.config.FieldTypeConfig;
import org.etk.sandbox.entity.plugins.jdbc.DatabaseManager;
import org.etk.sandbox.entity.plugins.model.configuration.ConfigurationUnmarshaller;
import org.etk.sandbox.entity.plugins.model.xml.Configuration;
import org.etk.sandbox.entity.plugins.model.xml.Entity;
import org.etk.sandbox.entity.plugins.model.xml.Field;
import org.etk.sandbox.entity.plugins.model.xml.FieldType;
import org.etk.sandbox.entity.plugins.model.xml.FieldTypeModel;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Sep 16, 2011  
 */
public class EntityConfigurationTest extends BaseEntityTestCase {

  private Logger log = Logger.getLogger(EntityConfigurationTest.class);
  
  @Override
  protected void end() {
    end(false);
  }

  protected void end(boolean save) {
    super.end();
  }

  @Override
  protected void setUp() throws Exception {
    super.setUp();
  }

  @Override
  protected void tearDown() throws Exception {
    super.tearDown();
  }
  
  public void testPostgresDatasource() throws Exception {
    assertEquals("Db-conection must not be null.", "postgresql-connection", datasourceInfo.dbConnection);
  }
  
  /*========================Field Mapping ======================*/
  public void testFieldTypeReader() throws Exception {
    assertEquals("FieldTypeConfig must not be null.", "entityconf/fieldtype/fieldtypepostgres.xml", fieldTypeConfig.getFieldTypeXMLFile());
    
    ClassLoader cl = Thread.currentThread().getContextClassLoader();
    URL entityURL = cl.getResource(fieldTypeConfig.getFieldTypeXMLFile());
    Configuration conf = unmarshaller.unmarshall(entityURL);
    
    assertNotNull(conf.getFieldTypeList());
    fieldTypeConfig.setFieldTypeModelList(conf.getFieldTypeList());
    
    assertTrue(fieldTypeConfig.getFieldTypeModelList().size() > 0);
    
    validateFieldType(fieldTypeConfig.getFieldTypeModelList());
  }
  
  
  /*========================Entity Mapping ======================*/
  public void testEntityMapping() throws Exception {
    //Unmarshaller the FieldTypeConfig
    ClassLoader cl = Thread.currentThread().getContextClassLoader();
    URL fieldTypeURL = cl.getResource(fieldTypeConfig.getFieldTypeXMLFile());
    Configuration fieldTypeConf = unmarshaller.unmarshall(fieldTypeURL);
    //Unmarshaller the EntityModel
    URL entityModelURL = cl.getResource(datasourceInfo.entityModelFile);
    Configuration entityModelConf = unmarshaller.unmarshall(entityModelURL);
    //merge configuration
    entityModelConf.mergeConfiguration(fieldTypeConf);
        
    dumpEntities(entityModelConf.getEntities());
    
  }
  
  
  
  /*========================Database Manager ======================*/
  public void testDatabaseManager() throws Exception {
    
    Configuration entityModelConf = dbManager.getEntityModelConf();
    assertNotNull(entityModelConf);
    
    dumpEntities(entityModelConf.getEntities());
    List<String> messages = FastList.newInstance();
    dbManager.checkDb(entityModelConf.getEntityMap(), messages, true);
    
  }
}
