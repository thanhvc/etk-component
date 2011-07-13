package org.etk.reflect.jlr.metadata;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;


public class JLReflectionMethodAnnotationMetadata extends JLReflectionAnnotationMetadata<Method> {

  public <A extends Annotation> A resolveDeclaredAnnotation(Method method, Class<A> annotationClass) {
    for (Annotation annotation : method.getAnnotations()) {
      if (annotationClass.isInstance(annotation)) {
        return annotationClass.cast(annotation);
      }
    }
    return null;
  }

  @Override
  public Collection<Annotation> getDeclaredAnnotation(Method method) {
    return Arrays.asList(method.getDeclaredAnnotations());
  }
}