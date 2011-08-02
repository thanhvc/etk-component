package org.etk.core.rest;

import java.io.IOException;
import java.lang.reflect.Type;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;


public interface GenericContainerResponse {

  /**
   * Set response. New response can be override old one.
   * 
   * @param response See {@link Response}
   */
  void setResponse(Response response);

  /**
   * Get preset {@link Response}. This method can be useful for modification
   * {@link GenericContainerResponse}. See
   * {@link ResponseFilter#doFilter(GenericContainerResponse)}.
   * 
   * @return preset {@link Response} or null if it was not initialized yet.
   */
  Response getResponse();

  /**
   * Write response to output stream.
   * 
   * @throws IOException if any i/o errors occurs
   */
  void writeResponse() throws IOException;

  /**
   * @return HTTP status
   */
  int getStatus();

  /**
   * @return HTTP headers
   */
  MultivaluedMap<String, Object> getHttpHeaders();

  /**
   * @return entity body
   */
  Object getEntity();

  /**
   * @return entity type
   */
  Type getEntityType();

  /**
   * @return body content type
   */
  MediaType getContentType();

}
