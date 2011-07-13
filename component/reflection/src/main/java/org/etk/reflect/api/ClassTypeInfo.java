package org.etk.reflect.api;

import java.util.Collection;
import java.util.List;

import org.etk.reflect.api.annotation.Annotated;
import org.etk.reflect.api.definition.ClassKind;


/**
 * 
 * @author thanh_vucong
 *
 */
public interface ClassTypeInfo extends TypeInfo, GenericDeclarationInfo, Annotated {

  /**
   * Return the name with package name of the class which represented by the <code>ClassTypeInfo</code> 
   * as a <tt>String</tt>
   * @return
   */
  String getName();
  /**
   * 
   * @return
   */
  String getSimpleName();
  
  ClassTypeInfo getEnclosing();
  
  /**
   * 
   * @return
   */
  String getPackageName();
  /**
   * 
   * @return
   */
  ClassKind getKind();
  
  Iterable<TypeInfo> getInterfaces();
  
  /**
   * 
   * @return
   */
  TypeInfo getSuperType();
  /**
   * 
   * @return
   */
  ClassTypeInfo getSuperClass();
  /**
   * 
   * @param type
   * @return
   */
  TypeInfo resolve(TypeInfo type);
  /**
   * Retrieving the Methods which belong to the current class.
   * @return
   */
  List<MethodInfo> getDeclaredMethods();
  /**
   * Retrieving the MethodInfo base on the MethodSignature.
   * @param signature
   * @return
   */
  MethodInfo getDeclaredMethod(MethodSignature signature);
  /**
   * Retrieving the Fields which belong to the current classType
   * @return
   */
  Collection<FieldInfo> getDeclaredFields();
  /**
   * Retrieving the Field base on the fieldName
   * @param fieldName
   * @return
   */
  FieldInfo getDeclaredField(String fieldName);
  /**
   * 
   * @param that
   * @return
   */
  boolean isAssignableFrom(ClassTypeInfo that);
}
