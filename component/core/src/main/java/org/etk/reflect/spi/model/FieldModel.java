package org.etk.reflect.spi.model;

import org.etk.reflect.core.AccessScope;

public interface FieldModel<T, F> {

  Iterable<F> getDeclaredFields(T classType);
  
  String getName(F f);
  
  AccessScope getAccess(F f);
  
  T getType(F f);
  
  boolean isStatic(F f);
  
  boolean isFinal(F f);
  
  T getOwner(F f);
}
