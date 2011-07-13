package org.etk.reflect.api.metadata;

import org.etk.reflect.core.AccessScope;

/**
 * A <code>FieldMetadata</code> provides information about, and dynamic access to, 
 * a single field of a class or an interface. The reflected field may be a class(static)
 * field or an instance field.
 * 
 * @author thanh_vucong
 *
 * @param <T> TypeInfo
 * @param <F> FieldInfo.
 */
public interface FieldMetadata<T, F> {

  /**
   * Identifies the set of members of a class or interface. 
   * Inherited members are not included.
   * 
   * @param classType
   * @return
   */
  Iterable<F> getDeclaredFields(T classType);
  /**
   * Returns the simple name of member or constructor represented 
   * by this Field.
   * 
   * @param f Field which provides to get the name.
   * @return The simple file name
   */
  String getName(F f);
  /**
   * Returns the Java language modifiers for the field represented 
   * by this <code>FieldMetadata</code> object.
   * 
   * @param f
   * @return
   */
  AccessScope getAccess(F f);
  
  /**
   * Return a Class object that identifies the declared type for the field 
   * represented by this <code>FieldMetadata</code> object
   * 
   * @param f Field which provides to get Type.
   * @return
   */
  T getType(F f);
  
  boolean isStatic(F f);
  
  boolean isFinal(F f);
  
  T getOwner(F f);
}
