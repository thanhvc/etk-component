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
import org.etk.model.plugins.entity.SimpleValueInfo;
import org.etk.orm.plugins.bean.ValueKind;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Jul 15, 2011  
 */
public class AttributeBinding extends PropertyBinding<PropertyInfo<SimpleValueInfo, ValueKind.Single>, SimpleValueInfo, ValueKind.Single> {

  /** . */
  private final AttributeType type;

  public AttributeBinding(PropertyInfo<SimpleValueInfo, ValueKind.Single> property, AttributeType type) {
    super(property);
    this.type = type;
  }

  @Override
  public boolean isTypeCovariant() {

    // 
    return false;
  }

  public AttributeType getType() {
    return type;
  }

  @Override
  public void accept(BindingVisitor visitor) {
    visitor.attributeBinding(this);
  }
}