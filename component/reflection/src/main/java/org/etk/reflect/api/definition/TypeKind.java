package org.etk.reflect.api.definition;

import javax.lang.model.type.NoType;

public enum TypeKind {

  /**
   * The pseudo-type corresponding to the keyword {@code void}.
   * @see NoType
   */
	VOID,
	/**
   * A class or interface type.
   */
	CLASS, 
	PARAMETERIZED, 
	 /**
   * A type variable.
   */
	VARIABLE, 
	/**
   * A wildcard type argument.
   */
	WILDCARD, 
	SIMPLE, 
	/**
   * An array type.
   */
	ARRAY

}