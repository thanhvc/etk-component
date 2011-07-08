package org.etk.reflect.spi.model;


public interface ReflectionModel<T, M, A, P, F> {

  TypeModel<T> getTypeModel();
  FieldModel<T, F> getFieldModel();
  MethodModel<T, M> getMethodModel();
  AnnotationModel<T, T, A, P> getTypeAnnotationModel();
  AnnotationModel<T, M, A, P> getMethodAnnotationModel();
  AnnotationModel<T, F, A, P> getFieldAnnotationModel();
}
