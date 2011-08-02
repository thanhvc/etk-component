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

package org.etk.core.rest.impl.provider;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.etk.common.logging.Logger;
import org.etk.core.rest.provider.EntityProvider;
import org.etk.kernel.container.component.BaseComponentPlugin;
import org.etk.kernel.container.xml.InitParams;
import org.etk.kernel.container.xml.ValueParam;


/**
 * @author <a href="mailto:andrew00x@gmail.com">Andrey Parfonov</a>
 * @version $Id: $
 */
@SuppressWarnings("unchecked")
public class EntityProviderComponentPlugin extends BaseComponentPlugin {

  /**
   * Logger.
   */
  private static final Logger LOG = Logger.getLogger(EntityProviderComponentPlugin.class);

  /**
   * See {@link EntityProvider}.
   */
  private Set<Class<? extends EntityProvider>> eps = new HashSet<Class<? extends EntityProvider>>();

  /**
   * @param params initialize parameters
   * @see InitParams
   */
  public EntityProviderComponentPlugin(InitParams params) {
    if (params != null) {
      Iterator<ValueParam> i = params.getValueParamIterator();
      while (i.hasNext()) {
        ValueParam v = i.next();
        try {
          eps.add((Class<? extends EntityProvider>) Class.forName(v.getValue()));
        } catch (ClassNotFoundException e) {
          LOG.error("Failed load class " + v.getValue(), e);
        }
      }
    }
  }

  /**
   * @return the entityProviders supplied from configuration
   */
  public Set<Class<? extends EntityProvider>> getEntityProviders() {
    return eps;
  }

}
