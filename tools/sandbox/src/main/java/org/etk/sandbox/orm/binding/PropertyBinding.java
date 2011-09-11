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
package org.etk.sandbox.orm.binding;

import org.etk.sandbox.orm.info.PropertyInfo;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Aug 17, 2011  
 */
public class PropertyBinding<P extends PropertyInfo> {

  public void accept(BindingVisitor visitor) {


  }
  
  /** . */
  ETKBinding owner;

  /** The optional parent. */
  PropertyBinding<P> parent;

  /** . */
  final P property;

  public PropertyBinding(P property) {
    this.property = property;
  }

  public PropertyBinding<P> getParent() {
    return parent;
  }

  public ETKBinding getOwner() {
    return owner;
  }

  public String getName() {
    return property.getName();
  }

  public P getProperty() {
    return property;
  }

  public Object getValue() {
    //return property.getValue();
    return null;
  }

  
}
