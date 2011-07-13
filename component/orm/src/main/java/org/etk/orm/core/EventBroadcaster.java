package org.etk.orm.core;

import java.util.ArrayList;
import java.util.List;

import org.etk.orm.api.event.EventListener;
import org.etk.orm.api.event.LifeCycleListener;
import org.etk.orm.api.event.StateChangeListener;
import org.etk.orm.plugins.common.CloneableInputStream;


public final class EventBroadcaster implements EventListener {

  /** . */
  private List<LifeCycleListener> lifeCycleListeners;

  /** . */
  private List<StateChangeListener> stateChangeListeners;

  @SuppressWarnings("unchecked")
  public EventBroadcaster() {
    this.lifeCycleListeners = null;
  }

  public void addLifeCycleListener(EventListener listener) throws NullPointerException {
    if (listener == null) {
      throw new NullPointerException();
    }
    if (listener instanceof LifeCycleListener) {
      LifeCycleListener lifeCycleListener = (LifeCycleListener)listener;
      if (lifeCycleListeners == null  || !lifeCycleListeners.contains(lifeCycleListener)) {
        if (lifeCycleListeners == null) {
          lifeCycleListeners = new ArrayList<LifeCycleListener>();
        }
        lifeCycleListeners.add(lifeCycleListener);
      }
    }
    if (listener instanceof StateChangeListener) {
      StateChangeListener stateChangeListener = (StateChangeListener)listener;
      if (stateChangeListeners == null || !stateChangeListeners.contains(stateChangeListener)) {
        if (stateChangeListeners == null) {
          stateChangeListeners = new ArrayList<StateChangeListener>();
        }
        stateChangeListeners.add(stateChangeListener);
      }
    }
  }

  public boolean hasLifeCycleListeners() {
    return lifeCycleListeners != null;
  }

  public boolean hasStateChangeListeners() {
    return stateChangeListeners != null;
  }

  public void created(Object o) {
    if (lifeCycleListeners == null) {
      return;
    }
    for (EventListener listener : lifeCycleListeners) {
      try {
        ((LifeCycleListener)listener).created(o);
      }
      catch (Exception ignore) {
      }
    }
  }

  public void loaded(EntityContext ctx, Object o) {
    if (lifeCycleListeners == null) {
      return;
    }
    String id = ctx.getId();
    String path = ctx.getPath();
    String name = ctx.getLocalName();
    for (EventListener listener : lifeCycleListeners) {
      try {
        ((LifeCycleListener)listener).loaded(id, path, name, o);
      }
      catch (Exception ignore) {
      }
    }
  }

  public void added(EntityContext ctx, Object o) {
    if (lifeCycleListeners == null) {
      return;
    }
    String id = ctx.getId();
    String path = ctx.getPath();
    String name = ctx.getLocalName();
    for (EventListener listener : lifeCycleListeners) {
      try {
        ((LifeCycleListener)listener).added(id, path, name, o);
      }
      catch (Exception ignore) {
      }
    }
  }

  public void removed(String id, String path, String name, Object o) {
    if (lifeCycleListeners == null) {
      return;
    }
    for (EventListener listener : lifeCycleListeners) {
      try {
        ((LifeCycleListener)listener).removed(id, path, name, o);
      }
      catch (Exception ignore) {
      }
    }
  }

  public void propertyChanged(String id, Object o, String propertyName, Object propertyValue) {
    if (stateChangeListeners == null) {
      return;
    }
    for (EventListener listener : stateChangeListeners) {
      try {
        if (propertyValue instanceof CloneableInputStream) {
          ((StateChangeListener)listener).propertyChanged(id, o, propertyName, ((CloneableInputStream)propertyValue).clone());
        } else {
          ((StateChangeListener)listener).propertyChanged(id, o, propertyName, propertyValue);
        }
      }
      catch (Exception ignore) {
      }
    }
  }
}

