package org.etk.model.api.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Defines the mapping between the property of a node type and a java bean property of the annotated class.
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Property {

  /**
   * The json property name either qualified or unqualified.
   *
   * @return the json property name
   */
  String name();

  /**
   * Specify the property type of the mapped property, the value must be a legal value referenced by
   * {@code javax.jcr.PropertyType}. The default value returned is -1 which means that the value is determined
   * by Model according to the type of the annotated property.
   *
   * @return the property type value.
   */
  int type() default -1;

}

