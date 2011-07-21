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
package org.etk.service.kernel;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;

import org.etk.common.logging.Logger;


/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Jul 21, 2011  
 */
public abstract class AbstractLifeCycle<T extends LifeCycleListener<E>, E extends LifeCycleEvent<?,?>> {

  /** Logger */
  private static final Logger LOG = Logger.getLogger(AbstractLifeCycle.class);

  protected Set<T> listeners = new HashSet<T>();

 
  
  private ThreadLocal<String> currentRepository;
  
 
  protected LifeCycleCompletionService completionService;
  

 protected AbstractLifeCycle() {

    
    currentRepository = new ThreadLocal<String>();
  }

  /**
   * {@inheritDoc}
   */
  public void addListener(T listener) {
    listeners.add(listener);
  }

  /**
   * {@inheritDoc}
   */
  public void removeListener(T listener) {
    listeners.remove(listener);
  }

  /**
   * Broadcasts an event to the registered listeners. The event is broadcasted
   * asynchronously but sequentially.
   *
   * @see #dispatchEvent(LifeCycleListener, LifeCycleEvent)
   * @param event
   */
  protected void broadcast(final E event) {
     addTasks(event);
  }

 
  protected void addTasks(final E event) {
    for (final T listener : listeners) {
      completionService.addTask(new Callable<E>() {
        public E call() throws Exception {
          try {
            begin();
            dispatchEvent(listener, event);
          }
          catch(Exception e) {
            LOG.debug(e.getMessage(), e);
          }
          finally {
            end();
          }

          return event;
        }
      });
    }
  }

  protected void begin() {
    
    
  }

  protected void end() {
    
  }

  protected abstract void dispatchEvent(final T listener, final E event);

}
