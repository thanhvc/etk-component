package org.etk.reflect.core;

import java.lang.annotation.Annotation;

import org.etk.reflect.api.ClassTypeInfo;
import org.etk.reflect.api.annotation.AnnotationInfo;

/**
 * Define the class which use to determine the Annotation type. 
 * @author Administrator
 *
 * @param <A> Annotation class.
 * @param <T> Annotation class type.
 */
public abstract class AnnotationType<A, T> {
	public static <A extends Annotation> AnnotationType<A, Class<A>> get(Class<A> a) {
		return new Runtime<A>(a);
	}
	
	public static AnnotationType<AnnotationInfo, ClassTypeInfo> get(ClassTypeInfo a) {
		return new Meta(a);
	}
	protected AnnotationType() {
	}
	/**
	 * Define the method which use to 
	 * determine class type of annotation.
	 * @return
	 */
	public abstract T getType();
	
	private static class Runtime<A extends Annotation> extends AnnotationType<A, Class<A>> {
		private final Class<A> type;
		private Runtime(Class<A> type) {
			this.type = type;
		}
		
		@Override
		public Class<A> getType() {
			return type;
		}
	}
	
	/**
	 * Define the Annotation has type of AnnotationInfo and ClassTypeInfo
	 * @author Administrator
	 *
	 */
	private static class Meta extends AnnotationType<AnnotationInfo, ClassTypeInfo> {
		private final ClassTypeInfo type;
		private Meta(ClassTypeInfo type) {
			this.type = type;
		}
		
		@Override
		public ClassTypeInfo getType() {
			return this.type;
		}
	}

}
