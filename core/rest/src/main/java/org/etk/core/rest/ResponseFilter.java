package org.etk.core.rest;

/**
 * Process the original {@link GenericContainerResponse} before pass it for
 * serialization to environment, e. g. servlet container. NOTE this filter must
 * not be used directly, it is part of REST framework.
 * 
 */
public interface ResponseFilter {

  /**
   * Can modify original response.
   * 
   * @param response the response from resource
   */
  void doFilter(GenericContainerResponse response);

}
