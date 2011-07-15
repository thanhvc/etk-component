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

import org.etk.model.plugins.entity.type.SimpleTypeBinding;
import org.etk.model.plugins.entity.type.SimpleTypeResolver;
import org.etk.orm.plugins.bean.mapping.jcr.PropertyMetaType;
import org.etk.orm.plugins.bean.type.SimpleTypeProvider;

import org.etk.reflect.api.TypeInfo;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Jul 15, 2011  
 */
public class ValueTypeFactory {

  /** . */
  private final SimpleTypeResolver typeResolver;

  public ValueTypeFactory(SimpleTypeResolver typeResolver) {
    this.typeResolver = typeResolver;
  }

  public <I> SimpleTypeProvider<I, ?> create(TypeInfo type, PropertyMetaType<I> jcrType) {
    SimpleTypeBinding vti = typeResolver.resolveType(type, jcrType);
    if (vti == null) {
      throw new IllegalArgumentException("could not find type provider for " + type);
    }

    //
    SimpleTypeProvider vt = vti.create();

    //
    if (!vt.getInternalType().equals(jcrType.getJavaValueType())) {
      throw new AssertionError("todo with type " + type + " / property type" + vt);
    }

    //
    return (SimpleTypeProvider<I, ?>)vt;
  }
}
