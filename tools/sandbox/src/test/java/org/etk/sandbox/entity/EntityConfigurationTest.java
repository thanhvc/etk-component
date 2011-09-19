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
package org.etk.sandbox.entity;

import java.net.URL;
import java.util.Collection;
import java.util.Iterator;

import org.etk.common.logging.Logger;
import org.etk.kernel.test.spi.AbstractApplicationTest;
import org.etk.sandbox.entity.plugins.config.DatasourceConfig;
import org.etk.sandbox.entity.plugins.config.FieldTypeConfig;
import org.etk.sandbox.entity.plugins.model.configuration.ConfigurationUnmarshaller;
import org.etk.sandbox.entity.plugins.model.xml.Configuration;
import org.etk.sandbox.entity.plugins.model.xml.FieldType;
import org.etk.sandbox.entity.plugins.model.xml.FieldTypeModel;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Sep 16, 2011  
 */
public class EntityConfigurationTest extends AbstractApplicationTest {

  private Logger log = Logger.getLogger(EntityConfigurationTest.class);

  private DatasourceConfig datasourceInfo = null;
  private FieldTypeConfig fieldTypeConfig = null;
  private ConfigurationUnmarshaller unmarshaller;
  
  @Override
  protected void end() {
    end(false);
  }

  protected void end(boolean save) {
    super.end();
  }

  @Override
  protected void setUp() throws Exception {
    datasourceInfo = (DatasourceConfig) getContainer().getComponentInstanceOfType(DatasourceConfig.class);
    fieldTypeConfig = (FieldTypeConfig) getContainer().getComponentInstanceOfType(FieldTypeConfig.class);
    unmarshaller = new ConfigurationUnmarshaller();
  }

  @Override
  protected void tearDown() throws Exception {
    datasourceInfo = null;
  }
  
  public void testPostgresDatasource() throws Exception {
    assertEquals("Db-conection must not be null.", "postgresql-connection", datasourceInfo.dbConnection);
  }
  
  public void testFieldTypeReader() throws Exception {
    assertEquals("FieldTypeConfig must not be null.", "entityconf/fieldtype/fieldtypepostgres.xml", fieldTypeConfig.getFieldTypeXMLFile());
    
    ClassLoader cl = Thread.currentThread().getContextClassLoader();
    URL entityURL = cl.getResource(fieldTypeConfig.getFieldTypeXMLFile());
    Configuration conf = unmarshaller.unmarshall(entityURL);
    
    assertNotNull(conf.getFieldTypeList());
    fieldTypeConfig.setFieldTypeModelList(conf.getFieldTypeList());
    
    assertTrue(fieldTypeConfig.getFieldTypeModelList().size() > 0);
    
    validateFieldType(fieldTypeConfig.getFieldTypeModelList());
    
    //Unmarshaller the EntityModel
    URL entityModelURL = cl.getResource(datasourceInfo.entityModelFile);
    Configuration entityModelConf = unmarshaller.unmarshall(entityModelURL);
    
    
  }
  
  private void validateFieldType(Collection<FieldTypeModel> fieldTypeModelList) throws Exception {
    assertEquals("fieldTypeModelList's size must be equal 1. ", 1, fieldTypeModelList.size());
    for(FieldTypeModel model : fieldTypeModelList) {
      assertEquals("fieldTypeList's size must be equal 31. ", 31, model.getFieldTypeSize());
      dumpFieldType(model.getFieldTypeIterator());
    }
  }

  private void dumpFieldType(Iterator<FieldType> fieldTypeIterator) {
    while(fieldTypeIterator.hasNext()) {
      FieldType type = fieldTypeIterator.next();
      log.info("type = '" + type.getType() + "' sql-type = '" + type.getSqlType() + "' java-type = '" + type.getJavaType() + "'");
    }
  }
  /*
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
    
    
  }*/
}
