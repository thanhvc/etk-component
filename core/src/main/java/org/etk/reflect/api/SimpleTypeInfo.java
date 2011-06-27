package org.etk.reflect.api;

/**
 * A simple type denotes either a reference type or a primitive type. 
 * It represents a type subject to boxing and unboxing.
 * @author Administrator
 *
 */
public interface SimpleTypeInfo extends ClassTypeInfo {

	  LiteralType getLiteralType();

	  boolean isPrimitive();

	}
