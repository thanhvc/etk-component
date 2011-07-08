package org.etk.reflect.model;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.List;
/**
 * Define the AnnotationModel interface help to resolve the annotation.
 * @author thanh_vucong
 *
 * @param <T>
 * @param <E>
 * @param <A>
 * @param <P>
 */
public interface AnnotationModel<T, E, A, P> {
  <A extends Annotation> A resolveDeclaredAnnotation(E element, Class<A> annotationClass);
  Collection<A> getDeclareAnnotation(E element);
  Collection<A> getAnnotationParameters(A annotation);
  
  T getAnnotationType(A a);
  String getAnnotationParameterName(P parameter);
  T getAnnotationParameterType(P parameter);
  List<?> getAnnotationParameterValue(A annotation, P parameter);

}
