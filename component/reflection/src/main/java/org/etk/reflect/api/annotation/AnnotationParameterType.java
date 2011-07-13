package org.etk.reflect.api.annotation;

import org.etk.reflect.api.ClassTypeInfo;

/**
 * Define the AnnotationParameterType consist of INTEGER, STRING, BOOLEAN, CLASS_TYPE_INFO, DOUBLE, SHORT, LONG, ENUM, ANNOTATION. 
 * @author thanh_vucong
 *
 * @param <T>
 */
public class AnnotationParameterType<T> {
  private final Class<T> javaType;
  
  private AnnotationParameterType(Class<T> javaType) {
    this.javaType = javaType;
  }
  
  public static final AnnotationParameterType<String> STRING = new AnnotationParameterType<String>(String.class);
  
  public static final AnnotationParameterType<Integer> INTEGER = new AnnotationParameterType<Integer>(Integer.class);
  
  public static final AnnotationParameterType<Long> LONG = new AnnotationParameterType<Long>(Long.class);
  
  public static final AnnotationParameterType<Boolean> BOOLEAN = new AnnotationParameterType<Boolean>(Boolean.class);
  
  public static final AnnotationParameterType<Float> FLOAT = new AnnotationParameterType<Float>(Float.class);
  
  public static final AnnotationParameterType<Byte> BYTE = new AnnotationParameterType<Byte>(Byte.class);
  
  public static final AnnotationParameterType<Short> SHORT = new AnnotationParameterType<Short>(Short.class);
  
  public static final AnnotationParameterType<Double> DOUBLE = new AnnotationParameterType<Double>(Double.class);
  
  public static final AnnotationParameterType<String> ENUM = new AnnotationParameterType<String>(String.class);
  
  public static final AnnotationParameterType<ClassTypeInfo> CLASS = new AnnotationParameterType<ClassTypeInfo>(ClassTypeInfo.class);
  
  public static final AnnotationParameterType<AnnotationInfo> ANNOTATION = new AnnotationParameterType<AnnotationInfo>(AnnotationInfo.class);
  

}

