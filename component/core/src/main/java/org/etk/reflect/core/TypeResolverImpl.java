package org.etk.reflect.core;

import java.util.HashMap;
import java.util.Map;

import org.etk.reflect.api.TypeInfo;
import org.etk.reflect.api.TypeResolver;
import org.etk.reflect.spi.model.AnnotationModel;
import org.etk.reflect.spi.model.FieldModel;
import org.etk.reflect.spi.model.MethodModel;
import org.etk.reflect.spi.model.ReflectionModel;
import org.etk.reflect.spi.model.TypeKind;
import org.etk.reflect.spi.model.TypeModel;


public class TypeResolverImpl<T, M, A, P, F> implements TypeResolver<T> {

	public static <T, M, A, P, F> TypeResolver<T> create(ReflectionModel<T, M, A, P, F> model) {
		return new TypeResolverImpl<T, M, A, P, F>(model, true);
	}
	
	public static <T, M, A, P, F> TypeResolver<T> create(ReflectionModel<T, M, A, P, F> model, boolean cache) {
		return new TypeResolverImpl<T, M, A, P, F>(model, cache);
	}
	
	/**
	 * Define the typeOfModel
	 */
	final TypeModel<T> typeModel;
	/**
	 * Define the fields which belong the class.
	 */
	final FieldModel<T, F> fieldModel;
	
	final MethodModel<T, M> methodModel;
	final AnnotationModel<T, T, A, P> typeAnnotationModel;
	final AnnotationModel<T, F, A, P> fieldAnnotationModel;
	final AnnotationModel<T, M, A, P> methodAnnotationModel;
	final Map<T, AbstractTypeInfo<T, M, A, P, F>> cache;
	
	private TypeResolverImpl(ReflectionModel<T, M, A, P, F> model, boolean cache) {
		this.typeModel = model.getTypeModel();
		this.fieldModel = model.getFieldModel();
		this.methodModel = model.getMethodModel();
		this.typeAnnotationModel = model.getTypeAnnotationModel();
		this.fieldAnnotationModel = model.getFieldAnnotationModel();
		this.methodAnnotationModel = model.getMethodAnnotationModel();
		this.cache = cache ? new HashMap<T, AbstractTypeInfo<T, M, A, P, F>>() : null;
	}
	
	public TypeInfo resolve(T type) throws NullPointerException {
		if (type == null) {
			throw new NullPointerException("No null type accepted");
		}
		return _getType(type);
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
	AbstractTypeInfo<T, M, A, P, F> _getType(T type) {
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
		TypeKind kind = typeModel.getKind(type);
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
			throw new AssertionError("Cannot handle type " + type
					+ " with kind " + kind);
		}
	}
	
	
}
