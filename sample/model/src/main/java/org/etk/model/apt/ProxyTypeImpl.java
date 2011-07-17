package org.etk.model.apt;

import java.lang.reflect.Constructor;

import org.etk.model.plugins.instrument.MethodHandler;
import org.etk.model.plugins.instrument.ProxyType;
/**
 * Implements the SPI {@link ProxyType} interface.
 *
 */
public class ProxyTypeImpl<O> implements ProxyType<O> {

  /** . */
  private final Constructor<? extends O> ctor;

  public ProxyTypeImpl(Class<O> objectClass) {

    Constructor<? extends O> ctor;
    try {
      ClassLoader classLoader = objectClass.getClassLoader();
      Class<? extends O> proxyClass = (Class<? extends O>)classLoader.loadClass(objectClass.getName() + "_ENTITY");
      ctor = proxyClass.getConstructor(MethodHandler.class);
    }
    catch (Exception e) {
      throw new AssertionError(e);
    }

    this.ctor = ctor;
  }

  public O createProxy(MethodHandler handler) {
    try {
      return ctor.newInstance(handler);
    }
    catch (Exception e) {
      throw new AssertionError(e);
    }
  }

  public Class<? extends O> getType() {
    return ctor.getDeclaringClass();
  }
}