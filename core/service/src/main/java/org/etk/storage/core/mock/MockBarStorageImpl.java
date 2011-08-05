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
import org.etk.service.bar.BarFilter;
import org.etk.service.foo.model.Bar;
import org.etk.service.foo.model.Foo;
import org.etk.storage.core.impl.jcr.BarStorageImpl;

/**
 * This is MockBarStorageImpl which provides to Unit Testing's Storage Caching.
 * It will also configure in the /config/application/test-configuration.xml.
 * 
 * Created by The eXo Platform SAS Author : 
 * eXoPlatform exo@exoplatform.com Jul 26, 2011
 */
public class MockBarStorageImpl extends BarStorageImpl {
  private final Logger log = Logger.getLogger(MockBarStorageImpl.class);
  @Override
  public Bar findById(String id) {
    log.debug("Access to findById(1) method \n\n");
    return createBarMock(1);
  }
  
  
  
  
  @Override
  public List<Bar> getBarByFilter(BarFilter barFilter, int offset, int limit) {
    log.debug("Access to getBarByFilter() method \n\n");
    return getBarsList(offset, limit);
  }
  
  @Override
  public int getBarByFilterCount(BarFilter fooFilter) {
    log.debug("Access to getFooByFilterCount() method \n\n");
    return getBarsList(0, 5).size();
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
  private List<Bar> getBarsList(int offset, int limit) {
    List<Bar> bars = new ArrayList<Bar>();
    for (int i = offset; i< limit; i++) {
      bars.add(createBarMock(i));
    }
    
    return bars;
  }
  private Bar createBarMock(int index) {
    return new Bar("F000" + index, "Name for F000" + index);
  }
  
  
  @Override
  public Bar saveBar(final Bar bar) {
    log.debug("saveBar(bar) with id = " + bar.getId());
    return bar;
  }
  
  
  @Override
  public void deleteBar(final Bar existingBar) {
    log.debug("deleteBar(existingBar) with id = " + existingBar.getId());
  }
  
  
  @Override
  public Bar updateBar(final Bar existingBar) {
    log.debug("updateBar(existingBar) with id = " + existingBar.getId());
    return existingBar;
  }

}
