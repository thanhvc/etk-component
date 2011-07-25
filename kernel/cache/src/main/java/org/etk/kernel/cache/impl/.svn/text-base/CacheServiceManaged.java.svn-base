/*
 * Copyright (C) 2003-2007 eXo Platform SAS.
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
package org.exoplatform.services.cache.impl;

import org.exoplatform.management.annotations.Managed;
import org.exoplatform.management.annotations.ManagedDescription;
import org.exoplatform.management.ManagementAware;
import org.exoplatform.management.ManagementContext;
import org.exoplatform.management.jmx.annotations.Property;
import org.exoplatform.management.jmx.annotations.NameTemplate;
import org.exoplatform.services.cache.ExoCache;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
@Managed
@NameTemplate(@Property(key="service", value="cachemanager"))
@ManagedDescription("Cache manager")
public class CacheServiceManaged implements ManagementAware {

  /** . */
  private ManagementContext context;

  /** . */
  private CacheServiceImpl cacheService;

  public CacheServiceManaged(CacheServiceImpl cacheService) {
    this.cacheService = cacheService;

    //
    cacheService.managed = this;
  }

  @Managed
  @ManagedDescription("Clear all registered cache instances")
  public void clearCaches() {
    for (Object o : cacheService.getAllCacheInstances()) {
      try {
        ((ExoCache)o).clearCache();
      }
      catch (Exception wtf) {
      }
    }
  }

  public void setContext(ManagementContext context) {
    this.context = context;
  }

  void registerCache(ExoCache cache) {
    if (context != null) {
      context.register(cache);
    }
  }
}
