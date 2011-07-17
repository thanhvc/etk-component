package org.etk.model.plugins.instrument;

/**
 * The proxy type to support creating the class instance.
 * It 's get Constructor and passes the MethodHandler such as argument 
 * to create new instance class "_ORM"
 *
 */
public interface ProxyType<O> {

  /**
   * Create a proxy instance that delegates all method invocations to the
   * provided handler.
   * 
   * @param handler the handler
   * @return a proxy instance
   */
  O createProxy(MethodHandler handler);

  /**
   * Returns the proxied type.
   *
   * @return the proxied type
   */
  Class<? extends O> getType();
}
