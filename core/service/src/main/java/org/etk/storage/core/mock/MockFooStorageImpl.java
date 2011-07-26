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
package org.etk.storage.core.mock;

import java.util.ArrayList;
import java.util.List;

import org.etk.common.logging.Logger;
import org.etk.service.foo.FooFilter;
import org.etk.service.foo.model.Bar;
import org.etk.service.foo.model.Foo;
import org.etk.storage.core.impl.jcr.FooStorageImpl;

/**
 * This is MockFooStorageImpl which provides to Unit Testing's Storage Caching.
 * It will also configure in the /config/application/test-configuration.xml.
 * 
 * Created by The eXo Platform SAS Author : 
 * eXoPlatform exo@exoplatform.com Jul 26, 2011
 */
public class MockFooStorageImpl extends FooStorageImpl {
  private final Logger log = Logger.getLogger(MockFooStorageImpl.class);
  @Override
  public Foo findById(String id) {
    log.debug("Access to findById(1) method \n\n");
    return createFooMock(1);
  }
  
  @Override
  public List<Foo> getFooByFilter(FooFilter fooFilter, int offset, int limit) {
    log.debug("Access to getFooByFilter() method \n\n");
    return getFoosList(offset, limit);
  }
  
  @Override
  public int getFooByFilterCount(FooFilter fooFilter) {
    log.debug("Access to getFooByFilterCount() method \n\n");
    return getFoosList(0, 5).size();
  }
  
  @Override
  public Bar loadBar(Foo foo) {
    log.debug("Access to loadBar() method \n\n");
    return createBarMock();
  }
  
  private Bar createBarMock() {
    return new Bar("B0001", "Description for B0001");

  }
  /**
   * Creates the List of Foo for Unit Testing
   * @param amountOfFoo
   * @return
   */
  private List<Foo> getFoosList(int offset, int limit) {
    List<Foo> foos = new ArrayList<Foo>();
    for (int i = offset; i< limit; i++) {
      foos.add(createFooMock(i));
    }
    
    return foos;
  }
  private Foo createFooMock(int index) {
    return new Foo("F000" + index, "Name for F000" + index);
  }

}
