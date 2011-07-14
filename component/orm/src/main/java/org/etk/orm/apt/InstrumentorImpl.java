package org.etk.orm.apt;

import java.lang.reflect.Field;

import org.etk.orm.plugins.instrument.Instrumentor;
import org.etk.orm.plugins.instrument.MethodHandler;
import org.etk.orm.plugins.instrument.ProxyType;


public class InstrumentorImpl implements Instrumentor {

  public <O> ProxyType<O> getProxyClass(Class<O> clazz) {
    return new ProxyTypeImpl<O>(clazz);
  }

  public MethodHandler getInvoker(Object proxy) {
    if (proxy instanceof Instrumented) {
      try {
        Field field = proxy.getClass().getField("handler");
        return (MethodHandler)field.get(proxy);
      }
      catch (NoSuchFieldException e) {
        throw new AssertionError(e);
      }
      catch (IllegalAccessException e) {
        throw new AssertionError(e);
      }
    } else {
      return null;
    }
  }
}