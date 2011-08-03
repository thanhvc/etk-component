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
package org.etk.service.foo.api;

import java.util.List;

import org.etk.service.foo.FooFilter;
import org.etk.service.foo.FooListenerPlugin;
import org.etk.service.foo.model.Foo;

/**
 * Provides the method to manipulate with the Space.
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Jul 21, 2011  
 */
public interface FooService {

  /**
   * Gets the Foo by its name
   * @param name
   * @return
   */
  Foo getFooByName(String name);
  
  /**
   * Gets a Foo by it's id.
   * 
   * @param fooId
   * @return
   */
  Foo getFooById(String fooId);
  
  /**
   * 
   * Gets a Foo list access which contains all the Foo
   * 
   * @return
   */
  List<Foo> getAllFoosWithListAccess();
  
  /**
   * Gets a Foo list access which contains all the foo by FooFilter.
   * 
   * @param filter
   * @return
   */
  List<Foo> getAllFoosByFilter(FooFilter filter);
  
  /**
   * Creates the Foo data.
   * @param foo
   * @return
   */
  Foo createFoo(Foo foo);
  /**
   * Updates a Foo's information.
   * 
   * @param existingFoo
   * @return
   */
  Foo updateFoo(Foo existingFoo);
  
  /**
   * Delets a foo. When deleting a foo, all of its bar also deletes.
   * @param foo
   */
  void deleteFoo(Foo foo);
  
  /**
   * Registers a Foo listener to listen to 
   * Foo lifecycle event: create, update, etc ...
   * 
   * @param fooListenerPlugin
   */
  void registerFooListenerPlugin(FooListenerPlugin fooListenerPlugin);
  /**
   * Unregisters an existing Foo listener plugin.
   * @param fooListenerPlugin
   */
  void unregisterListenerPlugin(FooListenerPlugin fooListenerPlugin);
  
  
  
}
