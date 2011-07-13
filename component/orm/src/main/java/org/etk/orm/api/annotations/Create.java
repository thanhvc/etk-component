package org.etk.orm.api.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Defines a creator method to create transient instances of a Chromattic entity.
 *
 * The annotated method can have the following arguments:
 * <ul>
 * <li>No arguments that returns a transient entity.</li>
 * <li>A single <code>String</code> argument that returns a transient entity. That entity has a name
 * that is equals to the argument value when the method is invoked.</li>
 * </ul>
 *
 * The type of the of the returned entity is defined by the return type of the method that must be the type
 * of a registered instantiatable Chromattic entity.
 *
 * After the invocation of the method, the status of the entity will be equals to {@link org.chromattic.api.Status#TRANSIENT}.
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Create {
}
