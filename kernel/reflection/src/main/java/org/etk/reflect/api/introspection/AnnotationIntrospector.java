package org.etk.reflect.api.introspection;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.etk.reflect.api.ClassTypeInfo;
import org.etk.reflect.api.MethodInfo;
import org.etk.reflect.api.MethodSignature;
import org.etk.reflect.api.visit.HierarchyScope;
import org.etk.reflect.api.visit.HierarchyVisitor;
import org.etk.reflect.core.AnnotationType;


public class AnnotationIntrospector<A> {

  /** . */
  private final AnnotationType<A, ?> annotationType;

  public AnnotationIntrospector(AnnotationType<A, ?> annotationType) {
    if (annotationType == null) {
      throw new NullPointerException();
    }

    //
    this.annotationType = annotationType;
  }

  private class Blah implements HierarchyVisitor<Blah> {

    LinkedList<AnnotationTarget<ClassTypeInfo, A>> annotations;

    public boolean enter(ClassTypeInfo type) {
      A annotation = type.getDeclaredAnnotation(annotationType);
      if (annotation != null) {
        if (annotations == null) {
          annotations = new LinkedList<AnnotationTarget<ClassTypeInfo,A>>();
        }
        annotations.add(new AnnotationTarget<ClassTypeInfo,A>(type, annotation));
      }
      return true;
    }

    public void leave(ClassTypeInfo type) {
    }
  }

  public AnnotationTarget<ClassTypeInfo, A> resolve(ClassTypeInfo annotatedType) {
    Blah blah = new Blah();
    annotatedType.accept(HierarchyScope.ALL.<Blah>get(), blah);
    return blah.annotations != null ? blah.annotations.getFirst() : null;
  }

  public List<AnnotationTarget<ClassTypeInfo, A>> resolveAll(ClassTypeInfo annotatedType) {
    Blah blah = new Blah();
    annotatedType.accept(HierarchyScope.ALL.<Blah>get(), blah);
    return blah.annotations != null ? Collections.unmodifiableList(blah.annotations) : Collections.<AnnotationTarget<ClassTypeInfo, A>>emptyList();
  }

  private class Bluh implements HierarchyVisitor<Bluh> {

    /** . */
    private final MethodSignature methodSignature;

    private Bluh(MethodSignature methodSignature) {
      this.methodSignature = methodSignature;
    }

    LinkedList<AnnotationTarget<MethodInfo, A>> annotations;

    public boolean enter(ClassTypeInfo type) {
      MethodInfo m = type.getDeclaredMethod(methodSignature);
      if (m != null) {
        A annotation = m.getDeclaredAnnotation(annotationType);
        if (annotation != null) {
          if (annotations == null) {
            annotations = new LinkedList<AnnotationTarget<MethodInfo,A>>();
          }
          annotations.add(new AnnotationTarget<MethodInfo,A>(m, annotation));
        }
      }
      return true;
    }

    public void leave(ClassTypeInfo type) {
    }
  }

  public AnnotationTarget<MethodInfo, A> resolve(MethodInfo method) {
    return resolve(method.getOwner(), method.getSignature());
  }

  public AnnotationTarget<MethodInfo, A> resolve(ClassTypeInfo declaringType, MethodSignature methodSignature) {
    Bluh bluh = new Bluh(methodSignature);
    declaringType.accept(HierarchyScope.ALL.<Bluh>get(), bluh);
    return bluh.annotations != null ? bluh.annotations.getFirst() : null;
  }

  public List<AnnotationTarget<MethodInfo, A>> resolveAll(ClassTypeInfo declaringType, MethodSignature methodSignature) {
    Bluh bluh = new Bluh(methodSignature);
    declaringType.accept(HierarchyScope.ALL.<Bluh>get(), bluh);
    return bluh.annotations != null ? Collections.unmodifiableList(bluh.annotations) : Collections.<AnnotationTarget<MethodInfo, A>>emptyList();
  }
}

