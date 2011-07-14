package org.etk.reflect.api.annotation;

import java.util.Collection;

import org.etk.reflect.api.ClassTypeInfo;

/**
 * Represents an annotated element of the program currently running in this VM
 * his interface allows annotations to be read reflectively. All annotations
 * returned by methods in this interface are immutable and serializable. It is
 * permissible for the caller to modify the arrays returned by accessors for
 * array-valued enum members; it will have no affect on the arrays returned to
 * other callers.
 * 
 * @author thanh_vucong
 */
public interface AnnotationInfo {
  ClassTypeInfo getType();

  AnnotationParameterInfo<?> getParameter(String parameterName);

  Collection<? extends AnnotationParameterInfo<?>> getParameters();

}
