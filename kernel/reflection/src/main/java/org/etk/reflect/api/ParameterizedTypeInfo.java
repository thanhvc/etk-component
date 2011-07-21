package org.etk.reflect.api;

import java.util.List;

/**
 * ParameterizedType represents a parameterized type such as Collection<String>
 * <p>
 * A parameterized type is created the first time it is needed by a reflective
 * method, as specified in this package. When a parameterized type p is created,
 * the generic type declaration that p instantiates is resolved, and all type
 * arguments of p are created recursively. See TypeVariable for details on the
 * creation process for type variables. Repeated creation of a parameterized
 * type has no effect. Instances of classes that implement this interface must
 * implement an equals() method that equates any two instances that share the
 * same generic type declaration and have equal type parameters.
 * 
 * @author thanh_vucong
 */
public interface ParameterizedTypeInfo extends TypeInfo {

  TypeInfo getRawType();

  /**
   * Returns a List of Type objects representing the actual type arguments to
   * this type. Note that in some cases, the returned array be empty. This can
   * occur if this type represents a non-parameterized type nested within a
   * parameterized type.
   * 
   * @return
   */
  List<TypeInfo> getTypeArguments();

  /**
   * Returns a Type object representing the type that this type is a member of.
   * For example, if this type is O<T>.I<S>, return a representation of O<T>. If
   * this type is a top-level type, null is returned.
   * 
   * @return
   */
  TypeInfo getOwnerType();

}
