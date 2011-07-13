package org.etk.reflect.api;

import java.util.List;

public interface TypeVariableInfo extends TypeInfo {
  
  /**
   * Return the bound list
   * @return the bound list
   */
  List<TypeInfo> getBounds();
  
  /**
   * Return the related generic declaration
   * @return the generic declaration
   */
  GenericDeclarationInfo getGenericDeclaration();
  

  /**
   * Returns the name of the variable.
   * @return the variable name
   */
  String getVariableName();
}
