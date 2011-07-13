package org.etk.reflect.core;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import org.etk.reflect.api.ClassTypeInfo;
import org.etk.reflect.api.annotation.AnnotationInfo;
import org.etk.reflect.api.annotation.AnnotationParameterInfo;

public class AnnotationInfoImpl<T, M, A, P, F, E> implements AnnotationInfo {

	private final AnnotatedDelegate<T, M, A, P, F, E> owner;
	private final ClassTypeInfo type;
	
	private final A annotation;
	
	private Map<String, AnnotationParameterInfoImpl<T, M, A, P, F, E, ?>> abc = null;
	
	public AnnotationInfoImpl(AnnotatedDelegate<T, M, A, P, F, E> owner, ClassTypeInfo type, A annotation) {
		this.owner = owner;
		this.type = type;
		this.annotation = annotation;
	}
	
	@Override
	public ClassTypeInfo getType() {
		return type;
	}
	
	private Map<String, AnnotationParameterInfoImpl<T, M, A, P, F, E, ?>> getMap() {
		if (this.abc == null) {
			LinkedHashMap<String, AnnotationParameterInfoImpl<T, M, A, P, F, E, ?>> abc = new LinkedHashMap<String, AnnotationParameterInfoImpl<T, M, A, P, F, E, ?>>();
			for (P param : owner.annotationModel.getAnnotationParameters(annotation)) {
				AnnotationParameterInfoImpl<T, M, A, P, F, E, ?> aaa = new AnnotationParameterInfoImpl<T, M, A, P,F, E, Object>(owner, annotation, param);
				abc.put(aaa.getName(), aaa);
			}
			
			this.abc = Collections.unmodifiableMap(abc);
		}
		
		return abc;
	}

	@Override
	public AnnotationParameterInfo<?> getParameter(String parameterName) {
		return getMap().get(parameterName);
	}

	@Override
	public Collection<? extends AnnotationParameterInfo<?>> getParameters() {

		return getMap().values();
	}

}
