package org.etk.reflect.core;

import java.util.ArrayList;
import java.util.List;

import org.etk.reflect.api.TypeInfo;


class WildcardTypeInfoImpl<T, M, A, P, F> extends AbstractWildcardType<T, M, A, P, F> {

	  /** . */
	  private final T wildcardType;
	  
	  /** . */
	  private List<TypeInfo> upperBounds;

	  /** . */
	  private List<TypeInfo> lowerBounds;

	  public WildcardTypeInfoImpl(TypeResolverImpl<T, M, A, P, F> domain, T wildcardType) {
	    super(domain);

	    //
	    this.wildcardType = wildcardType;
	  }

	  public T unwrap() {
	    return wildcardType;
	  }

	  public List<TypeInfo> getUpperBounds() {
	    if (upperBounds == null) {
	      List<TypeInfo> upperBounds = new ArrayList<TypeInfo>();
	      for (T upperBound : domain.typeMetadata.getUpperBounds(wildcardType)) {
	        upperBounds.add(domain.resolve(upperBound));
	      }
	      this.upperBounds = upperBounds;
	    }
	    return upperBounds;
	  }

	  public List<TypeInfo> getLowerBounds() {
	    if (lowerBounds == null) {
	      List<TypeInfo> lowerBounds = new ArrayList<TypeInfo>();
	      for (T lowerBound : domain.typeMetadata.getLowerBounds(wildcardType)) {
	        lowerBounds.add(domain.resolve(lowerBound));
	      }
	      this.lowerBounds = lowerBounds;
	    }
	    return lowerBounds;
	  }
	}
