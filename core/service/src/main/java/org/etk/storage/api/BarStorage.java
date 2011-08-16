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
package org.etk.storage.api;

import java.util.List;

import org.etk.service.bar.BarFilter;
import org.etk.service.foo.model.Bar;
import org.etk.service.foo.model.Foo;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Jul 22, 2011  
 */
public interface BarStorage {

  /**
   * Gets Bar by Id
   * @param id
   * @return
   */
  Bar findById(String id);
  
  /**
   * Gets the Bar list base on the FooFilter with offset and limit.
   * 
   * @param barFilter
   * @param offset
   * @param limit
   * @return
   */
  List<Bar> getBarByFilter(BarFilter barFilter, int offset, int limit);
  
  /**
   * Count the number of Bar with condition FooFilter.
   * @param barFilter
   * @return
   */
  int getBarByFilterCount(final BarFilter barFilter);

  /**
   * loads Bar from Foo
   * @param foo
   * @return
   */
  Bar loadBar(final Foo foo);
  
  
  /**
   * Creates the Bar data.
   * @param foo
   * @return
   */
  Bar saveBar(final Bar bar);
  
  /**
   * Updates a Bar's information.
   * 
   * @param existingBar
   * @return
   */
  Bar updateBar(final Bar existingBar);
  
  /**
   * Removes the existing Bar.
   * @param existingBar
   */
  void deleteBar(final Bar existingBar);
}
