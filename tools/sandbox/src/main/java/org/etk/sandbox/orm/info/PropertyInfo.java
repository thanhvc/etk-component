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
package org.etk.sandbox.orm.info;

import org.etk.reflect.api.MethodInfo;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Aug 17, 2011  
 */
public class PropertyInfo<V extends ValueInfo, K extends ValueKind> {

  private final ETKInfo owner;
  private PropertyInfo parent;
  
  private final String name;
  private final MethodInfo getter;
  private final MethodInfo setter;
  private final K valueKind;
  private final V value;
  
  public PropertyInfo(ETKInfo etkInfo, String name, MethodInfo getter, MethodInfo setter, K valueKind, V value) {
    this.owner = etkInfo;
    this.name = name;
    this.getter = getter;
    this.setter = setter;
    this.valueKind = valueKind;
    this.value = value;
  }


  public ETKInfo getOwner() {
    return owner;
  }

  public String getName() {
    return name;
  }

  public MethodInfo getGetter() {
    return getter;
  }

  public MethodInfo getSetter() {
    return setter;
  }

  public K getValueKind() {
    return valueKind;
  }

  public V getValue() {
    return value;
  }
}
