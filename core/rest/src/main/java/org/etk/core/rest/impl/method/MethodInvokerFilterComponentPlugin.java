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

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.etk.common.logging.Logger;
import org.etk.core.rest.method.MethodInvokerFilter;
import org.etk.kernel.container.component.BaseComponentPlugin;
import org.etk.kernel.container.xml.InitParams;
import org.etk.kernel.container.xml.ValueParam;


/**
 * For injection {@link MethodInvokerFilter} in {@link RequestHandler} at
 * startup.
 * 
 */
public class MethodInvokerFilterComponentPlugin extends BaseComponentPlugin {

  /**
   * Logger.
   */
  private static final Logger LOG  = Logger.getLogger(MethodInvokerFilterComponentPlugin.class);

  /**
   * List of {@link MethodInvokerFilter}.
   */
  private Set<Class<? extends MethodInvokerFilter>> mifs = new HashSet<Class<? extends MethodInvokerFilter>>();

  /**
   * @param params initialize parameters from configuration
   * @see InitParams
   */
  @SuppressWarnings("unchecked")
  public MethodInvokerFilterComponentPlugin(InitParams params) {
    if (params != null) {
      Iterator<ValueParam> i = params.getValueParamIterator();
      while (i.hasNext()) {
        ValueParam v = i.next();
        try {
          mifs.add((Class<? extends MethodInvokerFilter>) Class.forName(v.getValue()));
        } catch (ClassNotFoundException e) {
          LOG.error("Failed load class " + v.getValue(), e);
        }
      }
    }
  }

  /**
   * @return get collection of classes MethodInvokerFilter supplied from
   *         configuration
   */
  public Set<Class<? extends MethodInvokerFilter>> getFilters() {
    return mifs;
  }

}
