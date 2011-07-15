package org.etk.model.api.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Defines the mapping between a json and a entity java class.
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Entity {

  /**
   * The name of the primary node type to use.
   *
   * @return the primary node type name
   */
  String name();

  /**
   * Returns true if the type is abstract.
   *
   * @return the value true if the type is abstract
   */
  boolean abstract_() default false;

}

