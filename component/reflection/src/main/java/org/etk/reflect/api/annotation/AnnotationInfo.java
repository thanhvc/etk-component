package org.etk.reflect.api.annotation;

import java.util.Collection;

import org.etk.reflect.api.ClassTypeInfo;

public interface AnnotationInfo {
  ClassTypeInfo getType();
  AnnotationParameterInfo<?> getParameter(String parameterName);
  Collection<? extends AnnotationParameterInfo<?>> getParameters();

}
