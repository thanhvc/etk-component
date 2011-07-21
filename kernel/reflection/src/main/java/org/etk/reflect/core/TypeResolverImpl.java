package org.etk.reflect.core;

import java.util.HashMap;
import java.util.Map;

import org.etk.reflect.api.TypeInfo;
import org.etk.reflect.api.TypeResolver;
import org.etk.reflect.api.definition.TypeKind;
import org.etk.reflect.api.metadata.AnnotationMetadata;
import org.etk.reflect.api.metadata.FieldMetadata;
import org.etk.reflect.api.metadata.MethodMetadata;
import org.etk.reflect.api.metadata.ReflectionMetadata;
import org.etk.reflect.api.metadata.TypeMetadata;


public class TypeResolverImpl<T, M, A, P, F> implements TypeResolver<T> {

	public static <T, M, A, P, F> TypeResolver<T> create(ReflectionMetadata<T, M, A, P, F> model) {
		return new TypeResolverImpl<T, M, A, P, F>(model, true);
	}
	
	public static <T, M, A, P, F> TypeResolver<T> create(ReflectionMetadata<T, M, A, P, F> model, boolean cache) {
		return new TypeResolverImpl<T, M, A, P, F>(model, cache);
	}
	
	/**
	 * Define the typeOfModel
	 */
	final TypeMetadata<T> typeMetadata;
	/**
	 * Define the fields which belong the class.
	 */
	final FieldMetadata<T, F> fieldMetadata;
	
	final MethodMetadata<T, M> methodMetadata;
	final AnnotationMetadata<T, T, A, P> typeAnnotationMetadata;
	final AnnotationMetadata<T, F, A, P> fieldAnnotationMetadata;
	final AnnotationMetadata<T, M, A, P> methodAnnotationMetadata;
	final Map<T, AbstractTypeInfo<T, M, A, P, F>> cache;
	
	private TypeResolverImpl(ReflectionMetadata<T, M, A, P, F> model, boolean cache) {
		this.typeMetadata = model.getTypeModel();
		this.fieldMetadata = model.getFieldModel();
		this.methodMetadata = model.getMethodModel();
		this.typeAnnotationMetadata = model.getTypeAnnotationMetadata();
		this.fieldAnnotationMetadata = model.getFieldAnnotationMetadata();
		this.methodAnnotationMetadata = model.getMethodAnnotationMetadata();
		this.cache = cache ? new HashMap<T, AbstractTypeInfo<T, M, A, P, F>>() : null;
	}
	
	public TypeInfo resolve(T type) throws NullPointerException {
		if (type == null) {
			throw new NullPointerException("No null type accepted");
		}
		return getType(type);
	}
	
	public void clear() {
		if (cache != null) {
			cache.clear();
		}
	}
	
	/**
	 * Getting the TypeInfo base on the type parameter.
	 * @param type
	 * @return AbstractTypeInfo
	 */
	AbstractTypeInfo<T, M, A, P, F> getType(T type) {
		if (cache == null) {
			return build(type);
		} else {
			AbstractTypeInfo<T, M, A, P, F> cached = cache.get(type);
			if (cached == null) {
				cached = build(type);
				cache.put(type, cached);
			}
			return cached;
		}
	}
	
	/**
	 * Building the InfoType base on the type parameter.
	 * @param type
	 * @return
	 */
	private AbstractTypeInfo<T, M, A, P, F> build(T type) {
	  //Determines the TypeKind of a given TypeInfo.
		TypeKind kind = typeMetadata.getKind(type);
		switch (kind) {
		case SIMPLE:
			return new SimpleTypeInfoImpl<T, M, A, P, F>(this, type);
		case VOID:
			return new VoidTypeInfoImpl<T, M, A, P, F>(this, type);
		case CLASS:
			return new ClassTypeInfoImpl<T, M, A, P, F>(this, type);
		case PARAMETERIZED:
			return new ParameterizedTypeInfoImpl<T, M, A, P, F>(this, type);
		case VARIABLE:
			return new TypeVariableInfoImpl<T, M, A, P, F>(this, type);
		case WILDCARD:
			return new WildcardTypeInfoImpl<T, M, A, P, F>(this, type);
		case ARRAY:
			return new ArrayTypeInfoImpl<T, M, A, P, F>(this, type);
		default:
			throw new AssertionError("Cannot handle type " + type + " with kind " + kind);
		}
	}
	
	
}
