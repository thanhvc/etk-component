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

import java.util.HashSet;
import java.util.Set;

import org.etk.orm.plugins.bean.mapping.BeanMapping;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Jul 13, 2011  
 */
public class NodeDefinition {

  /** . */
  private final String name;

  /** . */
  private final boolean mandatory;

  /** . */
  private final boolean autocreated;

  /** . */
  final Set<BeanMapping> mappings;

  public NodeDefinition(String name, boolean mandatory, boolean autocreated) {
    this.name = name;
    this.mandatory = mandatory;
    this.autocreated = autocreated;
    this.mappings = new HashSet<BeanMapping>();
  }

  public String getName() {
    return name;
  }

  public boolean isMandatory() {
    return mandatory;
  }

  public boolean isAutocreated() {
    return autocreated;
  }

  public String getNodeTypeName() {
    // Try to find the common ancestor type of all types
    BeanMapping ancestorMapping = null;
    foo:
    for (BeanMapping relatedMapping1 : mappings) {
      for (BeanMapping relatedMapping2 : mappings) {
        if (!relatedMapping1.getBean().getClassType().isAssignableFrom(relatedMapping2.getBean().getClassType())) {
          continue foo;
        }
      }
      ancestorMapping = relatedMapping1;
      break;
    }

    //
    if (ancestorMapping == null) {
      return "nt:base";
    } else {
      return ancestorMapping.getNodeTypeName();
    }
  }
}

