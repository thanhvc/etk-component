package org.etk.model.plugins.instrument;

import java.lang.reflect.Method;
/**
 * It supports to receive the <code>Method</code> and <code>Object</code> target,
 * and then invokes the given method. 
 * 
 * @author thanh_vucong
 *
 */
public interface MethodHandler {

  /**
   * Invokes a zero argument method.
   *
   * @param o the target
   * @param method the method to invoke
   * @return the invocation returned value
   * @throws Throwable any throwable
   */
  Object invoke(Object o, Method method) throws Throwable;

  /**
   * Invokes a one argument method.
   *
   * @param o the target
   * @param method the method to invoke
   * @param arg the method argument
   * @return the invocation returned value
   * @throws Throwable any throwable
   */
  Object invoke(Object o, Method method, Object arg) throws Throwable;

  /**
   * Invokes a multi argument method.
   *
   * @param o the target
   * @param method the method to invoke
   * @param args the method arguments packed in an array
   * @return the invocation returned value
   * @throws Throwable any throwable
   */
  Object invoke(Object o, Method method, Object[] args) throws Throwable;

}
