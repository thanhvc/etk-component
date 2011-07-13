package org.etk.orm.api.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.etk.orm.api.format.ObjectFormatter;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface FormattedBy {

  /**
   * The optional object formatter for instances of this object.
   *
   * @return the object formatter
   */
  Class<? extends ObjectFormatter> value();
}
