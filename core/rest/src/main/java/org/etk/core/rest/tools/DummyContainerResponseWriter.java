package org.etk.core.rest.tools;

import java.io.IOException;

import javax.ws.rs.ext.MessageBodyWriter;

import org.etk.core.rest.ContainerResponseWriter;
import org.etk.core.rest.GenericContainerResponse;

/**
 * Mock object than can be used for any test when we don't care about response
 * entity at all.
 * 

 */
public class DummyContainerResponseWriter implements ContainerResponseWriter {

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  public void writeBody(GenericContainerResponse response, MessageBodyWriter entityWriter) throws IOException {
  }

  /**
   * {@inheritDoc}
   */
  public void writeHeaders(GenericContainerResponse response) throws IOException {
  }

}

