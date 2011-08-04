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
package org.etk.extras.benches.service;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import org.etk.common.logging.Logger;
import org.etk.extras.benches.service.util.NameGenerator;
import org.etk.kernel.container.ApplicationContainer;
import org.etk.service.core.event.LifeCycleCompletionService;
import org.etk.service.foo.api.FooService;
import org.etk.service.foo.model.Bar;
import org.etk.service.foo.model.Foo;
/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Aug 3, 2011  
 */
public class DataInjector {

  private static Logger LOG = Logger.getLogger(DataInjector.class);

  private final FooService fooService;

  private AtomicInteger fooCount;

  private NameGenerator nameGenerator;

  protected LifeCycleCompletionService completionService;
  private ApplicationContainer container;
  /**
   * 
   * @param fooService
   */
  public DataInjector(FooService fooService) {
    fooCount = new AtomicInteger(0);
    container = ApplicationContainer.getInstance();
    this.fooService = fooService;
    this.completionService = (LifeCycleCompletionService) container.getComponentInstanceOfType(LifeCycleCompletionService.class);
    nameGenerator = new NameGenerator();
  }

  /**
   * Generate a variable amount of foo
   *
   * @param count
   * @return foo created
   */
  public List<Foo> generateFoo(long count, long mulThreading) {
    long t1 = System.currentTimeMillis();
    List<Foo> foos = new CopyOnWriteArrayList<Foo>();
    for (int i = 0; i < count; i++) {
      Foo foo = generateFoo(mulThreading);
      if (foo != null) {
        foos.add(foo);
      }
    }
    LOG.info("generateFoo() : " + (System.currentTimeMillis() - t1) + "ms");
    return foos;
  }
  
  /**
   * Adds the task for synchronous or asynchronous
   * @param foo
   */
  private void addTasks(final Foo foo) {

    completionService.addTask(new Callable<Foo>() {
      public Foo call() throws Exception {
        try {
          fooService.createFoo(foo);
        } catch (Exception e) {
          LOG.debug(e.getMessage(), e);
        }

        return foo;
      }
    });

  }
  
  /**
   * Generate a variable amount of bar
   *
   * @param count
   * @return bar created
   */
  public List<Bar> generateBar(long count) {
    return null;
  }

   /**
   * Generate or get <code>Foo</code>
   *
   * @param user
   * @return
   */
  private Foo generateFoo(long mulThreading) {
    final Foo foo = new Foo();
    int idx = fooCount.getAndIncrement();
    initRandomFoo(foo, idx);
    foo.setId(String.valueOf(idx));
    try {
      //mulThreading value configures in the xml file
      if (mulThreading == 0) {
        fooService.createFoo(foo);
      } else {
        addTasks(foo);        
      }
      
    } catch (Exception e) {
      LOG.error("Failed to generate foo for " + foo.getName() + ": " + e.getMessage());
    }
    return foo;
  }
  
  /**
   * 
   * @param foo
   */
  void initRandomFoo(Foo foo, int idx) {
    foo.setName(nameGenerator.compose(4) + String.valueOf(idx));
  }
  
}
