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

import org.exoplatform.services.rest.ObjectModel;
import org.exoplatform.services.rest.impl.resource.PathValue;
import org.exoplatform.services.rest.uri.UriPattern;

/**
 * Describe Resource Class or Root Resource Class. Resource Class is any Java
 * class that uses JAX-RS annotations to implement corresponding Web resource.
 * 
 * @author <a href="mailto:andrew00x@gmail.com">Andrey Parfonov</a>
 * @version $Id: $
 */
public interface AbstractResourceDescriptor extends ResourceDescriptor, ObjectModel {

  /**
   * @return See {@link PathValue}
   */
  PathValue getPathValue();

  /**
   * @see ResourceMethodDescriptor
   * @return resource methods
   */
  ResourceMethodMap<ResourceMethodDescriptor> getResourceMethods();

  /**
   * @see SubResourceLocatorDescriptor
   * @return sub-resource locators
   */
  SubResourceLocatorMap getSubResourceLocators();

  /**
   * @see SubResourceMethodDescriptor
   * @return sub-resource methods
   */
  SubResourceMethodMap getSubResourceMethods();

  /**
   * @return See {@link UriPattern}
   */
  UriPattern getUriPattern();

  /**
   * @return true if resource is root resource false otherwise. Root resource is
   *         class which has own {@link javax.ws.rs.Path} annotation
   */
  boolean isRootResource();

}
