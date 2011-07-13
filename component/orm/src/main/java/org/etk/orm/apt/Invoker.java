package org.etk.orm.apt;

import java.lang.reflect.Method;
import java.lang.reflect.UndeclaredThrowableException;

import org.etk.orm.plugins.instrument.MethodHandler;


public final class Invoker {

  private static class MethodFinder extends TypeHierarchyVisitor {

    /** . */
    private final String methodName;

    /** . */
    private final Class[] parameterTypes;

    /** . */
    private Method method;

    private MethodFinder(String methodName, Class[] parameterTypes) {
      this.methodName = methodName;
      this.parameterTypes = parameterTypes;
    }

    protected boolean enter(Class type) {
      try {
        method = type.getDeclaredMethod(methodName, parameterTypes);
        return false;
      }
      catch (NoSuchMethodException e) {
        return true;
      }
    }
  }

  public static Invoker getDeclaredMethod(Class<?> clazz, final String methodName, final Class<?>... parameterTypes) {
    MethodFinder visitor = new MethodFinder(methodName, parameterTypes);
    visitor.accept(clazz);

    //
    if (visitor.method == null) {
      StringBuilder sb = new StringBuilder(methodName).append('(');
      for (int i = 0;i < parameterTypes.length;i++) {
        if (i > 0) {
          sb.append(",");
        }
        sb.append(parameterTypes[i].getName());
      }
      sb.append(')');
      throw new AssertionError("Could not find method " + sb + " on class " + clazz);
    }

    //
    return new Invoker(visitor.method);
  }

  /** . */
  public final Method method;

  public Invoker(Method method) {
    this.method = method;
  }

  public Method getMethod() {
    return method;
  }

  public Object invoke(MethodHandler methodInvoker, Object obj) {
    try {
      return methodInvoker.invoke(obj, method);
    }
    catch (Throwable e) {
      if (e instanceof RuntimeException) {
        throw (RuntimeException)e;
      } else if (e instanceof Error) {
        throw (Error)e;
      }
      throw new UndeclaredThrowableException(e);
    }
  }

  public Object invoke(MethodHandler methodInvoker, Object obj, Object arg) {
    try {
      return methodInvoker.invoke(obj, method, arg);
    }
    catch (Throwable e) {
      if (e instanceof RuntimeException) {
        throw (RuntimeException)e;
      } else if (e instanceof Error) {
        throw (Error)e;
      }
      throw new UndeclaredThrowableException(e);
    }
  }

  public Object invoke(MethodHandler methodInvoker, Object obj, Object[] args) {
    try {
      return methodInvoker.invoke(obj, method, args);
    }
    catch (Throwable e) {
      if (e instanceof RuntimeException) {
        throw (RuntimeException)e;
      } else if (e instanceof Error) {
        throw (Error)e;
      }
      throw new UndeclaredThrowableException(e);
    }
  }
}

