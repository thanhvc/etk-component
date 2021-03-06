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
package org.etk.component.base.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.etk.common.logging.Logger;
import org.etk.kernel.container.xml.InitParams;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          thanhvucong.78@google.com
 * Aug 6, 2011  
 */
public class ListenerService {
  /**
   * This executor used for asynchronously event broadcast.
   */
  private final Executor                    executor;

  /**
   * Listeners by name map.
   */
  private final Map<String, List<Listener>> listeners_;

  private static final Logger log = Logger.getLogger(ListenerService.class);

  /**
   * Construct a listener service.
   */
  public ListenerService() {
    listeners_ = new HashMap<String, List<Listener>>();
    executor = Executors.newFixedThreadPool(1, new ListenerThreadFactory());
  }

  /**
   * Construct a listener service.
   */
  public ListenerService(InitParams params) {
    listeners_ = new HashMap<String, List<Listener>>();
    int poolSize = 1;

    if (params != null) {
      if (params.getValueParam("asynchPoolSize") != null) {

        poolSize = Integer.parseInt(params.getValueParam("asynchPoolSize").getValue());
      }
    }
    executor = Executors.newFixedThreadPool(poolSize, new ListenerThreadFactory());
  }

  /**
   * This method is used to register a {@link Listener} to the events of the
   * same name. It is similar to addListener(listener.getName(), listener)
   * 
   * @param listener the listener to notify any time an even of the same name is
   *          triggered
   */
  public void addListener(Listener listener) {
    addListener(listener.getName(), listener);
  }

  /**
   * This method is used to register a new {@link Listener}. Any time an event
   * of the given event name has been triggered, the {@link Listener} will be
   * notified. This method will:
   * <ol>
   * <li>Check if it exists a list of listeners that have been registered for
   * the given event name, create a new list if no list exists</li>
   * <li>Add the listener to the list</li>
   * </ol>
   * 
   * @param eventName The name of the event to listen to
   * @param listener The Listener to notify any time the event with the given
   *          name is triggered
   */
  public void addListener(String eventName, Listener listener) {
    // Check is Listener or its superclass asynchronous, if so - wrap it in
    // AsynchronousListener.
    Class listenerClass = listener.getClass();

    do {
      if (listenerClass.isAnnotationPresent(Asynchronous.class)) {
        listener = new AsynchronousListener(listener);
        break;
      } else {
        listenerClass = listenerClass.getSuperclass();
      }
    } while (listenerClass != null);

    List<Listener> list = listeners_.get(eventName);
    if (list == null) {
      list = new ArrayList<Listener>();
      listeners_.put(eventName, list);
    }
    list.add(listener);
  }

  /**
   * This method is used to broadcast an event. This method should: 1. Check if
   * there is a list of listener that listen to the event name. 2. If there is a
   * list of listener, create the event object with the given name , source and
   * data 3. For each listener in the listener list, invoke the method
   * onEvent(Event)
   * 
   * @param <S> The type of the source that broacast the event
   * @param <D> The type of the data that the source object is working on
   * @param name The name of the event
   * @param source The source object instance
   * @param data The data object instance
   * @throws Exception TODO: Should not delegate to the method broadcast(Event)
   */
  final public <S, D> void broadcast(String name, S source, D data) throws Exception {
    List<Listener> list = listeners_.get(name);
    if (list == null)
      return;
    for (Listener<S, D> listener : list) {
      if (log.isDebugEnabled()) {
        log.debug("broadcasting event " + name + " on " + listener.getName());
      }

      try {
        listener.onEvent(new Event<S, D>(name, source, data));
      } catch (Exception e) {
        // log exception and keep broadcast events
        log.error("Exception on broadcasting events occures: " + e.getMessage(), e.getCause());
        log.info("Exception occures but keep broadcast events.");
      }
    }
  }

  /**
   * This method is used when a developer want to implement his own event object
   * and broadcast the event. The method should: 1. Check if there is a list of
   * listener that listen to the event name. 2. If there is a list of the
   * listener, ror each listener in the listener list, invoke the method
   * onEvent(Event)
   * 
   * @param <T> The type of the event object, the type of the event object has
   *          to be extended from the Event type
   * @param event The event instance
   * @throws Exception
   */
  final public <T extends Event> void broadcast(T event) throws Exception {
    List<Listener> list = listeners_.get(event.getEventName());
    if (list == null) {
      return;
    }
    for (Listener listener : list) {
      try {
        listener.onEvent(event);
      } catch (Exception e) {
        // log exception and keep broadcast events
        log.error("Exception on broadcasting events occures: " + e.getMessage(), e.getCause());
        log.info("Exception occures but keep broadcast events.");
      }
    }
  }

  /**
   * This AsynchronousListener is a wrapper for original listener, that executes
   * wrapped listeners onEvent() in separate thread.
   */
  protected class AsynchronousListener<S, D> extends Listener<S, D> {
    private Listener<S, D> listener;

    public AsynchronousListener(Listener<S, D> listener) {
      this.listener = listener;
    }

    @Override
    public String getName() {
      return listener.getName();
    }

    @Override
    public void setName(String s) {
      listener.setName(s);
    }

    @Override
    public String getDescription() {
      return listener.getDescription();
    }

    @Override
    public void setDescription(String s) {
      listener.setDescription(s);
    }

    @Override
    public void onEvent(Event<S, D> event) throws Exception {
      executor.execute(new RunListener<S, D>(listener, event));
    }
  }

  /**
   * This thread executes listener.onEvent(event) method.
   */
  protected class RunListener<S, D> implements Runnable {
    private Listener<S, D> listener;

    private Event<S, D>    event;

    public RunListener(Listener<S, D> listener, Event<S, D> event) {
      this.listener = listener;
      this.event = event;
    }

    /**
     * {@inheritDoc}
     */
    public void run() {
      try {
        listener.onEvent(event);
      } catch (Exception e) {
        // Do not throw exception. Event is asynchronous so just report error.
        // Must say that exception will be ignored even in synchronous events.
        log.error("Exception on broadcasting events occures: " + e.getMessage(), e.getCause());
      }
    }
  }
}
