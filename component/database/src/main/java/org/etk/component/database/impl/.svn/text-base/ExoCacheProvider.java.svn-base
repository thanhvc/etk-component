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

package org.exoplatform.services.database.impl;

import java.io.Serializable;
import java.util.Properties;

import org.hibernate.cache.Cache;
import org.hibernate.cache.CacheException;
import org.hibernate.cache.CacheProvider;
import org.hibernate.cache.Timestamper;

import org.exoplatform.services.cache.CacheService;
import org.exoplatform.services.cache.ExoCache;

/**
 * Created by The eXo Platform SAS .
 * 
 * @author <a href="mailto:gennady.azarenkov@exoplatform.com">Gennady Azarenkov
 *         </a>
 * @version $Id$
 */
@Deprecated
public class ExoCacheProvider implements CacheProvider {

  private CacheService cacheService;

  public ExoCacheProvider(CacheService cacheService) {
    this.cacheService = cacheService;

  }

  public Cache buildCache(String name, Properties properties) throws CacheException {
    try {
      ExoCache<Serializable, Object> cache = cacheService.getCacheInstance(name);
      cache.setMaxSize(5000); // TODO Do we really need override configuration
                              // in this way ?
      return new ExoCachePlugin(cache);
    } catch (Exception ex) {
      ex.printStackTrace();
      throw new CacheException("Cannot instanstiate cache provider");
    }
  }

  public long nextTimestamp() {
    return Timestamper.next();
  }

  /**
   * Callback to perform any necessary initialization of the underlying cache
   * implementation during SessionFactory construction.
   * 
   * @param properties current configuration settings.
   */
  public void start(Properties properties) throws CacheException {

  }

  /**
   * Callback to perform any necessary cleanup of the underlying cache
   * implementation during SessionFactory.close().
   */
  public void stop() {

  }

  public boolean isMinimalPutsEnabledByDefault() {
    return true;
  }

}
