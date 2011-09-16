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
package org.etk.sandbox.entity.plugins.model.xml;

import java.net.URL;
import java.util.Iterator;
import java.util.Map;

import org.etk.kernel.container.configuration.ConfigurationManagerImpl;
import org.jibx.runtime.IMarshallingContext;

import javolution.util.FastMap;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Sep 16, 2011  
 */
public class FieldTypeModel {

  private Map<String, FieldType> fieldTypeMap = FastMap.newInstance();
  private URL documentURL;
 
  
  public FieldTypeModel() {
    documentURL = ConfigurationManagerImpl.getCurrentURL();
  }

  public URL getDocumentURL() {
    return documentURL;
  }
  
  public void addFieldType(FieldType fieldType) {
    fieldTypeMap.put(fieldType.getType(), fieldType);
  }
  
  public Iterator<FieldType> getFieldTypeIterator() {
    return fieldTypeMap.values().iterator();
  }
  
  public int getFieldTypeSize() {
    return fieldTypeMap.size();
  }
  
  public void preGet(IMarshallingContext ictx) {
    ConfigurationMarshallerUtil.addURLToContent(documentURL, ictx);
  }
  
  public boolean hasFieldType() {
    return fieldTypeMap.size() > 0;
  }
}
