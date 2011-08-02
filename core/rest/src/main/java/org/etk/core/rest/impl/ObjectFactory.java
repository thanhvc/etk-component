package org.etk.core.rest.impl;


/**
 * Implementation of this interface should be able provide object instance
 * dependent of component lifecycle.
 * 
 * @param <T> ObjectModel extensions
 * @see ObjectModel
 */
public interface ObjectFactory<T extends ObjectModel> {

  /**
   * Create object instance. ApplicationContext can be used for getting required
   * parameters for object constructors or fields.
   * 
   * @param context ApplicationContext
   * @return object instance
   */
  Object getInstance(ApplicationContext context);

  /**
   * @return any extension of {@link ObjectModel}. That must allows create
   *         object instance and initialize object's fields for per-request
   *         resources
   */
  T getObjectModel();

}