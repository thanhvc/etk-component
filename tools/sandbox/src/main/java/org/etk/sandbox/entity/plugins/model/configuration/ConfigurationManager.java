/*
 * Copyright (C) 2003-2011 eXo Platform SAS.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.etk.sandbox.entity.plugins.model.configuration;

import java.io.InputStream;
import java.net.URL;
import java.util.Collection;

import org.etk.sandbox.entity.plugins.model.xml.Configuration;

/**
 * Created by The eXo Platform SAS Author : 
 * eXoPlatform exo@exoplatform.com Aug
 * 29, 2011
 */
public interface ConfigurationManager {
  /**
   * The name of the system property that indicates whether the logger of the configuration
   * must be in debug more or not.
   */
  public static final String LOG_DEBUG_PROPERTY = "org.etk.entity.configuration.debug";

  /**
   * Constant that indicates whether the logger of the configuration
   * must be in debug more or not.
   */
  public static final boolean LOG_DEBUG = System.getProperty(LOG_DEBUG_PROPERTY) != null;
  /**
   * Retrieving the Configuration which contains the information in the xml
   * file.
   * 
   * @return
   */
  public Configuration getConfiguration();

  public void addConfiguration(String url) throws Exception;

  public void addConfiguration(Collection urls) throws Exception;

  public void addConfiguration(URL url) throws Exception;

  public URL getResource(String url, String defaultURL) throws Exception;

  public URL getResource(String url) throws Exception;

  public InputStream getInputStream(String url, String defaultURL) throws Exception;

  public InputStream getInputStream(String url) throws Exception;

  public boolean isDefault(String value);

  public URL getURL(String uri) throws Exception;

}
