package org.etk.reflect.api.metadata;

public interface ReflectionMetadata<T, M, A, P, F> {

  TypeMetadata<T> getTypeModel();

  FieldMetadata<T, F> getFieldModel();

  MethodMetadata<T, M> getMethodModel();

  AnnotationMetadata<T, T, A, P> getTypeAnnotationMetadata();

  AnnotationMetadata<T, M, A, P> getMethodAnnotationMetadata();

  AnnotationMetadata<T, F, A, P> getFieldAnnotationMetadata();
}
