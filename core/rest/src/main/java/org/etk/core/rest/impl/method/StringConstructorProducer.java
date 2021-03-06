/**
 * Copyright (C) 2003-2008 eXo Platform SAS.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see<http://www.gnu.org/licenses/>.
 */

package org.etk.core.rest.impl.method;

import java.lang.reflect.Constructor;

/**
 * Create object which has constructor with single String parameter.
 * 
 * @author <a href="mailto:andrew00x@gmail.com">Andrey Parfonov</a>
 * @version $Id: $
 */
public final class StringConstructorProducer extends BaseTypeProducer {

  /**
   * Constructor which must be used for creation object.
   */
  private Constructor<?> constructor;

  /**
   * @param constructor this constructor will be used for creation instance of
   *          object
   */
  StringConstructorProducer(Constructor<?> constructor) {
    this.constructor = constructor;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected Object createValue(String value) throws Exception {
    if (value == null)
      return null;

    return constructor.newInstance(value);
  }

}
