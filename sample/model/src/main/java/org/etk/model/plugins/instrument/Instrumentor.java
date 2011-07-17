package org.etk.model.plugins.instrument;


public interface Instrumentor {

  <O> ProxyType<O> getProxyClass(Class<O> clazz);

  /**
   * Returns the method handler for the specified proxy or null if the object argument is not
   * instrumented.
   *
   * @param proxy the instrumented proxy
   * @return the method handler
   */
  MethodHandler getInvoker(Object proxy);

}
