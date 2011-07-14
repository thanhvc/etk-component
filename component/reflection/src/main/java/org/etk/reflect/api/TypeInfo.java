package org.etk.reflect.api;

/**
 * The TypeInfo interface represents a type referenced to <code>Class</code> and
 * <code>Variable</code> classes, specified in the class reflection with the BuilderTypeInfo.
 * 
 * @author thanh_vucong
 *
 */
public interface TypeInfo {

  /**
   * Returns true if the type is reified.
   *
   * @return the reifiability
   */
  boolean isReified();

  /**
   * Returns the name of the erased type corresponding of this type.
   *
   * @return the name of the erased type
   */
  String getName();

  /**
   * Unwraps the underlying type object and returns it
   *
   * @return the wrapped type object
   */
  Object unwrap();

 
  /**
   * Returns true if the current type is a sub type of the argument.
   *
   * @param typeInfo the type to test
   * @return true if this type is a subtype of the argument
   */
  boolean isSubType(TypeInfo typeInfo);
  
  /**
   * Visit the type info.
   *
   * @param strategy the strategy
   * @param visitor the visitor
   * @param <V> the visitor generic type
   * @param <S> the strategy generic type
   */
  <V extends Visitor<V, S>, S extends VisitorStrategy<V, S>> void accept(S strategy, V visitor);


}
