package org.etk.orm.api.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>The mixin annotation can be used to annote a class and declares that it represents one mixin.</p>
 *
 * <p>When a class declares a mixin annotation without being bound to a node mapping, this class can be involved
 * in a one to one relationship with the {@link org.chromattic.api.RelationshipType#EMBEDDED} type. The mixin is
 * added to a node when a one to one relationship of type mixin is created.</p>
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface MixinType {

  /**
   * Returns the mixin name.
   *
   * @return the mixin name
   */
  String name();

}