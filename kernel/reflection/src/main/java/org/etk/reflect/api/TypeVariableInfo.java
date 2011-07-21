package org.etk.reflect.api;

import java.util.List;

/**
 * TypeVariableInfo is the common superinterface for type variables of kinds. A type
 * variable is created the first time it is needed by a reflective method, as
 * specified in this package. If a type variable t is referenced by a type (i.e,
 * class, interface or annotation type) T, and T is declared by the nth
 * enclosing class of T (see JLS 8.1.2), then the creation of t requires the
 * resolution (see JVMS 5) of the ith enclosing class of T, for i = 0 to n,
 * inclusive. Creating a type variable must not cause the creation of its
 * bounds. Repeated creation of a type variable has no effect.
 * 
 * @author thanh_vucong
 */
public interface TypeVariableInfo extends TypeInfo {

  /**
   * Returns an array of Type objects representing the upper bound(s) of this
   * type variable. Note that if no upper bound is explicitly declared, the
   * upper bound is Object.
   * 
   * @return the bound list
   */
  List<TypeInfo> getBounds();

  /**
   * Return the related generic declaration
   * 
   * @return the generic declaration
   */
  GenericDeclarationInfo getGenericDeclaration();

  /**
   * Returns the name of the variable.
   * 
   * @return the variable name
   */
  String getVariableName();
}
