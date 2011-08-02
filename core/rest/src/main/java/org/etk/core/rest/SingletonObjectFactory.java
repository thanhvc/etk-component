package org.etk.core.rest;

import org.etk.core.rest.impl.ApplicationContext;
import org.etk.core.rest.impl.ObjectFactory;
import org.etk.core.rest.impl.ObjectModel;

/**
 * Provide object instance of components that support singleton lifecycle.
 * 
 * @param <T>
 */
public class SingletonObjectFactory<T extends ObjectModel> implements ObjectFactory<T> {

  /**
   * @see ObjectModel.
   */
  protected final T      model;

  /**
   * Component instance.
   */
  protected final Object object;

  /**
   * @param model ObjectMode
   * @param object component instance
   */
  public SingletonObjectFactory(T model, Object object) {
    this.model = model;
    this.object = object;
  }

  /**
   * {@inheritDoc}
   */
  public Object getInstance(ApplicationContext context) {
    // prepared object instance
    return object;
  }

  /**
   * {@inheritDoc}
   */
  public T getObjectModel() {
    return model;
  }

}
