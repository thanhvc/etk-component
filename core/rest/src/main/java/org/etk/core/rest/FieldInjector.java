package org.etk.core.rest;

import org.etk.core.rest.impl.ApplicationContext;
import org.etk.core.rest.Parameter;
import org.etk.core.rest.impl.ResourceDescriptor;

/**
 * Object field. Useful for initialization object field if type is used in
 * per-request mode.
 * 
 */
public interface FieldInjector extends Parameter, ResourceDescriptor {

  /**
   * @return field name
   */
  String getName();

  /**
   * Set Object {@link java.lang.reflect.Field} using ApplicationContext for
   * resolve actual field value.
   * 
   * @param resource root resource or provider
   * @param context ApplicationContext
   */
  void inject(Object resource, ApplicationContext context);

}
