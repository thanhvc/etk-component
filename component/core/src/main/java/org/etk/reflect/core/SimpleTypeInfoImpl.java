package org.etk.reflect.core;

import org.etk.reflect.api.LiteralType;
import org.etk.reflect.api.SimpleTypeInfo;



class SimpleTypeInfoImpl<T, M, A, P, F> extends ClassTypeInfoImpl<T, M, A, P, F> implements SimpleTypeInfo {

	  /** . */
	  private final LiteralType literalType;

	  /** . */
	  private final boolean primitive;

	  public SimpleTypeInfoImpl(TypeResolverImpl<T, M, A, P, F> typeDomain, T simpleType) {
	    super(typeDomain, simpleType);

	    //
	    this.literalType = domain.typeModel.getLiteralType(simpleType);
	    this.primitive = domain.typeModel.isPrimitive(simpleType);
	  }

	  public boolean isPrimitive() {
	    return primitive;
	  }

	  public LiteralType getLiteralType() {
	    return literalType;
	  }

	  public boolean equals(Object obj) {
	    if (obj == this) {
	      return true;
	    }
	    if (obj instanceof SimpleTypeInfo) {
	      SimpleTypeInfo that = (SimpleTypeInfo)obj;
	      return literalType == that.getLiteralType() && primitive == that.isPrimitive();
	    }
	    return false;
	  }
	}
