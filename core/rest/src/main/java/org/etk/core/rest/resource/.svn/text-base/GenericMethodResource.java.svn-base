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

package org.exoplatform.services.rest.resource;

import java.lang.reflect.Method;
import java.util.List;

import org.exoplatform.services.rest.method.MethodInvoker;
import org.exoplatform.services.rest.method.MethodParameter;

/**
 * Abstraction for method in resource, this essence is common for
 * {@link ResourceMethodDescriptor}, {@link SubResourceMethodDescriptor},
 * {@link SubResourceLocatorDescriptor} .
 * 
 * @author <a href="mailto:andrew00x@gmail.com">Andrey Parfonov</a>
 * @version $Id: $
 */
public interface GenericMethodResource {

  /**
   * @return See {@link Method}
   */
  Method getMethod();

  /**
   * @return List of method parameters
   */
  List<MethodParameter> getMethodParameters();

  /**
   * @return parent resource descriptor
   */
  AbstractResourceDescriptor getParentResource();
  
  /**
   * @return invoker that must be used for processing current method
   */
  MethodInvoker getMethodInvoker();
  
  /**
   * @return Java type returned by method, see {@link #getMethod()}
   */
  Class<?> getResponseType();

}
