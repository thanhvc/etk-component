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

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.etk.kernel.container.xml.InitParams;
import org.etk.kernel.container.xml.ValueParam;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Aug 5, 2011  
 */
public class InjectorCompletionService {
  private final String THREAD_NUMBER = "thread-number";
  private final String ASYNC_EXECUTION = "async-execution";
  
  private Executor executor = null;
  private ExecutorCompletionService ecs = null;
  
  private final int DEFAULT_THREAD_NUMBER = 20;
  private final boolean DEFAULT_ASYNC_EXECUTION = true;
  
  private int configThreadNumber;
  private boolean configAsyncExecution;

  public InjectorCompletionService(InitParams params) {
    
    ValueParam threadNumber = params.getValueParam(THREAD_NUMBER);
    ValueParam asyncExecution = params.getValueParam(ASYNC_EXECUTION);
    
    try {
      this.configThreadNumber = Integer.valueOf(threadNumber.getValue());
    } catch(Exception e) {
      this.configThreadNumber = DEFAULT_THREAD_NUMBER;
    }
    
    try {
      this.configAsyncExecution = Boolean.valueOf(asyncExecution.getValue());
    } catch(Exception e) {
      this.configAsyncExecution = DEFAULT_ASYNC_EXECUTION;
    }
    
    if (configAsyncExecution) {
      this.executor = Executors.newFixedThreadPool(this.configThreadNumber);
    } else {
      this.executor = new SynchronousExecutor();
      
    }
    
    this.ecs = new ExecutorCompletionService(executor);
    
  }
  
  public void addTask(Callable callable) {
    ecs.submit(callable);
  }
  
  public void waitCompletionFinished() {
    try {
      if (executor instanceof ExecutorService) {
        ((ExecutorService) executor).awaitTermination(1, TimeUnit.SECONDS);
      }
    }
    catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }
  
  private class SynchronousExecutor implements Executor {

    public void execute(final Runnable runnable) {
      if (Thread.interrupted()) throw new RuntimeException();
      runnable.run();
    }
  }
}
