package org.etk.reflect.core;

import org.etk.reflect.api.ClassTypeInfo;
import org.etk.reflect.api.FieldInfo;
import org.etk.reflect.api.TypeInfo;


public class FieldInfoImpl<T, M, A, P, F> extends ReflectedObject<T, M, A, P, F> implements FieldInfo {

  /** . */
  private final ClassTypeInfo owner;

  /** . */
  private final F field;

  /** . */
  private TypeInfo type;

  /** . */
  private final String name;

  /** . */
  private final AccessScope access;

  /** . */
  private final boolean _final;

  /** . */
  private final boolean _static;

  /** . */
  private AnnotatedDelegate<T, M, A, P, F, F> annotatedDelegate;

  public FieldInfoImpl(ClassTypeInfo owner, TypeResolverImpl<T, M, A, P, F> domain, F field) {
    super(domain);

    //
    this.owner = owner;
    this.field = field;
    this.type = null;
    this.name = domain.fieldMetadata.getName(field);
    this.access = domain.fieldMetadata.getAccess(field);
    this._final = domain.fieldMetadata.isFinal(field);
    this._static = domain.fieldMetadata.isStatic(field);
    this.annotatedDelegate = null;
  }

  public ClassTypeInfo getOwner() {
    return owner;
  }

  public TypeInfo getType() {
    if (type == null) {
      T ft = domain.fieldMetadata.getType(field);
      type = domain.resolve(ft);
    }
    return type;
  }

  public <AT> AT getDeclaredAnnotation(AnnotationType<AT, ?> annotationType) {
    if (annotatedDelegate == null) {
      annotatedDelegate = new AnnotatedDelegate<T, M, A, P, F, F>(
        domain,
        domain.fieldAnnotationMetadata,
        field);
    }
    return annotatedDelegate.getDeclaredAnnotation(field, annotationType);
  }

  public String getName() {
    return name;
  }

  public AccessScope getAccess() {
    return access;
  }

  public boolean isStatic() {
    return _static;
  }

  public boolean isFinal() {
    return _final;
  }
}
