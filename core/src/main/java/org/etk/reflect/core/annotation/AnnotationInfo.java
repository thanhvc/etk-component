package org.etk.reflect.core.annotation;

import java.util.Collection;

import org.etk.reflect.api.ClassTypeInfo;

public interface AnnotationInfo {
  ClassTypeInfo getType();
  AnnotationParameterInfo<?> getParameter(String parameterName);
  Collection<? extends AnnotationParameterInfo<?>> getParameters();

}
