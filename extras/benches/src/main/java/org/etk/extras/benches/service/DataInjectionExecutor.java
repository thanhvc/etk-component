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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.etk.common.logging.Logger;
import org.etk.extras.benches.service.common.BaseTaskInjector;
import org.etk.extras.benches.service.common.InjectorCompletionService;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Aug 5, 2011  
 */
public class DataInjectionExecutor implements DataExecutor {

  private final ConcurrentMap<String, List<?>> exchanger; 
  private InjectorCompletionService ics = null;
  private Logger log = Logger.getLogger(DataInjectionExecutor.class);
  List<BaseTaskInjector<?>> taskInjectors = new ArrayList<BaseTaskInjector<?>>();
  /**
   * Constructor build with {@link InjectorCompletionService}}
   * @param ics
   */
  public DataInjectionExecutor(InjectorCompletionService ics) {
    this.ics = ics;
    exchanger = new ConcurrentHashMap<String, List<?>>();
  }

  
  public void addInjectorTask(BaseTaskInjector<?> task) throws Exception {
    taskInjectors.add(task);
    log.info("Added the " + task.getTaskName() + " into the TaskList.");
  }
  /**
   * Instantiate the environment for Data Injector
   * @param task
   * @throws Exception
   */
  private void preCondition (BaseTaskInjector<?> task) throws Exception {
    task.initEnv();
    task.accept(this);
    
  }
  
  /**
   * 
   * @throws Exception
   */
  public void execute() throws Exception {
    Collections.sort(taskInjectors, new TaskComparator());
    for(BaseTaskInjector<?> task : taskInjectors) {
      preCondition(task);
      exchanger.put(task.getTaskName(), task.inject());
    }
  }

  public ConcurrentMap<String, List<?>> getExchanger() {
    return exchanger;
  }
  
  public interface TaskVisitor {
    void accept(DataExecutor executor);
  }
  
  private class TaskComparator implements Comparator<BaseTaskInjector<?>> {

    @Override
    public int compare(BaseTaskInjector<?> task1, BaseTaskInjector<?> task2) {
      return task1.priorityExecution - task2.priorityExecution;
    }

        
  }
}
