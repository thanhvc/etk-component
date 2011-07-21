package org.etk.reflect.jlr.metadata;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;

public class JLReflectionTypeAnnotationMetadata  extends JLReflectionAnnotationMetadata<Type> {

  public <A extends Annotation> A resolveDeclaredAnnotation(Type classType, Class<A> annotationClass) {
    return ((AnnotatedElement)classType).getAnnotation(annotationClass);
  }

  public Collection<Annotation> getDeclaredAnnotation(Type type) {
    return Arrays.asList(((Class)type).getDeclaredAnnotations());
  }
}
