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
package org.etk.model.plugins.entity.binding;

import org.etk.model.plugins.entity.PropertyInfo;
import org.etk.orm.plugins.bean.ValueInfo;
import org.etk.orm.plugins.bean.ValueKind;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Jul 14, 2011  
 */
public abstract class PropertyBinding<P extends PropertyInfo<V, K>, V extends ValueInfo, K extends ValueKind> {

  /** . */
  EntityBinding owner;

  /** The optional parent. */
  PropertyBinding parent;

  /** . */
  final P property;

  public PropertyBinding(P property) {
    this.property = property;
  }

  public PropertyBinding getParent() {
    return parent;
  }

  public EntityBinding getOwner() {
    return owner;
  }

  public String getName() {
    return property.getName();
  }

  public P getProperty() {
    return property;
  }

  public V getValue() {
    return property.getValue();
  }
  
  public abstract void accept(BindingVisitor visitor);


   /**
   * Returns true if the property type is covariant, meaning that it redefines the type from an ancestor
   * with a subclass.
   *
   * @return true if the property is type covariant
   */
  public abstract boolean isTypeCovariant();
}