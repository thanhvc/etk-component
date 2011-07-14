package org.etk.reflect.api;

import java.util.List;

/**
 * WildcardType represents a wildcard type expression, such as ?, ? extends
 * Number, or ? super Integer.
 * 
 * @author thanh_vucong
 */
public interface WildcardTypeInfo extends TypeInfo {

  /**
   * Returns an array of Type objects representing the upper bound(s) of this
   * type variable.
   * <p>
   * Note that if no upper bound is explicitly declared, the upper bound is
   * Object.
   * 
   * @return
   */
  List<TypeInfo> getUpperBounds();

  /**
   * Returns an array of Type objects representing the lower bound(s) of this
   * type variable.
   * <p>
   * Note that if no lower bound is explicitly declared, the lower bound is the
   * type of null. In this case, a zero length array is returned.
   * 
   * @return
   */
  List<TypeInfo> getLowerBounds();

}
