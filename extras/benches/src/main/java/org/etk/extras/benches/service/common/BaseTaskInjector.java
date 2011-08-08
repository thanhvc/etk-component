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
 * Define the BaseTaskInjector which uses for DataInjector.
 * 
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Aug 5, 2011  
 */
public abstract class BaseTaskInjector<M> extends BaseComponentPlugin implements TaskVisitor {

  private Logger log = Logger.getLogger(BaseTaskInjector.class);
  
  /**-----Fields for execution processing. ----**/
  /**Status which determine whether the Task executed or not **/
  protected boolean isInitialized = false;
  /** Keeps the data result which returns by this one.**/
  protected List<M> resultList = null;
  /**DataInjector execution.**/
  protected DataExecutor dataExecutor = null;
  /**Controls the dependency between the threads**/
  protected CountDownLatch endSignal = null;
  /** The Injector Completion Service controls the thread for executing the Data Injector code.**/
  protected InjectorCompletionService ics = null;
  
  /**-----Fields for configuration in the configuration.xml. ----**/
  
  protected final String DATA_AMOUNT_KEY = "data-amount";
  /**Allow the task can execute the parallel**/
  protected final String PARALLEL_EXECUTOR_KEY = "parallel-executor";
  /**Allows the task priority**/
  protected final String PRIORITY_EXECUTOR_KEY = "priority-executor";
  /**task Name makes sure the unique and which uses is key in the Exchanger.**/
  private String taskName;
  /** Configures the data amount which will created in the DB**/
  protected int dataAmount = 0;
  /** Configures the parallelExecution**/
  protected boolean parallelExecution = true;
  /** Priority which tells to Executor know to executions in order.**/
  public int priorityExecution = -1;
  
  /** Supports to create the TestData**/
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
          resultList.add(callService(model));
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
    
    ValueParam dataAmountVal = params.getValueParam(DATA_AMOUNT_KEY);
    ValueParam parallelExecutionVal = params.getValueParam(PARALLEL_EXECUTOR_KEY);
    ValueParam priorityExecutorVal = params.getValueParam(PRIORITY_EXECUTOR_KEY);
    
    try {
      this.dataAmount = Integer.valueOf(dataAmountVal.getValue());
    } catch(Exception e) {
      this.dataAmount = 1;
    }
    
    try {
      this.parallelExecution = Boolean.valueOf(parallelExecutionVal.getValue());
    } catch(Exception e) {
      this.parallelExecution = true;
    }
    
    try {
      this.priorityExecution = Integer.valueOf(priorityExecutorVal.getValue());
    } catch(Exception e) {
      this.priorityExecution = 1;
    }
    //Initializes the CountDownLatch
    if (this.parallelExecution == false) {
      endSignal = new CountDownLatch(this.dataAmount);
    }
  }
  
  /**
   * inject data
   * @throws Exception
   */
  public abstract List<M> inject() throws InjectionException;
  
  /**
   * reject data
   * @throws Exception
   */
  public abstract void reject() throws InjectionException;

  
  /**
   * Before the task executes
   */
  public void before() {
    
  }
  
  /**
   * After the task executes
   */
  public void after() {
    if (this.parallelExecution == false)
      endSignal.countDown();
  }
  
 
  /**
   * Calls the service which manipulates with the DataModel.
   * 
   * @param model
   * @return
   */
  public abstract M callService(M model);
  
}
