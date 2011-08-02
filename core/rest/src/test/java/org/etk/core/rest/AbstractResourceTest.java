package org.etk.core.rest;

import java.io.ByteArrayInputStream;

import javax.servlet.http.HttpServletRequest;

import org.etk.core.rest.impl.ContainerResponse;
import org.etk.core.rest.impl.EnvironmentContext;
import org.etk.core.rest.impl.InputHeadersMap;
import org.etk.core.rest.impl.MultivaluedMapImpl;
import org.etk.core.rest.servlet.mock.MockHttpServletRequest;
import org.etk.core.rest.tools.DummyContainerResponseWriter;

public abstract class AbstractResourceTest extends BaseTest {
  
  public ContainerResponse service(String method,
                                   String requestURI,
                                   String baseURI,
                                   MultivaluedMapImpl headers,
                                   byte[] data,
                                   ContainerResponseWriter writer) throws Exception {
    
    if (headers == null)
      headers = new MultivaluedMapImpl();
    
    ByteArrayInputStream in = null;
    if (data != null)
      in = new ByteArrayInputStream(data);
    EnvironmentContext envctx = new EnvironmentContext();
    HttpServletRequest request = new MockHttpServletRequest(in,
                                                            in != null ? in.available() : 0,
                                                            method,
                                                            new InputHeadersMap(headers));
    ContainerResponse response = new ContainerResponse(writer);
    requestHandler.handleRequest(request, response);
    return response;

  }
  
  public ContainerResponse service(String method,
                                   String requestURI,
                                   String baseURI,
                                   MultivaluedMapImpl headers,
                                   byte[] data) throws Exception {
    return service(method, requestURI, baseURI, headers, data, new DummyContainerResponseWriter());

  }
  
  
  public void tearDown() throws Exception {
  }
  

}
