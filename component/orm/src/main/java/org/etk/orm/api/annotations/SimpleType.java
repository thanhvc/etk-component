package org.etk.orm.api.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Declares a simple type.
 *
 */
@Target(value = ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface SimpleType {

  /**
   * Returns the {@link SimpleTypeProvider} class.
   *
   * @return the converter class
   */
  Class/*<? extends SimpleTypeProvider<?, ?>>*/ value();
}
