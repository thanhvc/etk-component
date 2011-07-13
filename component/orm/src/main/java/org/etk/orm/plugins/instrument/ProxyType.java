package org.etk.orm.plugins.instrument;

/**
 * The proxy type.
 *
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public interface ProxyType<O> {

  /**
   * Create a proxy instance that delegates all method invocations to the provided handler
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
