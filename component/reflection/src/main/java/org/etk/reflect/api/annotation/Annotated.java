package org.etk.reflect.api.annotation;

import org.etk.reflect.core.AnnotationType;

/**
 * The Annotated interface represents the annotation references to <code>Class</code>,
 * <code>Field</code>, and <code>Method</code>
 * @author thanh_vucong
 *
 */
public interface Annotated {

  <A> A getDeclaredAnnotation(AnnotationType<A, ?> annotationType);
}
