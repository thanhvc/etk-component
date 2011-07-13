package org.etk.reflect.core;

import java.util.List;

import org.etk.reflect.api.TypeInfo;
import org.etk.reflect.api.WildcardTypeInfo;

abstract class AbstractWildcardType<T, M, A, P, F> extends AbstractTypeInfo<T, M, A, P, F> implements WildcardTypeInfo {

	  public AbstractWildcardType(TypeResolverImpl<T, M, A, P, F> domain) {
	    super(domain);
	  }

	  public String getName() {
	    List<TypeInfo> bounds = getUpperBounds();
	    if (bounds.isEmpty()) {
	      return Object.class.getName();
	    } else {
	      return bounds.get(0).getName();
	    }
	  }

	  public List<TypeInfo> getUpperBounds() {
	    return null;
	  }

	  public boolean isReified() {
	    throw new UnsupportedOperationException("Does it make sense?");
	  }

	  public final int hashCode() {
	    return getUpperBounds().hashCode() ^ getLowerBounds().hashCode();
	  }

	  public final boolean equals(Object obj) {
	    if (obj == this) {
	      return true;
	    }
	    if (obj instanceof WildcardTypeInfo) {
	      WildcardTypeInfo that = (WildcardTypeInfo)obj;
	      return getUpperBounds().equals(that.getUpperBounds()) && getLowerBounds().equals(that.getLowerBounds());
	    }
	    return false;
	  }
	}
