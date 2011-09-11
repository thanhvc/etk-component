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
package org.etk.sandbox.orm.plugins;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          thanhvucong.78@google.com
 * Aug 19, 2011  
 */
public class AbstractEntityInfo {

  /** . */
  private final String name;

  /** . */
  private final Map<String, PropertyDefinition> propertyDefinitions;

  public AbstractEntityInfo(ORMType ormType) {
    Map<String, PropertyDefinition> propertyDefinitions = new HashMap<String, PropertyDefinition>();
    for (String propertyName : ormType.getPropertyNames()) {
      PropertyDefinition propertyDefinitionInfo = new PropertyDefinition(propertyName);
      propertyDefinitions.put(propertyName, propertyDefinitionInfo);
    }

    //
    this.name = ormType.getName();
    this.propertyDefinitions = propertyDefinitions;
  }

  public String getName() {
    return name;
  }

  public PropertyDefinition getPropertyDefinitionInfo(String name) {
    return propertyDefinitions.get(name);
  }

  public PropertyDefinition findPropertyDefinition(String propertyName) {
    PropertyDefinition propertyDefinitionInfo = getPropertyDefinitionInfo(propertyName);
    if (propertyDefinitionInfo == null) {
      return getPropertyDefinitionInfo("*");
    }
    return propertyDefinitionInfo;
  }

  public Object getValue(String propertyName) {
    return null;
  }

  public void setProperty(String propertyName, Object value) {
        
  }
}
