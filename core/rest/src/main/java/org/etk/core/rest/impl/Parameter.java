package org.etk.core.rest.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * Abstraction of method's, constructor's parameter or object field.
 * 
 */
public interface Parameter {

  /**
   * @return addition annotation
   */
  Annotation[] getAnnotations();

  /**
   * @return <i>main</i> annotation. It mind this annotation describe which
   *         value will be used for initialize parameter, e. g.
   *         {@link javax.ws.rs.PathParam}, {@link javax.ws.rs.QueryParam}, etc.
   */
  Annotation getAnnotation();

  /**
   * @return true if parameter must not be decoded false otherwise
   */
  boolean isEncoded();

  /**
   * @return default value for parameter
   */
  String getDefaultValue();

  /**
   * @return generic parameter type
   * @see java.lang.reflect.Method#getGenericParameterTypes()
   */
  Type getGenericType();

  /**
   * @return parameter class.
   * @see java.lang.reflect.Method#getParameterTypes()
   */
  Class<?> getParameterClass();

}
