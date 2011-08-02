package org.etk.sandbox.rest.api;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.etk.common.logging.Logger;
import org.etk.sandbox.rest.api.model.Version;
import org.etk.sandbox.rest.api.model.Versions;


/**
 * <p>The version <tt>public</tt> rest service to gets the current latest rest service version and supported
 * versions.</p> <p> Url template: <tt>{rest_context_name}/api/etk/version</tt> </p>
 *
*/
@Path("api/etk/version")
public class VersionResources {
  /**
   * The latest social rest api version.
   */
  public static final String LASTEST_VERSION = "v0.1.x";

  /**
   * The logger.
   */
  private final Logger log = Logger.getLogger(this.getClass());
  
  /**
   * The supported versions
   */
  public static final List<String> SUPPORTED_VERSIONS = new ArrayList<String>();

  static {
    SUPPORTED_VERSIONS.add(LASTEST_VERSION);
  }

  /**
   * Gets the latest social rest api version, this version number should be used as the latest and stable version. This
   * latest version is consider to include all new features and updates.
   *
   * @param uriInfo the uri info
   * @param format  the expected returned format
   * @return response of the request, the type bases on the format param
   */
  @GET
  @Path("latest.{format}")
  public Response getLatestVersion(@Context UriInfo uriInfo,
                                   @PathParam("format") String format) {
    final String[] supportedFormat = new String[]{"json"};
    MediaType mediaType = Util.getMediaType(format, supportedFormat);
    Version entity = new Version();
    entity.setVersion(LASTEST_VERSION);
    return Util.getResponse(entity, uriInfo, mediaType, Status.OK);
  }


  /**
   * Gets the supported social rest api versions, this is for backward compatible. If a client application is using an
   * older social rest api version, it should just work. The list order must be from the latest to oldest versions.
   *
   * @param uriInfo the uri info
   * @param format  the expected returned format
   * @return response of the request, the type bases on the format param
   */
  @GET
  @Path("supported.{format}")
  public Response getSupportedVersions(@Context UriInfo uriInfo,
                                       @PathParam("format") String format) {
    final String[] supportedFormat = new String[]{"json", "xml"};
    Versions entity = new Versions();
    entity.getVersions().addAll(SUPPORTED_VERSIONS);
    return Util.getResponse(entity, uriInfo, Util.getMediaType(format, supportedFormat), Status.OK);
  }

  

}