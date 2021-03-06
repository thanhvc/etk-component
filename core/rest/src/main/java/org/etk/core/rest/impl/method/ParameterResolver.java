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

package org.etk.core.rest.impl.method;

import org.etk.core.rest.impl.ApplicationContext;

/**
 * @param <T> on of JAX-RS annotation that used for method parameters
 */
public abstract class ParameterResolver<T> {

  /**
   * Create object which will be passed in resource method or locator. Object is
   * instance of {@link MethodParameterImpl#getParameterClass()}.
   * 
   * @param parameter See {@link Parameter}
   * @param context See {@link ApplicationContext}
   * @return newly created instance of class
   *         {@link MethodParameterImpl#getParameterClass()}
   * @throws Exception if any errors occurs
   */
  public abstract Object resolve(org.etk.core.rest.Parameter parameter, ApplicationContext context) throws Exception;

}
