package org.etk.reflect.spi.model;

import org.etk.reflect.api.ClassKind;
import org.etk.reflect.api.LiteralType;

/**
 * Define the TypeModel represents to generic model.
 * @author thanh_vucong
 *
 * @param <T>
 */
public interface TypeModel<T> {

	TypeKind getKind(T type);
	LiteralType getLiteralType(T simpleType);
	
	boolean isPrimitive(T simpleType);
	
	String getClassName(T classType);
	T getEnclosing(T classType);
	Iterable<T> getInterfaces(T classType);
	T getSuperClass(T classType);
	ClassKind getClassKind(T classType);
	Iterable<T> getTypeParameters(T classType);
	String getName(T typeVariable);
	T getComponentType(T arrayType);
	T getGenericDeclaration(T typeVariable);
	GenericDeclarationKind getGenericDeclarationKind(T typeVariable);
	T getRawType(T parameterizedType);
	Iterable<T> getTypeArguments(T parameterizedType);
	Iterable<T> getBounds(T typeVariable);
	
	Iterable<T> getUpperBounds(T wildcardType);

	Iterable<T> getLowerBounds(T wildcardType);
}
