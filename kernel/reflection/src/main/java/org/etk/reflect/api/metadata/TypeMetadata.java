package org.etk.reflect.api.metadata;

import org.etk.reflect.api.definition.ClassKind;
import org.etk.reflect.api.definition.LiteralKind;
import org.etk.reflect.api.definition.TypeKind;

/**
 * Define the TypeMetadata represents to generic model.
 * 
 * @author thanh_vucong
 * @param <T>
 */
public interface TypeMetadata<T> {

  /**
   * Gets the TypeKind via a given SimpleType
   * @param type
   * @return
   */
  TypeKind getKind(T type);

  LiteralKind getLiteralType(T simpleType);

  boolean isPrimitive(T simpleType);

  String getClassName(T classType);

  T getEnclosing(T classType);

  Iterable<T> getInterfaces(T classType);

  T getSuperClass(T classType);

  /**
   * Gets the ClassKind by a given ClassType
   * @param classType
   * @return
   */
  ClassKind getClassKind(T classType);

  Iterable<T> getTypeParameters(T classType);

  String getName(T typeVariable);

  T getComponentType(T arrayType);

  T getGenericDeclaration(T typeVariable);

  GenericDeclarationKind getGenericDeclarationKind(T typeVariable);

  T getRawType(T parameterizedType);

  Iterable<T> getTypeArguments(T parameterizedType);

  Iterable<T> getBounds(T typeVariable);

  Iterable<T> getUpperBounds(T wildcardType);

  Iterable<T> getLowerBounds(T wildcardType);
}
