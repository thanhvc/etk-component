package org.etk.reflect.api.introspection;

/**
 * The target of an annotation.
 *
 * @param <T> the target type
 * @param <A> the annotation type
 */
public class AnnotationTarget<T, A> {

  /** . */
  private final T target;

  /** . */
  private final A annotation;

  /**
   * Create a new annotated target.
   *
   * @param target the target
   * @param annotation the annotation
   * @throws NullPointerException if any argument is null
   */
  public AnnotationTarget(T target, A annotation) throws NullPointerException {
    if (target == null) {
      throw new NullPointerException("No null target accepted");
    }
    if (annotation == null) {
      throw new NullPointerException("No null annotation accepted");
    }

    //
    this.target = target;
    this.annotation = annotation;
  }

  public T getTarget() {
    return target;
  }

  public A getAnnotation() {
    return annotation;
  }
}

