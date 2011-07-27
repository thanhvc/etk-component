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
package org.etk.storage.core.impl.jcr;

import java.util.List;

import org.etk.service.foo.FooFilter;
import org.etk.service.foo.model.Bar;
import org.etk.service.foo.model.Foo;
import org.etk.storage.api.FooStorage;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Jul 22, 2011  
 */
public class FooStorageImpl implements FooStorage {

  private FooStorage fooStorage;
  
  @Override
  public List<Foo> getFooByFilter(FooFilter fooFilter, int offset, int limit) {
    return null;
  }
  @Override
  public int getFooByFilterCount(final FooFilter fooFilter) {
    return 0;
  }

  @Override
  public Foo findById(String id) {
    return null;
  }
  
  /**
   * Sets the FooStorage
   * @param storage
   */
  public void setStorage(FooStorage storage) {
    this.fooStorage = storage;
  }
  
  private FooStorage getStorage() {
    return (fooStorage != null ? fooStorage : this);
  }

  @Override
  public Bar loadBar(Foo foo) {
    return null;
  }
  @Override
  public Foo saveFoo(Foo foo) {
    // TODO Auto-generated method stub
    return null;
  }
  @Override
  public Foo updateFoo(Foo existingFoo) {
    // TODO Auto-generated method stub
    return null;
  }
  @Override
  public void deleteFoo(Foo existingFoo) {
    // TODO Auto-generated method stub
    
  }
  
  
}
