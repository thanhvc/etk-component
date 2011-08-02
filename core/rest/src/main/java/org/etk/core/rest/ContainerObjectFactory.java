package org.etk.core.rest;

import org.etk.core.rest.impl.ApplicationContext;
import org.etk.core.rest.impl.ObjectFactory;
import org.etk.core.rest.impl.ObjectModel;
import org.etk.kernel.container.KernelContainerContext;


/**
 * Factory provides object that is created and is manageable by
 * inversion-of-control container, PicoContainer.
 * 
 * @param <T> any extension of {@link ObjectModel}
 */

public class ContainerObjectFactory<T extends ObjectModel> implements ObjectFactory<T> {

  /**
   * Object model.
   */
  protected final T model;

  /**
   * @param model object model
   * @see ObjectModel
   */
  public ContainerObjectFactory(T model) {
    this.model = model;
  }

  /**
   * {@inheritDoc}
   */
  public Object getInstance(ApplicationContext context) {
    Class<?> clazz = model.getObjectClass();
    return KernelContainerContext.getCurrentContainer().getComponentInstanceOfType(clazz);
  }

  /**
   * {@inheritDoc}
   */
  public T getObjectModel() {
    return model;
  }

}

