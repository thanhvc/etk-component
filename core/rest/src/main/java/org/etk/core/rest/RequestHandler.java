package org.etk.core.rest;


/**
 * Contract of this component is process all requests, initialization and
 * control main components of JAX-RS implementation.
 * 
 */
public interface RequestHandler {

  /**
   * Temporary directory attribute name.
   */
  public static final String WS_RS_TMP_DIR               = "ws.rs.tmpdir";

  /**
   * Max buffer size attribute name. Entities that has size greater then
   * specified will be stored in temporary directory on file system during
   * entity processing.
   */
  public static final String WS_RS_BUFFER_SIZE           = "ws.rs.buffersize";

  /**
   * Handle the HTTP request by dispatching request to appropriate resource. If
   * no one appropriate resource found then error response will be produced.
   * 
   * @param request HTTP request
   * @param response HTTP response
   * @throws Exception if any error occurs
   */
  void handleRequest(GenericContainerRequest request, GenericContainerResponse response) throws Exception;

}
