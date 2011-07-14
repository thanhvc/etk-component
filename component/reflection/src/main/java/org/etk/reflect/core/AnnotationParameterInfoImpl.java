package org.etk.reflect.core;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

import org.etk.reflect.api.ClassTypeInfo;
import org.etk.reflect.api.TypeInfo;
import org.etk.reflect.api.annotation.AnnotationParameterInfo;
import org.etk.reflect.api.annotation.AnnotationParameterType;
import org.etk.reflect.api.definition.LiteralKind;
import org.etk.reflect.api.definition.TypeKind;

public class AnnotationParameterInfoImpl<T, M, A, P, F, E, Z> implements AnnotationParameterInfo<Z> {
	private static final EnumMap<LiteralKind, AnnotationParameterType<?>> literalToAnnotationParameterType;
	
	static {
		EnumMap<LiteralKind, AnnotationParameterType<?>> tmp = new EnumMap<LiteralKind, AnnotationParameterType<?>>(LiteralKind.class);
		tmp.put(LiteralKind.BOOLEAN, AnnotationParameterType.BOOLEAN);
		tmp.put(LiteralKind.INT, AnnotationParameterType.INTEGER);
		tmp.put(LiteralKind.DOUBLE, AnnotationParameterType.DOUBLE);
		tmp.put(LiteralKind.BYTE, AnnotationParameterType.BYTE);
		tmp.put(LiteralKind.SHORT, AnnotationParameterType.SHORT);
		tmp.put(LiteralKind.LONG, AnnotationParameterType.LONG);
		tmp.put(LiteralKind.FLOAT, AnnotationParameterType.FLOAT);
		literalToAnnotationParameterType = tmp;
	}
	
	private final AnnotatedDelegate<T, M, A, P, F, E> owner;
	
	private final A annotation;
	private final P parameter;
	private final String name;
	private final AnnotationParameterType<Z> type;
	
	private final boolean multiValued;
	
	private List<Z> values;
	
	public AnnotationParameterInfoImpl(AnnotatedDelegate<T, M, A, P, F, E> owner, A annotation, P parameter) {

	    //
	    String name = owner.annotationModel.getAnnotationParameterName(parameter);

	    //
	    AnnotationParameterType<?> type;

	    // *[] : unwrap component type
	    T parameterType = owner.annotationModel.getAnnotationParameterType(parameter);
	    TypeKind parameterTypeKind = owner.domain.typeMetadata.getKind(parameterType);
	    boolean multiValued;
	    if (parameterTypeKind == TypeKind.ARRAY) {
	      parameterType = owner.domain.typeMetadata.getComponentType(parameterType);
	      parameterTypeKind = owner.domain.typeMetadata.getKind(parameterType);
	      multiValued = true;
	    } else {
	      multiValued = false;
	    }

	    //
	    switch (parameterTypeKind) {
	      case SIMPLE:
	        LiteralKind parameterLiteralType = owner.domain.typeMetadata.getLiteralType(parameterType);
	        type = literalToAnnotationParameterType.get(parameterLiteralType);
	        break;
	      case PARAMETERIZED:
	        // We do handler Class<*> type
	        T rawParameterType = owner.domain.typeMetadata.getRawType(parameterType);
	        String className = owner.domain.typeMetadata.getClassName(rawParameterType);
	        if (!className.equals(Class.class.getName())) {
	          throw new AssertionError("Does not make any sense");
	        }
	        type = AnnotationParameterType.CLASS;
	        break;
	      case CLASS:
	        switch (owner.domain.typeMetadata.getClassKind(parameterType)) {
	          case CLASS:
	            String parameterClassName = owner.domain.typeMetadata.getClassName(parameterType);
	            if (parameterClassName.equals(String.class.getName())) {
	              type = AnnotationParameterType.STRING;
	            } else if (parameterClassName.equals(Class.class.getName())) {
	              type = AnnotationParameterType.CLASS;
	            } else {
	              throw new UnsupportedOperationException("Unsupported parameter class " + parameterClassName);
	            }
	            break;
	          case ENUM:
	            type = AnnotationParameterType.ENUM;
	            break;
	          case ANNOTATION:
	            type = AnnotationParameterType.ANNOTATION;
	            break;
	          default:
	            throw new UnsupportedOperationException();
	        }
	        break;
	      default:
	        throw new UnsupportedOperationException("Unsupported parameter type kind " + parameterTypeKind + " for parameter " + parameter);
	    }

	    //
	    this.owner = owner;
	    this.annotation = annotation;
	    this.parameter = parameter;
	    this.name = name;
	    this.type = (AnnotationParameterType<Z>)type;
	    this.values = null;
	    this.multiValued = multiValued;
	  }

	  public String getName() {
	    return name;
	  }

	  public AnnotationParameterType<Z> getType() {
	    return type;
	  }

	  public boolean isMultiValued() {
	    return multiValued;
	  }

	  public List<Z> getValues() {
	    if (values == null) {
	      List<?> annotationParameterValue = owner.annotationModel.getAnnotationParameterValue(annotation, parameter);
	      if (type == AnnotationParameterType.CLASS) {
	        ArrayList<Object> abc = new ArrayList<Object>(annotationParameterValue.size());
	        for (Object anAnnotationParameterValue : annotationParameterValue) {
	          T typeValue = (T)anAnnotationParameterValue;
	          TypeInfo typeValueTI = owner.domain.resolve(typeValue);
	          abc.add(typeValueTI);
	        }
	        values = (List<Z>)abc;
	      } else if (type == AnnotationParameterType.ANNOTATION) {
	        ArrayList<Object> abc = new ArrayList<Object>(annotationParameterValue.size());
	        for (Object anAnnotationParameterValue : annotationParameterValue) {
	          A valueAnnotation = (A)anAnnotationParameterValue;
	          T valueAnnotationType = owner.annotationModel.getAnnotationType(valueAnnotation);
	          ClassTypeInfo valueAnnotationTI = (ClassTypeInfo)owner.domain.resolve(valueAnnotationType);
	          AnnotationInfoImpl<T, M, A, P, F, E> valueAI = new AnnotationInfoImpl<T, M, A, P, F, E>(owner, valueAnnotationTI, (A)valueAnnotation);
	          abc.add(valueAI);
	        }
	        values = (List<Z>)abc;
	      } else {
	        values = (List<Z>)annotationParameterValue;
	      }
	    }
	    return values;
	  }

	  public Z getValue() {
	    List<Z> values = getValues();
	    return values.isEmpty() ? null : values.get(0);
	  }
	}
