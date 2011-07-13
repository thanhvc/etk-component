package org.etk.reflect.jlr.metadata;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;

import org.etk.reflect.api.metadata.AnnotationMetadata;
import org.etk.reflect.api.metadata.FieldMetadata;
import org.etk.reflect.api.metadata.MethodMetadata;
import org.etk.reflect.api.metadata.ReflectionMetadata;
import org.etk.reflect.api.metadata.TypeMetadata;
import org.etk.reflect.core.AccessScope;

public class JLReflectionMetadata implements ReflectionMetadata<Type, Method, Annotation, Method, Field> {
  
  public static JLReflectionMetadata newInstance() {
    return instance;
  }
  
  private final static JLReflectionMetadata instance = new JLReflectionMetadata();
  
  private final JLReflectionTypeMetadata typeModel = new JLReflectionTypeMetadata();
  
  /** . */
  private final JLReflectionFieldMetadata fieldModel = new JLReflectionFieldMetadata();

  /** . */
  private final JLReflectionMethodMetadata methodModel = new JLReflectionMethodMetadata();

  /** . */
  private final JLReflectionTypeAnnotationMetadata typeAnnotationModel = new JLReflectionTypeAnnotationMetadata();

  /** . */
  private final JLReflectionFieldAnnotationMetadata fieldAnnotationModel = new JLReflectionFieldAnnotationMetadata();

  /** . */
  private final JLReflectionMethodAnnotationMetadata methodAnnotationModel = new JLReflectionMethodAnnotationMetadata();

  
  public TypeMetadata<Type> getTypeModel() {
    return typeModel;
  }

  public FieldMetadata<Type, Field> getFieldModel() {
    return fieldModel;
  }

  public MethodMetadata<Type, Method> getMethodModel() {
    return methodModel;
  }

  public AnnotationMetadata<Type, Type, Annotation, Method> getTypeAnnotationMetadata() {
    return typeAnnotationModel;
  }

  public AnnotationMetadata<Type, Method, Annotation, Method> getMethodAnnotationMetadata() {
    return methodAnnotationModel;
  }

  public AnnotationMetadata<Type, Field, Annotation, Method> getFieldAnnotationMetadata() {
    return fieldAnnotationModel;
  }

  static AccessScope getAccess(Member member) {
    int modifiers = member.getModifiers();
    if (Modifier.isPrivate(modifiers)) {
      return AccessScope.PRIVATE;
    } else if (Modifier.isPublic(modifiers)) {
      return AccessScope.PUBLIC;
    } else if (Modifier.isProtected(modifiers)) {
      return AccessScope.PROTECTED;
    } else {
      return AccessScope.PACKAGE_PROTECTED;
    }
  }

  
}
