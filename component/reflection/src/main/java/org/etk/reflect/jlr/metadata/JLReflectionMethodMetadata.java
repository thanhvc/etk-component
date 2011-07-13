package org.etk.reflect.jlr.metadata;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Arrays;

import org.etk.reflect.api.metadata.MethodMetadata;
import org.etk.reflect.core.AccessScope;


public class JLReflectionMethodMetadata implements MethodMetadata<Type, Method> {

  public Iterable<Method> getDeclaredMethods(Type classType) {
    Class<?> clazz = (Class<?>)classType;
    MethodContainer methods = new MethodContainer();
    for (Method declaredMethod : clazz.getDeclaredMethods()) {
      int modifiers = declaredMethod.getModifiers();
      if (!Modifier.isStatic(modifiers) && !Modifier.isPrivate(modifiers)) {
        if (!declaredMethod.isBridge()) {
          methods.add(declaredMethod);
        }
      }
    }
    return methods;
  }

  public Iterable<Type> getParameterTypes(Method method) {
    return Arrays.asList(method.getGenericParameterTypes());
  }

  public String getName(Method method) {
    return method.getName();
  }

  public Type getReturnType(Method method) {
    return method.getGenericReturnType();
  }
  
  public AccessScope getAccess(Method method) {
    return JLReflectionMetadata.getAccess(method);
  }

  public boolean isAbstract(Method method) {
    return Modifier.isAbstract(method.getModifiers());
  }

  public boolean isStatic(Method method) {
    return Modifier.isStatic(method.getModifiers());
  }

  public boolean isNative(Method method) {
    return Modifier.isNative(method.getModifiers());
  }

  public boolean isFinal(Method method) {
    return Modifier.isFinal(method.getModifiers());
  }

  public Iterable<Type> getTypeParameters(Method method) {
    ArrayList<Type> typeParameters = new ArrayList<Type>();
    for (TypeVariable typeParameter : method.getTypeParameters()) {
      typeParameters.add(typeParameter);
    }
    return typeParameters;
  }

  public Iterable<String> getParameterNames(Method method) {
    return null;
  }

  public Type getOwner(Method method) {
    return method.getDeclaringClass();
  }

  public Method getGenericDeclaration(Type typeVariable) {
    return (Method)((TypeVariable)typeVariable).getGenericDeclaration();
  }

  public Iterable<Type> getThrownTypes(Method method) {
    return Arrays.asList(method.getGenericExceptionTypes());
  }


}
