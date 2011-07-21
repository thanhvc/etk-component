package org.etk.reflect.api;

import org.etk.reflect.api.definition.LiteralKind;

/**
 * A simple type denotes either a reference type or a primitive type. 
 * It represents a type subject to boxing and unboxing.
 * 
 * @author Thanh_VuCong
 *
 */
public interface SimpleTypeInfo extends ClassTypeInfo {

	  LiteralKind getLiteralType();

	  boolean isPrimitive();

	}
