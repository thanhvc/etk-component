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
package org.etk.service.foo.impl;

import java.util.List;

import org.etk.service.foo.FooFilter;
import org.etk.service.foo.FooLifeCycle;
import org.etk.service.foo.FooListenerPlugin;
import org.etk.service.foo.model.Foo;
import org.etk.service.foo.spi.FooService;
import org.etk.storage.api.FooStorage;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Jul 21, 2011  
 */
public class FooServiceImpl implements FooService {

  private FooStorage fooStorage;
  private FooLifeCycle fooLifeCycle = new FooLifeCycle();
  
  public FooServiceImpl(FooStorage fooStorage) {
    this.fooStorage = fooStorage;
    
  }
  
  @Override
  public Foo getFooByName(String name) {
    return null;
  }

  @Override
  public Foo getFooById(String fooId) {
    return fooStorage.findById(fooId);
  }

  @Override
  public List<Foo> getAllFoosWithListAccess() {
    return null;
  }

  @Override
  public List<Foo> getAllFoosByFilter(FooFilter filter) {
    return fooStorage.getFooByFilter(filter, 0, 5);
  }

  @Override
  public Foo createFoo(Foo foo) {
    fooStorage.saveFoo(foo);
    fooLifeCycle.fooCreated(foo, "executor");
    return foo;
  }

  @Override
  public Foo updateFoo(Foo existingFoo) {
    fooStorage.saveFoo(existingFoo);
    fooLifeCycle.fooUpdated(existingFoo, "executor");
    return existingFoo;
  }

  @Override
  public void deleteFoo(Foo existingFoo) {
    fooStorage.deleteFoo(existingFoo);
    fooLifeCycle.fooDeleted(existingFoo, "executor");
  }

  @Override
  public void registerFooListenerPlugin(FooListenerPlugin fooListenerPlugin) {
    fooLifeCycle.addListener(fooListenerPlugin);
    
  }

  @Override
  public void unregisterListenerPlugin(FooListenerPlugin fooListenerPlugin) {
    fooLifeCycle.removeListener(fooListenerPlugin);
  }
  
  public void addFooListener(FooListenerPlugin plugin) {
    registerFooListenerPlugin(plugin);
  }

}
