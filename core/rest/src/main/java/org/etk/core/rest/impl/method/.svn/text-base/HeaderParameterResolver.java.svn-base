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

import javax.ws.rs.HeaderParam;

import org.exoplatform.services.rest.ApplicationContext;
import org.exoplatform.services.rest.method.TypeProducer;

/**
 * @author <a href="mailto:andrew00x@gmail.com">Andrey Parfonov</a>
 * @version $Id: $
 */
public class HeaderParameterResolver extends ParameterResolver<HeaderParam> {

  /**
   * See {@link HeaderParam}.
   */
  private final HeaderParam headerParam;

  /**
   * @param headerParam HeaderParam
   */
  HeaderParameterResolver(HeaderParam headerParam) {
    this.headerParam = headerParam;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object resolve(org.exoplatform.services.rest.Parameter parameter,
                        ApplicationContext context) throws Exception {
    String param = this.headerParam.value();
    TypeProducer typeProducer = ParameterHelper.createTypeProducer(parameter.getParameterClass(),
                                                                         parameter.getGenericType());
    return typeProducer.createValue(param,
                                    context.getHttpHeaders().getRequestHeaders(),
                                    parameter.getDefaultValue());
  }

}
