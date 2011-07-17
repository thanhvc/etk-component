package org.etk.model.apt;

import java.lang.reflect.Field;

import org.etk.model.plugins.instrument.Instrumentor;
import org.etk.model.plugins.instrument.MethodHandler;
import org.etk.model.plugins.instrument.ProxyType;


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