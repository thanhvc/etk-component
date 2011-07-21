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
package org.etk.orm.plugins.bean.typegen;

import java.util.ArrayList;
import java.util.List;

import org.etk.orm.plugins.bean.mapping.jcr.PropertyDefinitionMapping;

/**
 * Created by The eXo Platform SAS
 * Jul 13, 2011  
 */
public class PropertyDefinition {

  PropertyDefinition(String name, boolean multiple, int type) {
    this.name = name;
    this.multiple = multiple;
    this.type = type;
    this.defaultValues = null;
    this.valueConstraints = null;
  }

  PropertyDefinition(PropertyDefinitionMapping<?> mapping, boolean multiple) {

    //
    List<String> defaultValues = mapping.getDefaultValue();
    if (defaultValues == null) {
      defaultValues = null;
    } else {
      defaultValues = new ArrayList<String>(defaultValues);
    }

    //
    this.multiple = multiple;
    this.name = mapping.getName();
    this.type = mapping.getMetaType().getCode();
    this.defaultValues = defaultValues;
    this.valueConstraints = null;
  }

  /** . */
  private final String name;

  /** . */
  private final boolean multiple;

  /** . */
  private final int type;

  /** . */
  private List<String> defaultValues;

  /** . */
  private List<String> valueConstraints;

  public String getName() {
    return name;
  }

  public boolean isMultiple() {
    return multiple;
  }

  public int getType() {
    return type;
  }

  public List<String> getDefaultValues() {
    return defaultValues;
  }

  public List<String> getValueConstraints() {
    return valueConstraints;
  }

  void addValueConstraint(String valueConstraint) {
    if (valueConstraints == null) {
      valueConstraints = new ArrayList<String>();
    }
    valueConstraints.add(valueConstraint);
  }
}
