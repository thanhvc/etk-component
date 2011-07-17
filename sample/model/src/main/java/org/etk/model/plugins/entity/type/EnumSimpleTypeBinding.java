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
package org.etk.model.plugins.entity.type;

import org.etk.model.plugins.vt2.PropertyMetaType;
import org.etk.orm.plugins.bean.type.SimpleTypeMapping;
import org.etk.orm.plugins.bean.type.SimpleTypeProvider;
import org.etk.reflect.api.ClassTypeInfo;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Jul 15, 2011  
 */
public class EnumSimpleTypeBinding  implements SimpleTypeBinding {

  /** . */
  private final ClassTypeInfo enumInfo;

  public EnumSimpleTypeBinding(ClassTypeInfo enumInfo) {
    this.enumInfo = enumInfo;
  }

  public PropertyMetaType<String> getPropertyMetaType() {
    return PropertyMetaType.STRING;
  }

  public SimpleTypeProvider<?, ?> create() {
    // todo : maybe need a cache here?
    Class clazz = (Class<Object>)enumInfo.unwrap();
    return new EnumSimpleTypeProvider(clazz);
  }
}