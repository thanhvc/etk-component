package org.etk.orm.api.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Defines the mapping between a primarty type and a java class.
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface PrimaryType {

  /**
   * The name of the primary node type to use.
   *
   * @return the primary node type name
   */
  String name();

  /**
   * Returns true if the node is ordereable. This information is used when generating the
   * meta model or checking the meta model validity against the underlying model.
   *
   * @return the orderability
   */
  boolean orderable() default false;

  /**
   * Returns true if the type is abstract.
   *
   * @return the value true if the type is abstract
   */
  boolean abstract_() default false;

}

