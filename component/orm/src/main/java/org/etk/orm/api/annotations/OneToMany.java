package org.etk.orm.api.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.etk.orm.api.RelationshipType;

/**
 * Defines the one side in a one to many relationship.
 *
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 * @see org.chromattic.api.RelationshipType
 * @see org.chromattic.api.annotations.ManyToOne
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface OneToMany {

  /**
   * The type of the relationship.
   *
   * @return the relationship type
   */
  RelationshipType type() default RelationshipType.HIERARCHIC;
  
}