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
import java.util.Set;

import org.etk.sandbox.orm.binding.ETKBinding;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          thanhvucong.78@google.com
 * Aug 19, 2011  
 */
public class ORMType {

  public enum Kind {
    JSON, JDBC, JCR
  }
  /** . */
  private Kind entityKind;
  
  /** . */
  final ETKBinding binding;
  
  /** . */
  final String name;

  /** . */
  final String className;
  
  final Map<String, String> dataMap;
  
  /** . */
  final Map<String, PropertyDefinition> properties;

  public Kind getEntityKind() {
    return entityKind;
  }
  
  public ORMType(ETKBinding binding) {
    this(binding, Kind.JSON);
  }
  
  public ORMType(ETKBinding binding, Kind kind) {
    this.entityKind = kind;
    this.binding = binding;
    this.name = binding.getEntityName();
    this.className = binding.getEtkInfo().getClassTypeInfo().getName();
    this.properties = new HashMap<String, PropertyDefinition>();
    
    this.dataMap = new HashMap<String, String>();
  }
  
  public String getClassName() {
    return className;
  }

   
  public String getName() {
    return name;
  }
  
  public PropertyDefinition getPropertyDefinition(String propertyName) {
    return properties.get(propertyName);
  }

  public Map<String, PropertyDefinition> getPropertyDefinitions() {
    return properties;
  }
  
  public Set<String> getPropertyNames() {
    return dataMap.keySet();
  }
  
  @Override
  public String toString() {
    return "SourceType[name=" + name + "]";
  }
}
