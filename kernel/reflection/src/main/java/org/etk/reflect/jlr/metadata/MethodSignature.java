package org.etk.reflect.jlr.metadata;

import java.lang.reflect.Method;
import java.util.Arrays;

class MethodSignature {

  /** . */
  private final String name;

  /** . */
  private final Class<?>[] parameterTypes;

  public MethodSignature(Method method) {
    this.name = method.getName();
    this.parameterTypes = method.getParameterTypes();
  }

  public MethodSignature(String methodName, Class... parameterTypes) {
    this.name = methodName;
    this.parameterTypes = parameterTypes;
  }

  public String getName() {
    return name;
  }

  public Method getDeclaredMethod(Class declaringClass) {
    try {
      return declaringClass.getDeclaredMethod(name, parameterTypes);
    }
    catch (NoSuchMethodException e) {
      return null;
    }
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj instanceof MethodSignature) {
      MethodSignature that = (MethodSignature)obj;
      return name.equals(that.name) && Arrays.equals(parameterTypes, that.parameterTypes);
    }
    return false;
  }

  @Override
  public int hashCode() {
    return name.hashCode() ^ Arrays.hashCode(parameterTypes);
  }
}

