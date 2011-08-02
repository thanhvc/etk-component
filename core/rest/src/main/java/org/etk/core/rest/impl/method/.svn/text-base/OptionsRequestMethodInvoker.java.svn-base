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

package org.exoplatform.services.rest.impl.method;

import javax.ws.rs.core.Response;

import org.exoplatform.services.rest.ApplicationContext;
import org.exoplatform.services.rest.impl.header.MediaTypeHelper;
import org.exoplatform.services.rest.method.MethodInvoker;
import org.exoplatform.services.rest.resource.GenericMethodResource;
import org.exoplatform.services.rest.wadl.WadlProcessor;
import org.exoplatform.services.rest.wadl.research.Application;

/**
 * @author <a href="mailto:andrew00x@gmail.com">Andrey Parfonov</a>
 * @version $Id: $
 */
public class OptionsRequestMethodInvoker implements MethodInvoker {

  /**
   * {@inheritDoc}
   */
  public Object invokeMethod(Object resource,
                             GenericMethodResource genericMethodResource,
                             ApplicationContext context) {
    Application wadlApplication = new WadlProcessor().process(genericMethodResource.getParentResource(),
                                                              context.getBaseUri());
    return Response.ok(wadlApplication, MediaTypeHelper.WADL_TYPE).build();
  }

}
