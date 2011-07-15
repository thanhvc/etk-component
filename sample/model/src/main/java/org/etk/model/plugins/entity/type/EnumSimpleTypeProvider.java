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

import org.etk.orm.plugins.bean.type.SimpleTypeProvider;
import org.etk.orm.plugins.bean.type.TypeConversionException;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Jul 15, 2011  
 */
class EnumSimpleTypeProvider<E extends Enum<E>> extends SimpleTypeProvider.STRING<E> {

  /** . */
  private final Class<E> externalType;

  public EnumSimpleTypeProvider(Class<E> externalType) {
    this.externalType = externalType;
  }

  @Override
  public String getInternal(E e) throws TypeConversionException {
    return e.name();
  }

  @Override
  public E getExternal(String s) throws TypeConversionException {
    try {
      return Enum.valueOf(externalType, s);
    }
    catch (IllegalArgumentException e) {
      throw new IllegalStateException("Enum value cannot be determined from the stored value", e);
    }
  }

  @Override
  public E fromString(String s) throws TypeConversionException {
    return getExternal(s);
  }

  @Override
  public String toString(E e) throws TypeConversionException {
    return getInternal(e);
  }

  @Override
  public Class<E> getExternalType() {
    return externalType;
  }
}

