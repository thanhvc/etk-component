package org.etk.sandbox.entity.plugins.jdbc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import org.etk.common.logging.Logger;
import org.etk.sandbox.entity.plugins.config.DatasourceConfig;


public class DatabaseUtil {

  public static final Logger logger = Logger.getLogger(DatabaseUtil.class);

  protected DatasourceConfig datasourceInfo = null;
  
  protected ExecutorService executor;
  
  public DatabaseUtil(DatasourceConfig datasourceInfo, ExecutorService executor) {
    // FIXME get datasource from the Component.
    this.datasourceInfo = datasourceInfo;
    this.executor = executor;
  }
  
  protected <T> Future<T> submitWork(Callable<T> callable) {
    if (this.executor == null) {
      FutureTask<T> task = new FutureTask<T>(callable);
      task.run();
      return task;
    }
    return this.executor.submit(callable);
  }

  protected <T> List<Future<T>> submitAll(Collection<? extends Callable<T>> tasks) {
    List<Future<T>> futures = new ArrayList<Future<T>>(tasks.size());

    if (this.executor == null) {
      for (Callable<T> callable : tasks) {
        FutureTask<T> task = new FutureTask<T>(callable);
        task.run();
        futures.add(task);
      }
    }

    for (Callable<T> callable : tasks) {
      futures.add(this.executor.submit(callable));
    }

    return futures;
  }
  
  
}
