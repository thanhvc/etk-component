package org.etk.orm.api.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotates a bean property to map it against the current name of the related jcr node.
 * <ul>
 * <li>When the entity is {@link org.chromattic.api.Status#TRANSIENT} the name is saved along with the entity
 * until it is persisted. When the entity is persisted, the name is used when no additional name is provided.</li>
 * <li>When the entity is {@link org.chromattic.api.Status#PERSISTENT} the name is associated with the current JCR
 * node name. A property read returns the current node name and a property write performs a JCR move operation to
 * rename the entity with the new name.</li>
 * <li>When the entity is {@link org.chromattic.api.Status#REMOVED} any property access throws an
 * {@link IllegalStateException}.</li>
 * </ul>
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Name {
}
