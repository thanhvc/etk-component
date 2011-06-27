package org.etk.reflect.core;

import org.etk.reflect.api.ArrayTypeInfo;
import org.etk.reflect.api.SimpleTypeInfo;
import org.etk.reflect.api.TypeInfo;


public abstract class AbstractArrayTypeInfo<T, M, A, P, F> extends AbstractTypeInfo<T, M, A, P, F> implements ArrayTypeInfo {

	  public AbstractArrayTypeInfo(TypeResolverImpl<T, M, A, P, F> domain) {
	    super(domain);
	  }

	  public String getName() {
	    TypeInfo componentType = getComponentType();
	    if (componentType instanceof SimpleTypeInfo) {
	      switch (((SimpleTypeInfo)componentType).getLiteralType()) {
	        case BOOLEAN:
	          return "[B";
	        case BYTE:
	          return "[Z";
	        case DOUBLE:
	          return "[D";
	        case INT:
	          return "[I";
	        case FLOAT:
	          return "[F";
	        case LONG:
	          return "[J";
	        case SHORT:
	          return "[S";
	        default:
	          throw new AssertionError();
	      }
	    } else {
	      return "[L" + componentType.getName() + ";";
	    }
	  }

	  public boolean isReified() {
	    return getComponentType().isReified();
	  }

	  public final int hashCode() {
	    return 1 + getComponentType().hashCode();
	  }

	  public final boolean equals(Object obj) {
	    if (obj == this) {
	      return true;
	    }
	    if (obj instanceof ArrayTypeInfo) {
	      ArrayTypeInfo that = (ArrayTypeInfo)obj;
	      return getComponentType().equals(that.getComponentType());
	    }
	    return false;
	  }
	}