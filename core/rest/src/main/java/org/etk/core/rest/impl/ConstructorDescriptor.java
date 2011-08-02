package org.etk.core.rest.impl;

import java.lang.reflect.Constructor;
import java.util.List;

import org.etk.core.rest.ConstructorParameter;


/**
 * Abstraction of constructor descriptor. Used for create object instance when
 * type is used in per-request lifecycle.
 * 
 */
public interface ConstructorDescriptor extends ResourceDescriptor {

  /**
   * @param context ApplicationContext
   * @return newly created instance of the constructor's
   * @see ApplicationContext
   */
  Object createInstance(ApplicationContext context);

  /**
   * Get source constructor.
   * 
   * @return constructor
   * @see Constructor
   */
  Constructor<?> getConstructor();

  /**
   * @return constructor's parameters
   */
  List<ConstructorParameter> getParameters();

}
