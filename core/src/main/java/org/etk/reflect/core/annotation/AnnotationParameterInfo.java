package org.etk.reflect.core.annotation;

import java.util.List;

public interface AnnotationParameterInfo<T> {

  String getName();
  AnnotationParameterType<T> getType();
  boolean isMultiValued();
  T getValue();
  List<T> getValues();
}
