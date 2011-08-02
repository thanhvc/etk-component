package org.etk.core.rest;

/**
 * Process the original {@link GenericContainerRequest} before it dispatch by
 * {@link ResourceDispatcher}. NOTE this method must be not called directly, it
 * is part of REST framework, otherwise {@link ApplicationContext} may contains
 * wrong parameters.
 * 

 */
public interface RequestFilter {

  /**
   * Can modify original request.
   * 
   * @param request the request
   */
  void doFilter(GenericContainerRequest request);

}