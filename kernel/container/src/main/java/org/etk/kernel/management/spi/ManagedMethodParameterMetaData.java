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
package org.etk.kernel.management.spi;

/**
 * Meta data that describes the parameter of a managed method.
 * 
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Jul 28, 2011  
 */
public class ManagedMethodParameterMetaData extends ManagedParameterMetaData {

   /** . */
   private final int index;

   /**
    * Build a managed method parameter meta data.
    *
    * @param index the parameter index
    * @throws IllegalArgumentException if the index is negative
    */
  public ManagedMethodParameterMetaData(int index) throws IllegalArgumentException {
    if (index < 0) {
      throw new IllegalArgumentException("Non negative index value accepted " + index);
    }
    this.index = index;
  }

  public int getIndex() {
    return index;
  }
}
