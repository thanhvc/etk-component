package org.etk.reflect.api.relationship;

import org.etk.reflect.api.TypeInfo;



public interface TypeRelationship<T1 extends TypeInfo, T2 extends TypeInfo>  {

  boolean isSatisfied(T1 t1, T2 t2);

  public static final TypeRelationship<TypeInfo, TypeInfo> SUB_TYPE = new SubType();
}
