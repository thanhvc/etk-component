package org.etk.reflect.api;

import org.etk.reflect.core.AccessScope;


public interface MemberInfo {

  /**
   * Returns the class type owning this method.
   *
   * @return the class type
   */
  ClassTypeInfo getOwner();

  /**
   * Returns the method name.
   *
   * @return the method name
   */
  String getName();

  /**
   * Returns the access modifier of this method.
   *
   * @return the access scope
   */
  AccessScope getAccess();
}
