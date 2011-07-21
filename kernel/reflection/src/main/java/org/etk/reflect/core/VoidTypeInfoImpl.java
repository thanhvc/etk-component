package org.etk.reflect.core;

import org.etk.reflect.api.VoidTypeInfo;

class VoidTypeInfoImpl<T, M, A, P, F> extends ClassTypeInfoImpl<T, M, A, P, F> implements VoidTypeInfo {

	public VoidTypeInfoImpl(TypeResolverImpl<T, M, A, P, F> domain, T type) {
		super(domain, type);
	}
}
