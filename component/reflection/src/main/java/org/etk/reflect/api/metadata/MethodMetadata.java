package org.etk.reflect.api.metadata;

import org.etk.reflect.core.AccessScope;

public interface MethodMetadata<T, M> {

  Iterable<M> getDeclaredMethods(T classType);

  String getName(M m);

  T getReturnType(M m);

  Iterable<T> getParameterTypes(M m);

  Iterable<String> getParameterNames(M m);

  AccessScope getAccess(M m);

  boolean isAbstract(M m);

  boolean isStatic(M m);

  boolean isFinal(M m);

  boolean isNative(M m);

  Iterable<T> getTypeParameters(M m);

  M getGenericDeclaration(T typeVariable);

  T getOwner(M m);

  Iterable<T> getThrownTypes(M method);
}
