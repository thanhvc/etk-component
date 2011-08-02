package org.etk.core.rest;

import java.io.IOException;

import javax.ws.rs.ext.MessageBodyWriter;

/**
 * All implementation of this interface should be able to write data in
 * container response, e. g. servlet response.
 * 
 */
public interface ContainerResponseWriter {

  /**
   * Write HTTP status and headers in HTTP response.
   * 
   * @param response container response
   * @throws IOException if any i/o error occurs
   */
  void writeHeaders(GenericContainerResponse response) throws IOException;

  /**
   * Write entity body in output stream.
   * 
   * @param response container response
   * @param entityWriter See {@link MessageBodyWriter}
   * @throws IOException if any i/o error occurs
   */
  @SuppressWarnings("unchecked")
  void writeBody(GenericContainerResponse response, MessageBodyWriter entityWriter) throws IOException;

}
