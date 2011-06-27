package org.etk.reflect.core;

public class ReflectedObject<T, M, A, P, F> {

  protected final TypeResolverImpl<T, M, A, P, F> domain;
  
  public ReflectedObject(TypeResolverImpl<T, M, A, P, F> domain) {
    this.domain = domain;
  }
}
