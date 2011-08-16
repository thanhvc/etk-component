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
package org.etk.service.bar.api;

import java.util.List;

import org.etk.service.bar.BarFilter;
import org.etk.service.bar.BarListenerPlugin;
import org.etk.service.foo.FooFilter;
import org.etk.service.foo.FooListenerPlugin;
import org.etk.service.foo.model.Bar;
import org.etk.service.foo.model.Foo;

/**
 * Provides the method to manipulate with the Space.
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Jul 21, 2011  
 */
public interface BarService {

  /**
   * Gets the Bar by its description
   * @param name
   * @return
   */
  Bar getBarByDescription(String description);
  
  /**
   * Gets a Bar by it's id.
   * 
   * @param fooId
   * @return
   */
  Bar getBarById(String barId);
  
  /**
   * 
   * Gets a Bar list access which contains all the Foo
   * 
   * @return
   */
  List<Bar> getAllBarsWithListAccess();
  
  /**
   * Gets a Bar list access which contains all the foo by FooFilter.
   * 
   * @param filter
   * @return
   */
  List<Bar> getAllBarsByFilter(BarFilter filter);
  
  /**
   * Creates the Bar data.
   * @param Bar
   * @return
   */
  Bar createBar(Bar bar);
  /**
   * Updates a Bar information.
   * 
   * @param existingBar
   * @return
   */
  Bar updateBar(Bar existingBar);
  
  /**
   * Delets a Bar. When deleting a bar, all of its bar also deletes.
   * @param bar
   */
  void deleteBar(Bar bar);
  
  /**
   * Registers a Bar listener to listen to 
   * Bar lifecycle event: create, update, etc ...
   * 
   * @param barListenerPlugin
   */
  void registerBarListenerPlugin(BarListenerPlugin barListenerPlugin);
  /**
   * Unregisters an existing Bar listener plugin.
   * @param barListenerPlugin
   */
  void unregisterListenerPlugin(BarListenerPlugin barListenerPlugin);
  
  
  
}
