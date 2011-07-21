package org.etk.reflect.jlr.metadata;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;

public class JLReflectionFieldAnnotationMetadata extends JLReflectionAnnotationMetadata<Field> {

  public <A extends Annotation> A resolveDeclaredAnnotation(Field field, Class<A> annotationClass) {
    for (Annotation annotation : field.getAnnotations()) {
      if (annotationClass.isInstance(annotation)) {
        return annotationClass.cast(annotation);
      }
    }
    return null;
  }

  public Collection<Annotation> getDeclaredAnnotation(Field field) {
    return Arrays.asList(field.getDeclaredAnnotations());
  }
}
