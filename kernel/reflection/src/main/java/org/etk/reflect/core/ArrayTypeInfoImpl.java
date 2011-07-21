package org.etk.reflect.core;

import org.etk.reflect.api.ArrayTypeInfo;
import org.etk.reflect.api.TypeInfo;


public class ArrayTypeInfoImpl<T, M, A, P, F> extends AbstractArrayTypeInfo<T, M, A, P, F> implements ArrayTypeInfo {

	  /** . */
	  private final T type;

	  /** . */
	  private TypeInfo componentType;


	  public ArrayTypeInfoImpl(TypeResolverImpl<T, M, A, P, F> domain, T type) {
	    super(domain);

	    //
	    this.type = type;
	  }

	  public Object unwrap() {
	    return type;
	  }

	  public TypeInfo getComponentType() {
	    if (componentType == null) {
	      T componentType = domain.typeMetadata.getComponentType(type);
	      this.componentType = domain.getType(componentType);
	    }
	    return componentType;
	  }
	}
