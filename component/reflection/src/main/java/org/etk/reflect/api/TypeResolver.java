package org.etk.reflect.api;

/**
 * The TypeResolve interface represents a Resolver for <code>TypeInfo</code>
 * @author thanh_vucong
 *
 * @param <T>
 */
public interface TypeResolver<T> {

  TypeInfo resolve(T type);
}
