package org.etk.kernel.test.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Annotates a unit test extending the
 * {@link org.exoplatform.component.test.AbstractGateInTest} to provide the
 * various configuration unit relevant for executing the unit test.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ConfiguredBy {

  /**
   * Returns the various relevant configuration units.
   * 
   * @return the configuration units
   */
  ConfigurationUnit[] value();

}
