package org.etk.orm.api.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.etk.orm.api.NameConflictResolution;

/**
 * Provide information about the naming to the children of a node.
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface NamingPolicy {

  NameConflictResolution onDuplicate() default NameConflictResolution.REPLACE;

}