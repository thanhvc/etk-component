package org.etk.reflect.api;

import org.etk.reflect.api.annotation.Annotated;

public interface FieldInfo extends Annotated, MemberInfo {

  /**
   * Returns the field type.
   *
   * @return the field type
   */
  TypeInfo getType();

  /**
   * Returns true if method is static, otherwise false.
   *
   * @return true when the method is static
   */
  boolean isStatic();

  /**
   * Returns true if method is final, otheriwse false.
   *
   * @return true when the method is final
   */
  boolean isFinal();
}
