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
package org.etk.extras.benches.service.common;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;

import org.etk.common.logging.Logger;
import org.etk.extras.benches.service.DataExecutor;
import org.etk.extras.benches.service.DataInjectionExecutor.TaskVisitor;
import org.etk.extras.benches.service.util.NameGenerator;
import org.etk.kernel.container.component.BaseComponentPlugin;
import org.etk.kernel.container.xml.InitParams;
import org.etk.kernel.container.xml.ValueParam;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Aug 5, 2011  
 */
public abstract class BaseTaskInjector<M> extends BaseComponentPlugin implements TaskVisitor {

  private Logger log = Logger.getLogger(BaseTaskInjector.class);
  
  protected final String DATA_AMOUNT_KEY = "data-amount";
  //Allow the task can execute the parallel
  protected final String PARALLEL_EXECUTOR_KEY = "parallel-executor";
  //Allows the task priority
  protected final String PRIORITY_EXECUTOR_KEY = "priority-executor";
  
  //status which contains the initialize status
  protected boolean isInitialized = false;
  protected DataExecutor dataExecutor = null;
  //task Name
  private String taskName;
  
  protected int dataAmount = 0;
  protected boolean parallelExecution = false;
  public int priorityExecution = -1;
  protected List<M> resultList = null;
  
  protected CountDownLatch endSignal = null;
  protected InjectorCompletionService ics = null;
  protected NameGenerator nameGenerator;
  
  /**
   * Constructor creates the Injector Task
   * @param ics
   * @param params
   * @param taskName
   */
  public BaseTaskInjector(InjectorCompletionService ics, InitParams params, String taskName) {
    this.taskName = taskName;
    initParams(params);
    this.ics = ics;
    
    //
    resultList = new CopyOnWriteArrayList<M>();
    nameGenerator = new NameGenerator();
  }
  
  
  public void addTask(final M model) {
    ics.addTask(new Callable<M>() {
      @Override
      public M call() throws Exception {
        before();
        try {
          M result = doExecute(model);
          resultList.add(result);
          log.info("model created = " + model.toString());
        } finally {
          
          after();
        }
        return model;
      }
      
    });
  }
  
  /**
   * Gets the taskName which adds to the Exchanger.
   * @return
   */
  public String getTaskName() {
    return taskName;
  }

  /**
   * check whether data is initialized or not.
   * 
   * @return true if data is initialized.
   */
  public boolean isInitialized() {
    return isInitialized;
  }
  
  
  /**
   * Initializes the environment for executing the Data Injector.
   * Example: Gets the Service from Container.
   */
  public abstract void initEnv();
  
  /**
   * transform parameters set by user to variables.
   * @param initParams
   */
  private void initParams(InitParams params) {
    
    ValueParam dataAmount = params.getValueParam(DATA_AMOUNT_KEY);
    ValueParam parallelExecution = params.getValueParam(PARALLEL_EXECUTOR_KEY);
    ValueParam priorityExecutor = params.getValueParam(PRIORITY_EXECUTOR_KEY);
    
    try {
      this.dataAmount = Integer.valueOf(dataAmount.getValue());
    } catch(Exception e) {
      this.dataAmount = 1;
    }
    
    try {
      this.parallelExecution = Boolean.valueOf(parallelExecution.getValue());
    } catch(Exception e) {
      this.parallelExecution = false;
    }
    
    try {
      this.priorityExecution = Integer.valueOf(priorityExecutor.getValue());
    } catch(Exception e) {
      this.priorityExecution = 1;
    }
  }
  
  /**
   * inject data
   * @throws Exception
   */
  public abstract List<M> inject() throws Exception;
  
  /**
   * reject data
   * @throws Exception
   */
  public abstract void reject() throws Exception;

  
  /**
   * Before the task executes
   */
  public void before() {
    
  }
  
  /**
   * After the task executes
   */
  public void after() {
    endSignal.countDown();
    log.info("endSignal = " + endSignal.toString());
  }
  
 
  public abstract M doExecute(M model);
  
}
