package org.etk.core.rest.impl;

import java.util.List;
import java.util.Map;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Providers;

import org.etk.core.rest.GenericContainerRequest;
import org.etk.core.rest.GenericContainerResponse;

/**
 * Provides access to ContainerRequest, ContainerResponse and request URI
 * information.
 * 
 */
public interface ApplicationContext extends UriInfo {

  /**
   * Should be used to pass template values in context by using returned list in
   * matching to @see
   * {@link org.exoplatform.services.rest.uri.UriPattern#match(String, List)}
   * . List will be cleared during matching.
   * 
   * @return the list for template values
   */
  List<String> getParameterValues();

  /**
   * Pass in context list of path template parameters @see {@link UriPattern}.
   * 
   * @param parameterNames list of templates parameters
   */
  void setParameterNames(List<String> parameterNames);

  /**
   * Add ancestor resource, according to JSR-311:
   * <p>
   * Entries are ordered according in reverse request URI matching order, with
   * the root resource last.
   * </p>
   * So add each new resource at the begin of list.
   * 
   * @param resource the resource e. g. resource class, sub-resource method or
   *          sub-resource locator.
   */
  void addMatchedResource(Object resource);

  /**
   * Add ancestor resource, according to JSR-311:
   * <p>
   * Entries are ordered in reverse request URI matching order, with the root
   * resource URI last.
   * </p>
   * So add each new URI at the begin of list.
   * 
   * @param uri the partial part of that matched to resource class, sub-resource
   *          method or sub-resource locator.
   */
  void addMatchedURI(String uri);

  /**
   * @return get mutable runtime attributes
   */
  Map<String, Object> getAttributes();

  /**
   * @return See {@link Request}
   */
  Request getRequest();

  /**
   * @return See {@link HttpHeaders}
   */
  HttpHeaders getHttpHeaders();

  /**
   * @return See {@link SecurityContext}
   */
  SecurityContext getSecurityContext();

  /**
   * @return See {@link GenericContainerRequest}
   */
  GenericContainerRequest getContainerRequest();

  /**
   * @return See {@link UriInfo}
   */
  UriInfo getUriInfo();

  /**
   * @return See {@link GenericContainerResponse}
   */
  GenericContainerResponse getContainerResponse();
  
  /**
   * @return {@link ProviderBinder}
   * @see Providers
   */
  ProviderBinder getProviders();

}

