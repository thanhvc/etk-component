/**
 * Copyright (C) 2003-2008 eXo Platform SAS.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see<http://www.gnu.org/licenses/>.
 */

package org.etk.core.rest.impl.method;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * Describes the method's parameter.
 * 
 */
public class MethodParameterImpl implements org.etk.core.rest.method.MethodParameter {

  /**
   * External annotations for parameter, external it mind some other then
   * contains in {@link ParameterHelper#METHOD_PARAMETER_ANNOTATIONS_MAP}.
   */
  private final Annotation[] additional;

  /**
   * One of annotations from
   * {@link ParameterHelper#METHOD_PARAMETER_ANNOTATIONS_MAP}.
   */
  private final Annotation   annotation;

  /**
   * Parameter type. See {@link java.lang.reflect.Method#getGenericParameterTypes()} .
   */
  private final Type         type;

  /**
   * Parameter class. See {@link java.lang.reflect.Method#getParameterTypes()}
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
   * @param defaultValue default value for parameter. See
   *          {@link javax.ws.rs.DefaultValue}.
   * @param encoded true if parameter must not be decoded false otherwise
   */
  public MethodParameterImpl(Annotation annotation,
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
  public Annotation[] getAnnotations() {
    return additional;
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
  public boolean isEncoded() {
    return encoded;
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
  public Type getGenericType() {
    return type;
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
  @Override
  public String toString() {
    StringBuffer sb = new StringBuffer("[ MethodParameter: ");
    sb.append("annotation: " + getAnnotation())
      .append("; type: " + getParameterClass())
      .append("; generic-type: " + getGenericType())
      .append("; default-value: " + getDefaultValue())
      .append("; encoded: " + isEncoded())
      .append(" ]");
    return sb.toString();
  }
  

}
