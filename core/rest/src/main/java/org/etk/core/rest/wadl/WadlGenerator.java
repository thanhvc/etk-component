/**
 * Copyright (C) 2003-2008 eXo Platform SAS.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see<http://www.gnu.org/licenses/>.
 */

package org.etk.core.rest.wadl;

import javax.ws.rs.core.MediaType;

import org.etk.core.rest.method.MethodParameter;
import org.etk.core.rest.resource.AbstractResourceDescriptor;
import org.etk.core.rest.resource.ResourceMethodDescriptor;

import org.etk.core.rest.wadl.research.Application;
import org.etk.core.rest.wadl.research.Param;
import org.etk.core.rest.wadl.research.RepresentationType;
import org.etk.core.rest.wadl.research.Resources;




/**
 * A WadGenerator creates structure that can be reflected to WADL
 * representation.
 * 
 */
public interface WadlGenerator {

  /**
   * @return {@link Application} instance, it is root element in WADL
   */
  Application createApplication();

  /**
   * @return {@link Resources} instance. Element <i>resources</i> in WADL
   *         document is container for the descriptions of resources provided by
   *         application
   */
  Resources createResources();

  /**
   * @param rd See {@link AbstractResourceDescriptor}
   * @return {@link org.exoplatform.services.rest.wadl.research.Resource.Resource}
   *         describes application resource, each resource identified by a URI
   */
  org.etk.core.rest.wadl.research.Resource createResource(AbstractResourceDescriptor rd);

  /**
   * @param path resource relative path
   * @return {@link org.exoplatform.services.rest.wadl.research.Resource.Resource}
   *         describes application resource, each resource identified by a URI
   */
  org.etk.core.rest.wadl.research.Resource createResource(String path);

  /**
   * @param md See {@link ResourceMethodDescriptor}
   * @return {@link org.exoplatform.services.rest.wadl.research.Method}
   *         describes the input to and output from an HTTP protocol method they
   *         may be applied to a resource
   */
  org.etk.core.rest.wadl.research.Method createMethod(ResourceMethodDescriptor md);

  /**
   * @return {@link org.exoplatform.services.rest.wadl.research.Request}
   *         describes the input to be included when applying an HTTP method to
   *         a resource
   * @see {@link org.exoplatform.services.rest.wadl.research.Method}
   */
  org.etk.core.rest.wadl.research.Request createRequest();

  /**
   * @return {@link org.exoplatform.services.rest.wadl.research.Response}
   *         describes the output that result from performing an HTTP method on
   *         a resource
   * @see {@link org.exoplatform.services.rest.wadl.research.Method}
   */
  org.etk.core.rest.wadl.research.Response createResponse();

  /**
   * @param mediaType one of media type that resource can consume
   * @return {@link RepresentationType} describes a representation of resource's
   *         state
   */
  RepresentationType createRequestRepresentation(MediaType mediaType);

  /**
   * @param mediaType one of media type that resource can produce
   * @return {@link RepresentationType} describes a representation of resource's
   *         state
   */
  RepresentationType createResponseRepresentation(MediaType mediaType);

  /**
   * @param methodParameter See {@link MethodParameter}
   * @return {@link Param} describes a parameterized component of its parent
   *         element resource, request, response
   * @see org.exoplatform.services.rest.wadl.research.Resource
   * @see org.exoplatform.services.rest.wadl.research.Request
   * @see org.exoplatform.services.rest.wadl.research.Response
   */
  Param createParam(MethodParameter methodParameter);

}
