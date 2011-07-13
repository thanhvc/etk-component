package org.etk.reflect.core;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

import org.etk.reflect.api.ClassTypeInfo;
import org.etk.reflect.api.annotation.AnnotationInfo;
import org.etk.reflect.api.definition.ClassKind;
import org.etk.reflect.api.metadata.AnnotationMetadata;
/**
 * Anntation delegate which uses for responsibilities to resolve the annotation of <code>ClassInfo</code>, 
 * <code>Method</code>, <code>Field</code>, and <code>Variable</code>.
 * 
 * @author thanh_vucong
 *
 * @param <T> TypeInfo which delegates to resolve.
 * @param <M> MethodInfo which delegates to resolve.
 * @param <A> AnnotationInfo which delegates to resolve
 * @param <P> 
 * @param <F> FieldInfo which delegates to resolve
 * @param <E> Element which provides to resolve.
 */
public class AnnotatedDelegate<T, M, A, P, F, E> extends ReflectedObject<T, M, A, P, F>{

  final AnnotationMetadata<T, E, A, P> annotationModel;
  
  private final E element;
  
  private Map<ClassTypeInfo, AnnotationInfo> annotations;
  
  public AnnotatedDelegate(TypeResolverImpl<T, M, A, P, F> domain, 
                           AnnotationMetadata<T, E, A, P> annotationModel,
                           E element) {
    super(domain);
    
    this.annotationModel = annotationModel;
    this.element = element;
  }
  /**
   * Retrieve these annotations which were declared in the method, field, or class.
   * @param <AN> Generic type of Annotation
   * @param element
   * @param annotationType
   * @return
   */
  public <AN> AN getDeclaredAnnotation(E element, AnnotationType<AN, ?> annotationType) {
	  Object type = annotationType.getType();
	  if (type instanceof Class<?>) {
		  return (AN) annotationModel.resolveDeclaredAnnotation(element, (Class<Annotation>)type);
	  } else if (type instanceof ClassTypeInfo) {
		  ClassTypeInfo cti = (ClassTypeInfo) type;
		  if (cti.getKind() != ClassKind.ANNOTATION) {
			  throw new IllegalArgumentException("The provided class type info is not an annotation.");
		  }
		  if (annotations == null) {
			  Map<ClassTypeInfo, AnnotationInfo> annotations = new HashMap<ClassTypeInfo, AnnotationInfo>();
			  for(A a : annotationModel.getDeclaredAnnotation(element)) {
				  T ant = annotationModel.getAnnotationType(a);
				  ClassTypeInfo aaaa = (ClassTypeInfo) domain.resolve(ant);
				  annotations.put(aaaa, new AnnotationInfoImpl<T, M, A, P, F, E>(this, aaaa, a));
			  }
			  this.annotations = annotations;
		  }
		  return (AN) annotations.get(cti);
	  } else {
		  throw new AssertionError();
	  }
  }
}
