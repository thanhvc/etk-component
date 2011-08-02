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

package org.exoplatform.services.rest.impl.provider;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.exoplatform.services.log.Log;
import org.exoplatform.container.component.BaseComponentPlugin;
import org.exoplatform.container.xml.InitParams;
import org.exoplatform.container.xml.ValueParam;
import org.exoplatform.services.log.ExoLogger;

/**
 * For injection JAXBContext from configuration at startup in
 * {@link JAXBContextResolver}.
 * 
 * @author <a href="mailto:andrew00x@gmail.com">Andrey Parfonov</a>
 * @version $Id: $
 */
public class JAXBContextComponentPlugin extends BaseComponentPlugin {

  /**
   * Logger.
   */
  private static final Log    LOG = ExoLogger.getLogger(JAXBContextComponentPlugin.class.getName());

  /**
   * Set of classes that will be bounded.
   */
  private final Set<Class<?>> jcs = new HashSet<Class<?>>();

  /**
   * @param params initialize parameters
   * @see InitParams
   */
  @SuppressWarnings("unchecked")
  public JAXBContextComponentPlugin(InitParams params) {
    if (params != null) {
      Iterator<ValueParam> i = params.getValueParamIterator();
      while (i.hasNext()) {
        ValueParam v = i.next();
        try {
          jcs.add(Class.forName(v.getValue()));
        } catch (ClassNotFoundException e) {
          LOG.warn("Failed load class " + v.getValue(), e);
        }
      }
    }
  }

  /**
   * @return collection of classes to be bound
   */
  public Set<Class<?>> getJAXBContexts() {
    return jcs;
  }

}
