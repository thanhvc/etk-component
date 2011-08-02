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

package org.exoplatform.services.rest.method;

import org.exoplatform.services.rest.ApplicationContext;
import org.exoplatform.services.rest.resource.GenericMethodResource;

/**
 * Invoke resource methods.
 * 
 * @see GenericMethodResource
 * @author <a href="mailto:andrew00x@gmail.com">Andrey Parfonov</a>
 * @version $Id: $
 */
public interface MethodInvoker {

  /**
   * Invoke supplied method and return result of method invoking.
   * 
   * @param resource object that contains method
   * @param genericMethodResource See {@link GenericMethodResource}
   * @param context See {@link ApplicationContextImpl}
   * @return result of method invoking
   */
  Object invokeMethod(Object resource,
                      GenericMethodResource genericMethodResource,
                      ApplicationContext context);

}
