/*
 * Copyright (C) 2003-2011 eXo Platform SAS.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.etk.common.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Various utils for performing runtime introspection on annotations.
 * 
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Jul 28, 2011  
 */
public class AnnotationIntrospector {

   /**
    * Resolve an annotation of the specified class and its inheritance hierarchy. If no such annotation
    * cannot be resolved then null is returned.
    *
    * @param clazz the examined class
    * @param classAnnotation the annotation to lookup
    * @param <A> the annotation class
    * @return the annotation
    * @throws NullPointerException if any argument is null
    */
  public static <A extends Annotation> A resolveClassAnnotations(Class<?> clazz,
                                                                 Class<A> classAnnotation) throws NullPointerException {
    if (clazz == null) {
      throw new NullPointerException("No null class");
    }
    if (classAnnotation == null) {
      throw new NullPointerException("No null annotation");
    }

    //
    A tpl = clazz.getAnnotation(classAnnotation);

    //
    if (tpl == null) {
      for (Class itf : clazz.getInterfaces()) {
        tpl = resolveClassAnnotations(itf, classAnnotation);
        if (tpl != null) {
          break;
        }
      }
    }

    //
    if (tpl == null) {
      Class<?> superClazz = clazz.getSuperclass();
      if (superClazz != null) {
        tpl = resolveClassAnnotations(superClazz, classAnnotation);
      }
    }

    //
    return tpl;
  }

  /**
   * Resolves the Method annotations which Class<?> is owner and MethodAnnotation.
   * 
   * @param <A>
   * @param clazz
   * @param methodAnnotation
   * @return
   */
  public static <A extends Annotation> Map<Method, A> resolveMethodAnnotations(final Class<?> clazz,
                                                                               Class<A> methodAnnotation) {
    if (clazz == null) {
      throw new NullPointerException("No null class");
    }
    if (methodAnnotation == null) {
      throw new NullPointerException("No null annotation");
    }

    //
    Map<Method, A> methods = new HashMap<Method, A>();

    for (Method method : clazz.getDeclaredMethods()) {
      A annotation = method.getAnnotation(methodAnnotation);
      if (annotation != null) {
        methods.put(method, annotation);
      }
    }

    // Resolve parent annotations
    Class<?> superClazz = clazz.getSuperclass();
    if (superClazz != null) {
      Map<Method, A> parentAnnotations = resolveMethodAnnotations(superClazz, methodAnnotation);
      for (Map.Entry<Method, A> entry : parentAnnotations.entrySet()) {
        Method parentMethod = entry.getKey();
        try {
          Method method = clazz.getMethod(parentMethod.getName(), parentMethod.getParameterTypes());
          if (!methods.containsKey(method)) {
            methods.put(method, entry.getValue());
          }
        } catch (NoSuchMethodException e) {
          throw new AssertionError(e);
        }
      }
    }

    // Resolve interface annotations
    for (Class<?> itf : clazz.getInterfaces()) {
      Map<Method, A> itfAnnotations = resolveMethodAnnotations(itf, methodAnnotation);
      for (Map.Entry<Method, A> entry : itfAnnotations.entrySet()) {
        Method itfMethod = entry.getKey();
        try {
          Method method = clazz.getMethod(itfMethod.getName(), itfMethod.getParameterTypes());
          if (!methods.containsKey(method)) {
            methods.put(method, entry.getValue());
          }
        } catch (NoSuchMethodException e) {
          throw new AssertionError(e);
        }
      }
    }

    //
    return methods;
  }

}
