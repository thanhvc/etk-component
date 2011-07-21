package org.etk.reflect.core;

import org.etk.reflect.api.SimpleTypeInfo;
import org.etk.reflect.api.definition.LiteralKind;

/**
 * 
 * @author thanh_vucong
 *
 * @param <T>
 * @param <M>
 * @param <A>
 * @param <P>
 * @param <F>
 */
class SimpleTypeInfoImpl<T, M, A, P, F> extends ClassTypeInfoImpl<T, M, A, P, F> implements SimpleTypeInfo {

	  /** . */
	  private final LiteralKind literalType;

	  /** . */
	  private final boolean primitive;

	  public SimpleTypeInfoImpl(TypeResolverImpl<T, M, A, P, F> typeDomain, T simpleType) {
	    super(typeDomain, simpleType);

	    //
	    this.literalType = domain.typeMetadata.getLiteralType(simpleType);
	    this.primitive = domain.typeMetadata.isPrimitive(simpleType);
	  }

	  public boolean isPrimitive() {
	    return primitive;
	  }

	  public LiteralKind getLiteralType() {
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
