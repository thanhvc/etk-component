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

import javax.ws.rs.CookieParam;
import javax.ws.rs.core.Cookie;

import org.exoplatform.services.rest.ApplicationContext;

/**
 * @author <a href="mailto:andrew00x@gmail.com">Andrey Parfonov</a>
 * @version $Id: $
 */
public class CookieParameterResolver extends ParameterResolver<CookieParam> {

  /**
   * See {@link CookieParam}.
   */
  private final CookieParam cookieParam;

  /**
   * @param cookieParam CookieParam
   */
  CookieParameterResolver(CookieParam cookieParam) {
    this.cookieParam = cookieParam;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object resolve(org.exoplatform.services.rest.Parameter parameter,
                        ApplicationContext context) throws Exception {
    String param = this.cookieParam.value();
    Object c = context.getHttpHeaders().getCookies().get(param);
    if (c != null)
      return c;

    if (parameter.getDefaultValue() != null)
      return Cookie.valueOf(parameter.getDefaultValue());

    return null;
  }

}
