package org.etk.core.rest;

import org.etk.core.rest.impl.ApplicationContext;
import org.etk.core.rest.impl.ConstructorDescriptor;
import org.etk.core.rest.impl.ObjectFactory;
import org.etk.core.rest.impl.ObjectModel;


/**
 * Provide object's instance of component that support per-request lifecycle.
 * 
 * @param <T> ObjectModel extensions
 * @see ObjectModel
 * @author <a href="mailto:andrew00x@gmail.com">Andrey Parfonov</a>
 * @version $Id: $
 */
public class PerRequestObjectFactory<T extends ObjectModel> implements ObjectFactory<T> {

  /**
   * Object model that at least gives possibility to create object instance.
   * Should provide full set of available constructors and object fields.
   * 
   * @see ObjectModel
   */
  protected final T model;

  /**
   * @param model any extension of ObectModel
   */
  public PerRequestObjectFactory(T model) {
    this.model = model;
  }

  /**
   * {@inheritDoc}
   */
  public Object getInstance(ApplicationContext context) {
    ConstructorDescriptor inj = model.getConstructorDescriptors().get(0);
    Object object = inj.createInstance(context);

    for (FieldInjector field : model.getFieldInjectors()) {
      field.inject(object, context);
    }

    return object;
  }

  /**
   * {@inheritDoc}
   */
  public T getObjectModel() {
    return model;
  }

}
