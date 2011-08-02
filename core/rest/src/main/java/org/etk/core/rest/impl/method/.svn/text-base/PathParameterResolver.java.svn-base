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

import javax.ws.rs.PathParam;

import org.exoplatform.services.rest.ApplicationContext;
import org.exoplatform.services.rest.method.TypeProducer;

/**
 * @author <a href="mailto:andrew00x@gmail.com">Andrey Parfonov</a>
 * @version $Id: $
 */
public class PathParameterResolver extends ParameterResolver<PathParam> {

  /**
   * See {@link PathParam}.
   */
  private final PathParam pathParam;

  /**
   * @param pathParam PathParam
   */
  PathParameterResolver(PathParam pathParam) {
    this.pathParam = pathParam;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object resolve(org.exoplatform.services.rest.Parameter parameter,
                        ApplicationContext context) throws Exception {
    String param = this.pathParam.value();
    TypeProducer typeProducer = ParameterHelper.createTypeProducer(parameter.getParameterClass(),
                                                                         parameter.getGenericType());
    return typeProducer.createValue(param,
                                    context.getPathParameters(!parameter.isEncoded()),
                                    parameter.getDefaultValue());
  }

}
