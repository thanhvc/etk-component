package org.etk.reflect.core;

import java.util.ArrayList;
import java.util.List;

import org.etk.reflect.api.ParameterizedTypeInfo;
import org.etk.reflect.api.TypeInfo;


class ParameterizedTypeInfoImpl<T, M, A, P, F> extends AbstractParameterizedTypeInfo<T, M, A, P, F> implements ParameterizedTypeInfo {

	  /** . */
	  private final T type;

	  /** . */
	  private TypeInfo rawType;

	  /** . */
	  private List<TypeInfo> typeArguments;

	  public ParameterizedTypeInfoImpl(TypeResolverImpl<T, M, A, P, F> domain, T type) {
	    super(domain);

	    //
	    this.type = type;
	    this.rawType = null;
	  }

	  public T unwrap() {
	    return type;
	  }

	  public TypeInfo getRawType() {
	    if (rawType == null) {
	      T rawType = domain.typeMetadata.getRawType(type);
	      this.rawType = domain.getType(rawType);
	    }
	    return rawType;
	  }

	  public List<TypeInfo> getTypeArguments() {
	    if (typeArguments == null) {
	      ArrayList<TypeInfo> typeArguments = new ArrayList<TypeInfo>();
	      for (T typeArgument : domain.typeMetadata.getTypeArguments(type)) {
	        AbstractTypeInfo<T, M, A, P, F> ta = domain.getType(typeArgument);
	        typeArguments.add(ta);
	      }
	      this.typeArguments = typeArguments;
	    }
	    return typeArguments;
	  }
	}
