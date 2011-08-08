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

import org.etk.common.logging.Logger;
import org.etk.extras.benches.service.common.BaseTaskInjector;
import org.etk.extras.benches.service.common.InjectionException;
import org.etk.extras.benches.service.common.InjectorCompletionService;
import org.etk.kernel.container.ApplicationContainer;
import org.etk.kernel.container.xml.InitParams;
import org.etk.service.bar.api.BarService;
import org.etk.service.foo.model.Bar;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Aug 5, 2011  
 */
public class BarTaskInjector extends BaseTaskInjector<Bar> {

  private Logger log = Logger.getLogger(BarTaskInjector.class);
  
  BarService barService = null;
  private ApplicationContainer container;
  /**
   * 
   */
  public BarTaskInjector(InitParams params, InjectorCompletionService ics) {
    super(ics, params, BarTaskInjector.class.getName());
    container = ApplicationContainer.getInstance();
    
  }
 
  @Override
  public void initEnv() {
    barService = (BarService) container.getComponentInstanceOfType(BarService.class);
  }

  @Override
  public void accept(DataExecutor executor) {
    this.dataExecutor = executor;
    
  }

  @Override
  public List<Bar> inject() throws InjectionException {
    for (int i = 0; i < dataAmount; i++) {
      Bar model = new Bar(String.valueOf(i), nameGenerator.compose(4) + String.valueOf(i)); 
      addTask(model);
    }
    if (parallelExecution == false) {
      try {
        endSignal.await();
      } catch (InterruptedException e) {
        throw new InjectionException(e.getMessage(), e);
      }
    }
    return resultList;
    
  }

  @Override
  public void reject() throws InjectionException {
    // TODO Auto-generated method stub
    
  }

  @Override
  public Bar callService(Bar model) {
    log.info("created the BAR model name: " + model.getDescription());
    return barService.createBar(model);
  }
  
  
}
