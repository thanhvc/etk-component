package org.etk.orm.api.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Defines the name of the property that is used to maintain one to many relationship with path or reference.
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MappedBy {

  /**
   * The name of the property that maintains the relationship.
   *
   * @return the property name
   */
  String value();
}
