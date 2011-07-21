package org.etk.reflect.api.metadata;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.List;
/**
 * Define the AnnotationMetadata interface help to resolve the annotation.
 * 
 * @author thanh_vucong
 *
 * @param <T>
 * @param <E>
 * @param <A>
 * @param <P>
 */
public interface AnnotationMetadata<T, E, A, P> {
  <A extends Annotation> A resolveDeclaredAnnotation(E element, Class<A> annotationClass);
  Collection<A> getDeclaredAnnotation(E element);
  Collection<P> getAnnotationParameters(A annotation);
  
  T getAnnotationType(A a);
  String getAnnotationParameterName(P parameter);
  T getAnnotationParameterType(P parameter);
  List<?> getAnnotationParameterValue(A annotation, P parameter);

}
