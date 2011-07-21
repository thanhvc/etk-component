package org.etk.reflect.api;

import java.util.List;

import org.etk.reflect.api.annotation.Annotated;
import org.etk.reflect.api.definition.MethodKind;



public interface MethodInfo extends GenericDeclarationInfo, Annotated, MemberInfo {

  /**
   * Unwraps the method underlying object and returns it.
   *
   * @return the wrapped method
   */
  Object unwrap();

  /**
   * Returns the return type.
   *
   * @return the return type
   */
  TypeInfo getReturnType();

  /**
   * Returns the list of type parameter of this method.
   *
   * @return the parameter types
   */
  List<TypeInfo> getParameterTypes();

  /**
   * Returns the parameter names of the method or null if the parameter names cannot be determined.
   *
   * @return the method parameter names
   */
  List<String> getParameterNames();

  /**
   * Returns the method signature.
   *
   * @return the method signature
   */
  MethodSignature getSignature();

  /**
   * <p>Resolves a type signature with a provided class type context.</p>
   *
   * <p>
   * For instance, let's consider the following class:
   *
   * public class A implements java.util.List<String> {
   *    ...
   *    public boolean add(String e) { ... }
   *    ...
   * }
   *
   * Whenever you resolve the method {@link List#add(Object)} from the {@link List} interface
   * within the context of the <tt>A</tt> class, you will obtain the signature
   * <tt>add(String e)</tt> instead of the <tt>add(Object e)</tt> signature.</pp>
   *
   * @param context the class type context
   * @return the signature with respect to the provided context
   */
  MethodSignature getSignature(ClassTypeInfo context);

  /**
   * Returns true when the method {@link #getType()} returns the value {@link MethodKind#ABSTRACT}.
   *
   * @see #getType()
   * @return true when the method is abstract
   */
  boolean isAbstract();

  /**
   * Returns true when the method {@link #getType()} returns the value {@link MethodKind#NATIVE}.
   *
   * @see #getType()
   * @return true when the method is native
   */
  boolean isNative();

  /**
   * Returns true when the method {@link #getType()} returns the value {@link MethodKind#CONCRETE}.
   *
   * @see #getType()
   * @return true when the method is concrete
   */
  boolean isConcrete();

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

  /**
   * Returns the method type.
   *
   * @return the method type
   */
  MethodKind getType();

  /**
   *
   * @return
   */
  List<ClassTypeInfo> getThrownTypes();
}
