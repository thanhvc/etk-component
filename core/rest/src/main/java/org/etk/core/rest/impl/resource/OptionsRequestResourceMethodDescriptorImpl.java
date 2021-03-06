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

package org.etk.core.rest.impl.resource;

import java.lang.reflect.Method;
import java.util.List;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.etk.core.rest.method.MethodInvoker;
import org.etk.core.rest.method.MethodParameter;
import org.etk.core.rest.resource.AbstractResourceDescriptor;

/**
 * @author <a href="mailto:andrew00x@gmail.com">Andrey Parfonov</a>
 * @version $Id: $
 */
public final class OptionsRequestResourceMethodDescriptorImpl extends ResourceMethodDescriptorImpl {

  /**
   * @param method See {@link Method}
   * @param httpMethod HTTP request method designator
   * @param parameters list of method parameters. See {@link MethodParameter}
   * @param parentResource parent resource for this method
   * @param consumes list of media types which this method can consume
   * @param produces list of media types which this method can produce
   * @param invoker method invoker
   */
  public OptionsRequestResourceMethodDescriptorImpl(Method method,
                                                    String httpMethod,
                                                    List<MethodParameter> parameters,
                                                    AbstractResourceDescriptor parentResource,
                                                    List<MediaType> consumes,
                                                    List<MediaType> produces,
                                                    MethodInvoker invoker) {
    super(method, httpMethod, parameters, parentResource, consumes, produces, invoker);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Class<?> getResponseType() {
    return Response.class;
  }

}
