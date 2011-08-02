package org.etk.sandbox.rest.spi;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.etk.sandbox.rest.api.Util;
import org.etk.service.foo.model.Foo;
import org.etk.service.foo.spi.FooService;

@Path("/etk/rest/foo")
public class FooRestService {
  private FooService fooService;
  
  public FooRestService() {
    
  }
  
  /**
   * shows pendingSpaceList by json/xml format
   *
   * @param uriInfo
   * @param userId
   * @param format
   * @return response
   * @throws Exception
   */
  @GET
  @Path("pendingSpaces/show.{format}")
  public Response showFoo(@Context UriInfo uriInfo,
                                       @PathParam("id") String fooId,
                                       @PathParam("format") String format) throws Exception {
    MediaType mediaType = Util.getMediaType(format);
    fooService = Util.getFooService();
    Foo foo = fooService.getFooById(fooId);
    return Util.getResponse(foo, uriInfo, mediaType, Response.Status.OK);
  }
  
  
  
}
