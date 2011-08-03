package org.etk.sandbox.rest.api;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.etk.kernel.container.ApplicationContainer;
import org.etk.kernel.container.KernelContainerContext;
import org.etk.service.foo.api.FooService;

public final class Util {
  /**
   * Prevents constructing a new instance.
   */
  private Util() {
  }

  /**
   * Gets the response object constructed from the provided params.
   *
   * @param entity the identity
   * @param uriInfo the uri request info
   * @param mediaType the media type to be returned
   * @param status the status code
   * @return response the response object
   */
  static public Response getResponse(Object entity, UriInfo uriInfo, MediaType mediaType, Response.Status status) {
    return Response.created(UriBuilder.fromUri(uriInfo.getAbsolutePath()).build())
                   .entity(entity)
                   .type(mediaType)
                   .status(status)
                   .build();
  }
  
  /**
   * Gets mediaType from string format.
   * Currently supports json and xml only.
   *
   * @param format
   * @return mediaType of matched or throw BAD_REQUEST exception
   * @throws WebApplicationException
   * @deprecated User {@link #getMediaType(String, String[])} instead.
   *             Will be removed by 1.3.x
   */
  @Deprecated
  static public MediaType getMediaType(String format) throws WebApplicationException {
    if (format.equals("json")) {
      return MediaType.APPLICATION_JSON_TYPE;
    } else if(format.equals("xml")) {
      return MediaType.APPLICATION_XML_TYPE;
    }
    throw new WebApplicationException(Response.Status.BAD_REQUEST);
  }


  /**
   * Gets the media type from an expected format string (usually the input) and an array of supported format strings.
   * If epxectedFormat is not found in the supported format array, Status.UNSUPPORTED_MEDIA_TYPE is thrown.
   * The supported format must include one of those format: json, xml, atom or rss, otherwise Status.NOT_ACCEPTABLE
   * could be thrown.
   *
   * @param expectedFormat the expected input format
   * @param supportedFormats the supported format array
   * @return the associated media type
   */
  public static MediaType getMediaType(String expectedFormat, String[] supportedFormats) {

    if (!isSupportedFormat(expectedFormat, supportedFormats)) {
      throw new WebApplicationException(Status.UNSUPPORTED_MEDIA_TYPE);
    }

    if (expectedFormat.equals("json") && isSupportedFormat("json", supportedFormats)) {
      return MediaType.APPLICATION_JSON_TYPE;
    } else if (expectedFormat.equals("xml") && isSupportedFormat("xml", supportedFormats)) {
      return MediaType.APPLICATION_XML_TYPE;
    } else if (expectedFormat.equals("atom") && isSupportedFormat("atom", supportedFormats)) {
      return MediaType.APPLICATION_ATOM_XML_TYPE;
    }
    //TODO What's about RSS format?
    throw new WebApplicationException(Status.NOT_ACCEPTABLE);
  }

  
  /**
   * Get viewerId from servlet request data information.
   *  
   * @param uriInfo
   * @return
   */
  static public String getViewerId (UriInfo uriInfo) {
    URI uri = uriInfo.getRequestUri();
    String requestString = uri.getQuery();
    if (requestString == null) return null;
    String[] queryParts = requestString.split("&");
    String viewerId = null;
    for (String queryPart : queryParts) {
      if (queryPart.startsWith("opensocial_viewer_id")) {
        viewerId = queryPart.substring(queryPart.indexOf("=") + 1, queryPart.length());
        break;
      }
    }
    
    return viewerId;
  }

 


  /**
   * Gets {@link SpaceService} with default portal container.
   *
   * @return the space service
   * @since  1.2.0-GA
   */
  public static final FooService getFooService() {
    return (FooService) getDefaultApplicationContainer().getComponentInstanceOfType(FooService.class);
  }


  /**
   * Converts a timestamp string to time string by the pattern: EEE MMM d HH:mm:ss Z yyyy.
   *
   * @param timestamp the timstamp to convert
   * @return the time string
   */
  public static final String convertTimestampToTimeString(long timestamp) {
   SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM d HH:mm:ss Z yyyy");
   dateFormat.setTimeZone(TimeZone.getDefault());
   return dateFormat.format(new Date(timestamp));
  }



  /**
   * Checks if an expected format is supported not not.
   *
   * @param expectedFormat  the expected format
   * @param supportedFormats the array of supported format
   * @return true or false
   */
  private static boolean isSupportedFormat(String expectedFormat, String[] supportedFormats) {
    for (String supportedFormat : supportedFormats) {
      if (supportedFormat.equals(expectedFormat)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Gets default portal container name.
   *
   * @return the portal container
   */
  private static ApplicationContainer getDefaultApplicationContainer() {
    return ApplicationContainer.getInstance();
  }

  /**
   * Gets a portal container by its name.
   *
   * @param applicationContainerName the specified portal container name
   * @return the portal container name
   */
  private static ApplicationContainer getApplicationContainerByName(String applicationContainerName) {
    return (ApplicationContainer) KernelContainerContext.getContainerByName(applicationContainerName);
  }
  
}
