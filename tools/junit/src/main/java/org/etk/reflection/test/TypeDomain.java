package org.etk.reflection.test;

import java.util.Map;


public class TypeDomain<T> {

  /** . */
  private TypeResolver<T> domain;

  /** . */
  private Map<String, T> types;

  public TypeDomain(TypeResolver<T> domain, Map<String, T> types) {
    this.domain = domain;
    this.types = types;
  }

  protected final TypeInfo getTypeInfo(String typeId) {
    T type = types.get(typeId);
    if (type == null) {
      throw new IllegalArgumentException("Could not find type " + typeId);
    }
    return getInfo(type);
  }

  private TypeInfo getInfo(T type) {
    return domain.resolve(type);
  }

  public MethodInfo getMethodInfo(String id) {
    for (T type : types.values()) {
      TypeInfo ti = domain.resolve(type);
      if (ti instanceof ClassTypeInfo) {
        for (MethodInfo mi : ((ClassTypeInfo)ti).getDeclaredMethods()) {
          org.reflext.test.Method methodAnn = mi.getDeclaredAnnotation(AnnotationType.get(org.reflext.test.Method.class));
          if (methodAnn != null && methodAnn.value().equals(id)) {
            return mi;
          }
        }
      }
    }
    return null;
  }
}