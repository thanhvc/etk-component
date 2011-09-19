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
package org.etk.sandbox.entity.plugins.config;

import java.util.Collection;
import java.util.Map;

import javolution.util.FastMap;

import org.etk.common.logging.Logger;
import org.etk.kernel.container.ApplicationContainer;
import org.etk.kernel.container.configuration.ConfigurationException;
import org.etk.kernel.container.xml.InitParams;
import org.etk.kernel.container.xml.ValueParam;
import org.etk.sandbox.entity.plugins.model.xml.FieldType;
import org.etk.sandbox.entity.plugins.model.xml.FieldTypeModel;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Sep 16, 2011  
 */
public class FieldTypeConfig {

  private final static Logger logger = Logger.getLogger(FieldTypeConfig.class);
  private String fieldTypeXMLFile = null;
  private String fieldTypeName = null;
  private DatasourceConfig datasourceInfo;
  private Map<String, Collection<FieldTypeModel>> fieldTypeModelMap = FastMap.newInstance();
  
  public static FieldTypeConfig getInstance() {
    ApplicationContainer container = ApplicationContainer.getInstance();
    return (FieldTypeConfig) container.getComponentInstanceOfType(FieldTypeConfig.class);
  }
  
  /**
   * When you want to get instance, please use the getInstance() method.
   * @param params
   * @param datasourceInfo
   * @throws ConfigurationException
   */
  public FieldTypeConfig(InitParams params, DatasourceConfig datasourceInfo) throws ConfigurationException {
    if (params == null) {
      throw new ConfigurationException("Initializations parameters expected");
    }
    
    this.datasourceInfo = datasourceInfo;
    this.fieldTypeName = this.datasourceInfo.fieldTypeName;
    
    ValueParam valParam = params.getValueParam(this.fieldTypeName);
    if (valParam == null) {
      throw new ConfigurationException("fieldTypeXML expected in properties section");
    } 
    
    this.fieldTypeXMLFile = valParam.getValue();
    
  }

  public String getFieldTypeXMLFile() {
    return fieldTypeXMLFile;
  }

  public void setFieldTypeXMLFile(String fieldTypeXMLFile) {
    this.fieldTypeXMLFile = fieldTypeXMLFile;
  }

  public Collection<FieldTypeModel> getFieldTypeModelList() {
    return fieldTypeModelMap.get(this.fieldTypeName);
  }
  
  public Collection<FieldTypeModel> getFieldTypeModelList(String fieldTypeName) {
    return fieldTypeModelMap.get(fieldTypeName);
  }
  
  public boolean getHasFieldType(String fieldTypeName) {
    return fieldTypeModelMap.containsKey(fieldTypeName);
  }

  public void setFieldTypeModelList(Collection<FieldTypeModel> fieldTypeModelList) {
    this.fieldTypeModelMap.put(this.fieldTypeName, fieldTypeModelList);
  }
  //TODO need to unitTest this method
  public FieldType getFieldType(String fieldType) {
    Collection<FieldTypeModel> fieldTypes = getFieldTypeModelList();
    FieldType got = null;
    for(FieldTypeModel typeModel : fieldTypes) {
      got = typeModel.getFieldType(fieldType);
      break;
    }
    return got;
  }
  
}
