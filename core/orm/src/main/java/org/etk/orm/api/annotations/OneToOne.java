package org.etk.orm.api.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.etk.orm.api.RelationshipType;


/**
 * Defines the one side in a one to one relationship.
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface OneToOne {

  /**
   * The type of the relationship.
   *
   * @return the relationship type
   */
  RelationshipType type() default RelationshipType.HIERARCHIC;

}
