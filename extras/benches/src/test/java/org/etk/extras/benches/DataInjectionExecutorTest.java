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
package org.etk.extras.benches;

import java.util.List;
import java.util.concurrent.ConcurrentMap;

import org.etk.extras.benches.service.BarTaskInjector;
import org.etk.extras.benches.service.DataInjectionExecutor;
import org.etk.extras.benches.service.FooTaskInjector;
import org.etk.extras.benches.service.common.InjectorCompletionService;
import org.etk.kernel.test.spi.AbstractApplicationTest;
import org.etk.service.bar.api.BarService;
import org.etk.storage.api.BarStorage;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Aug 5, 2011  
 */
public class DataInjectionExecutorTest  extends AbstractApplicationTest {

  
  /**
   * Data Injector for Performance Testing.
   * @throws Exception
   */
  public void testDataInjectorExecutorInstantiate() throws Exception {
    InjectorCompletionService ics = (InjectorCompletionService) getContainer().getComponentInstanceOfType(InjectorCompletionService.class);
    assertNotNull(ics);
    
    DataInjectionExecutor executor = (DataInjectionExecutor) getContainer().getComponentInstanceOfType(DataInjectionExecutor.class);
    assertNotNull(executor);
    
    BarStorage barStorage = (BarStorage) getContainer().getComponentInstanceOfType(BarStorage.class);
    assertNotNull(barStorage);
    
    BarService barService = (BarService) getContainer().getComponentInstanceOfType(BarService.class);
    assertNotNull(barService);
  }
  
  public void testDataInjectorFoo() throws Exception {
    DataInjectionExecutor executor = (DataInjectionExecutor) getContainer().getComponentInstanceOfType(DataInjectionExecutor.class);
    executor.execute();
    ConcurrentMap<String, List<?>> exchanger = executor.getExchanger();
    assertTrue("Exchanger's size must be greater than zero.", exchanger.size() > 0);
    List<?> foos = exchanger.get(FooTaskInjector.class.getName());
    assertEquals("Foos's size must be equals 100.", 100, foos.size());
    
    List<?> bars = exchanger.get(BarTaskInjector.class.getName());
    assertEquals("Bars's size must be equals 100.", 100, bars.size());
  }
}
