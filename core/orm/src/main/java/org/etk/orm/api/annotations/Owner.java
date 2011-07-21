package org.etk.orm.api.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotates the owner of a relationship:
 * <ul>
 * <li>In a one to one hierarchic relationship it distinguishes the parent.</li>
 * <li>In a many to one path or referenced relationship it distinguishes the entity owning the field, in that case it is
 * optional as the relationship is not symetric.</li>
 * <li>In an one to one embedded relationship, it distinguishes the entity from its embedded mixin or super type.</li>
 * </ul>
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Owner {
}

