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
package org.etk.model.core.entity;

import java.util.HashMap;
import java.util.Map;

import org.etk.model.plugins.json.type.PropertyDefinitionInfo;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Jul 15, 2011  
 */
public class EntityTypeInfo {

  /** . */
  private final String name;

  /** . */
  private final Map<String, PropertyDefinitionInfo> propertyDefinitions;

  public EntityTypeInfo(EntityType entityType) {
    Map<String, PropertyDefinitionInfo> propertyDefinitions = new HashMap<String, PropertyDefinitionInfo>();
    for (String propertyName : entityType.getPropertyNames()) {
      PropertyDefinitionInfo propertyDefinitionInfo = new PropertyDefinitionInfo(propertyName);
      propertyDefinitions.put(propertyName, propertyDefinitionInfo);
    }

    //
    this.name = entityType.getName();
    this.propertyDefinitions = propertyDefinitions;
  }

  public String getName() {
    return name;
  }

  public PropertyDefinitionInfo getPropertyDefinitionInfo(String name) {
    return propertyDefinitions.get(name);
  }

  public PropertyDefinitionInfo findPropertyDefinition(String propertyName) {
    PropertyDefinitionInfo propertyDefinitionInfo = getPropertyDefinitionInfo(propertyName);
    if (propertyDefinitionInfo == null) {
      return getPropertyDefinitionInfo("*");
    }
    return propertyDefinitionInfo;
  }
  
  
}
