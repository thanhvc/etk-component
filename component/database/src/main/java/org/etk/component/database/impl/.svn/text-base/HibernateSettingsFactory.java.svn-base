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

import java.util.Properties;

import org.hibernate.HibernateException;
import org.hibernate.cache.CacheProvider;
import org.hibernate.cfg.SettingsFactory;

/**
 * Created by The eXo Platform SAS .
 * 
 * @author <a href="mailto:gennady.azarenkov@exoplatform.com">Gennady
 *         Azarenkov</a>
 * @version $Id: HibernateSettingsFactory.java 5332 2006-04-29 18:32:44Z geaz $
 *          Hibernate's SettingsFactory for configure settings
 * @see SettingsFactory
 */
public class HibernateSettingsFactory extends SettingsFactory {

  private static final String HIBERNATE_CACHE_PROPERTY = "hibernate.cache.provider_class";

  private ExoCacheProvider    cacheProvider;

  public HibernateSettingsFactory(ExoCacheProvider cacheProvider) throws HibernateException {
    super();
    this.cacheProvider = cacheProvider;
  }

  protected CacheProvider createCacheProvider(Properties properties) {
    properties.setProperty(HIBERNATE_CACHE_PROPERTY, ExoCacheProvider.class.getName());
    return cacheProvider;
  }

}
