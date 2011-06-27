package org.etk.reflect.core;

import java.util.List;

import org.etk.reflect.api.ClassTypeInfo;
import org.etk.reflect.api.ParameterizedTypeInfo;
import org.etk.reflect.api.TypeInfo;
import org.etk.reflect.api.WildcardTypeInfo;


public abstract class AbstractParameterizedTypeInfo<T, M, A, P, F> extends AbstractTypeInfo<T, M, A, P, F> implements ParameterizedTypeInfo {

	  protected AbstractParameterizedTypeInfo(TypeResolverImpl<T, M, A, P, F> domain) {
	    super(domain);
	  }

	  public String getName() {
	    return getRawType().getName();
	  }

	  public boolean isReified() {
	    for (TypeInfo typeArgument : getTypeArguments()) {
	      if (typeArgument instanceof WildcardTypeInfo) {
	        WildcardTypeInfo wildcardTypeArgument = (WildcardTypeInfo)typeArgument;
	        if (wildcardTypeArgument.getLowerBounds().isEmpty()) {
	          switch (wildcardTypeArgument.getUpperBounds().size()) {
	            case 1:
	              TypeInfo ti = wildcardTypeArgument.getUpperBounds().get(0);
	              if (!(ti instanceof ClassTypeInfo)) {
	                break;
	              }
	              ClassTypeInfo cti = (ClassTypeInfo)ti;
	              if (!cti.getName().equals(Object.class.getName())) {
	                break;
	              }
	            case 0:
	              continue;
	            default:
	          }
	        }
	      }
	      return false;
	    }
	    return true;  
	  }

	  public TypeInfo getOwnerType() {
	    throw new UnsupportedOperationException();
	  }

	  public final int hashCode() {
	    int hashCode = getRawType().hashCode();
	    for (TypeInfo typeArgument : getTypeArguments()) {
	      hashCode = hashCode * 43 + typeArgument.hashCode();
	    }
	    return hashCode;
	  }

	  public final boolean equals(Object obj) {
	    if (obj == this) {
	      return true;
	    }
	    if (obj instanceof ParameterizedTypeInfo) {
	      ParameterizedTypeInfo that = (ParameterizedTypeInfo)obj;
	      if (getRawType().equals(that.getRawType())) {
	        List<TypeInfo> typeArguments = getTypeArguments();
	        List<? extends TypeInfo> thatTypeArguments = that.getTypeArguments();
	        if (typeArguments.size() == thatTypeArguments.size()) {
	          int length = typeArguments.size();
	          for (int i = 0;i < length;i++) {
	            TypeInfo typeArgument = typeArguments.get(i);
	            TypeInfo thatTypeArgument = thatTypeArguments.get(i);
	            if (!typeArgument.equals(thatTypeArgument)) {
	              return false;
	            }
	          }
	        }
	        return true;
	      }
	    }
	    return false;
	  }

	  @Override
	  public final String toString() {
	    return "ParameterizedType[rawType" + getRawType() + ",typeArguments=" + getTypeArguments() + "]";
	  }
	}
