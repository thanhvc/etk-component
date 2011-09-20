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

import java.util.Collection;
import java.util.Iterator;

import org.etk.kernel.test.spi.AbstractApplicationTest;
import org.etk.sandbox.entity.plugins.config.DatasourceConfig;
import org.etk.sandbox.entity.plugins.config.FieldTypeConfig;
import org.etk.sandbox.entity.plugins.jdbc.DatabaseManager;
import org.etk.sandbox.entity.plugins.model.configuration.ConfigurationUnmarshaller;
import org.etk.sandbox.entity.plugins.model.xml.Entity;
import org.etk.sandbox.entity.plugins.model.xml.Field;
import org.etk.sandbox.entity.plugins.model.xml.FieldType;
import org.etk.sandbox.entity.plugins.model.xml.FieldTypeModel;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Sep 20, 2011  
 */
public abstract class BaseEntityTestCase extends AbstractApplicationTest {

  protected DatasourceConfig datasourceInfo = null;
  protected FieldTypeConfig fieldTypeConfig = null;
  protected DatabaseManager dbManager = null;
  protected ConfigurationUnmarshaller unmarshaller;
  
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
    dbManager = (DatabaseManager) getContainer().getComponentInstanceOfType(DatabaseManager.class);
    unmarshaller = new ConfigurationUnmarshaller();
  }

  @Override
  protected void tearDown() throws Exception {
    datasourceInfo = null;
    fieldTypeConfig = null;
    dbManager = null;
    unmarshaller = null;
  }
  
  protected void validateFieldType(Collection<FieldTypeModel> fieldTypeModelList) throws Exception {
    assertEquals("fieldTypeModelList's size must be equal 1. ", 1, fieldTypeModelList.size());
    for(FieldTypeModel model : fieldTypeModelList) {
      assertEquals("fieldTypeList's size must be equal 31. ", 31, model.getFieldTypeSize());
      dumpFieldType(model.getFieldTypeIterator());
    }
  }

  protected void dumpFieldType(Iterator<FieldType> fieldTypeIterator) {
    while(fieldTypeIterator.hasNext()) {
      FieldType type = fieldTypeIterator.next();
      log.info("type = '" + type.getType() + "' sql-type = '" + type.getSqlType() + "' java-type = '" + type.getJavaType() + "'");
    }
  }
  
  protected void dumpEntities(Collection<Entity> entities) {
    for (Entity entity : entities) {
      log.info(entity.toString());
      log.info("Entity's fields = " + entity.getFields().size());
      dumpFields(entity.getFields());
    }
  }
  
  protected void dumpFields(Collection<Field> fields) {
    for (Field field : fields) {
      log.info("Field name = '" + field.getName() + "' Type = '" + field.getType() + "' Column = '" + field.getColName() + "' isPK = '" + field.getIsPk() +"'");
    }
  }
}
