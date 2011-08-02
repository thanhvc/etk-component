package org.etk.core.rest;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;



/**
 * Describes constructor's parameter.
 * 
 * @author <a href="mailto:andrew00x@gmail.com">Andrey Parfonov</a>
 * @version $Id: $
 */
public class ConstructorParameterImpl implements ConstructorParameter {

  /**
   * All annotations including JAX-RS annotation.  
   */
  private final Annotation[] additional;

  /**
   * One of JAX-RS annotations.
   */
  private final Annotation   annotation;

  /**
   * Parameter type. See {@link java.lang.reflect.Constructor#getGenericParameterTypes()} .
   */
  private final Type         type;

  /**
   * Parameter class. See {@link Constructor#getParameterTypes()}
   */
  private final Class<?>     clazz;

  /**
   * Default value for this parameter, default value can be used if there is not
   * found required parameter in request. See {@link javax.ws.rs.DefaultValue}.
   */
  private final String       defaultValue;

  /**
   * See {@link javax.ws.rs.Encoded}.
   */
  private final boolean      encoded;

  /**
   * Constructs new instance of MethodParameter.
   * 
   * @param annotation see {@link #annotation}
   * @param additional see {@link #additional}
   * @param clazz parameter class
   * @param type generic parameter type
   * @param defaultValue default value for parameter. See {@link javax.ws.rs.DefaultValue}.
   * @param encoded true if parameter must not be decoded false otherwise
   */
  public ConstructorParameterImpl(Annotation annotation,
                                  Annotation[] additional,
                                  Class<?> clazz,
                                  Type type,
                                  String defaultValue,
                                  boolean encoded) {
    this.annotation = annotation;
    this.additional = additional;
    this.clazz = clazz;
    this.type = type;
    this.defaultValue = defaultValue;
    this.encoded = encoded;
  }

  /**
   * {@inheritDoc}
   */
  public Annotation getAnnotation() {
    return annotation;
  }

  /**
   * {@inheritDoc}
   */
  public Annotation[] getAnnotations() {
    return additional;
  }

  /**
   * {@inheritDoc}
   */
  public String getDefaultValue() {
    return defaultValue;
  }

  /**
   * {@inheritDoc}
   */
  public Class<?> getParameterClass() {
    return clazz;
  }

  /**
   * {@inheritDoc}
   */
  public Type getGenericType() {
    return type;
  }

  /**
   * {@inheritDoc}
   */
  public boolean isEncoded() {
    return encoded;
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    StringBuffer sb = new StringBuffer("[ ConstructorParameterImpl : ");
    sb.append("annotation: " + getAnnotation())
      .append("; type: " + getParameterClass())
      .append("; generic-type : " + getGenericType())
      .append("; default-value: " + getDefaultValue())
      .append("; encoded: " + isEncoded())
      .append(" ]");
    return sb.toString();
  }

}

