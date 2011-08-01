package org.etk.kernel.test.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Describe a configuration unit that targets a container scoped by the {@link org.etk.kernel.container.test.annotations.exoplatform.component.test.ContainerScope}
 * type. The path value is the absolute path that will be used to retrieve the configuration file via the
 * {@link ClassLoader#getResources(String)} mechanism therefore the format is described by this mechanisme.
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ConfigurationUnit {
  /**
   * The container scope.
   * 
   * @return the container scope
   */
  ContainerScope scope();

  /**
   * The configuration path.
   * 
   * @return the configuration path
   */
  String path();

}