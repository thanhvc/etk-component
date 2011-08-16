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
import java.util.concurrent.CountDownLatch;

import org.etk.common.logging.Logger;
import org.etk.extras.benches.service.common.BaseTaskInjector;
import org.etk.extras.benches.service.common.InjectionException;
import org.etk.extras.benches.service.common.InjectorCompletionService;
import org.etk.kernel.container.ApplicationContainer;
import org.etk.kernel.container.xml.InitParams;
import org.etk.service.foo.api.FooService;
import org.etk.service.foo.model.Foo;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Aug 5, 2011  
 */
public class FooTaskInjector extends BaseTaskInjector<Foo> {

  private Logger log = Logger.getLogger(FooTaskInjector.class);
  
  FooService fooService = null;
  private ApplicationContainer container;
  /**
   * 
   */
  public FooTaskInjector(InitParams params, InjectorCompletionService ics) {
    super(ics, params, FooTaskInjector.class.getName());
    container = ApplicationContainer.getInstance();
    
  }
 
  @Override
  public void initEnv() {
    fooService = (FooService) container.getComponentInstanceOfType(FooService.class);
  }

  @Override
  public void accept(DataExecutor executor) {
    this.dataExecutor = executor;
    
  }

  @Override
  public List<Foo> inject() throws InjectionException {
    for (int i = 0; i < dataAmount; i++) {
      Foo model = new Foo(String.valueOf(i), nameGenerator.compose(4) + String.valueOf(i));
      addTask(model);
    }
    if (parallelExecution == false) {
      try {
        endSignal.await();
      } catch (InterruptedException e) {
        throw new InjectionException(e.getMessage(), e);
      }
    }
    log.info("\n\n\nresultList's size = " + resultList.size());
    return resultList;
    
  }

  @Override
  public void reject() throws InjectionException {
    // TODO Auto-generated method stub
    
  }

  

  @Override
  public Foo callService(Foo model) {
    log.info("created the model name: " + model.getName());
    return fooService.createFoo(model);
  }
  
  
}
