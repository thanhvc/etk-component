package org.etk.orm.api.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Defines a destructor method to significate the end of life of a Chromattic entity. The annotated
 * method must provides a no argument constructor. After the invocation of the method, the status
 * of the entity will be equals to {@link org.chromattic.api.Status#REMOVED}.
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Destroy {
}