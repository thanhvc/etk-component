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
package org.etk.common.utils;

import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          thanhvucong.78@google.com
 * Aug 6, 2011  
 */
public class SystemHelper {

  /**
   * Gets system property in privileged mode.
   * 
   * @param key
   * @return
   */
  public static String getProperty(final String key) {

    return System.getProperty(key);

  }

  /**
   * Gets system properties in privileged mode.
   * 
   * @param key
   * @return
   */
  public static Properties getProperties() {

    return System.getProperties();

  }

  /**
   * Gets system property in privileged mode.
   * 
   * @param key
   * @return
   */
  public static void setProperty(final String key, final String value) {

    System.setProperty(key, value);

  }

  /**
   * Gets system property in privileged mode.
   * 
   * @param key
   * @param def
   * @return
   */
  public static String getProperty(final String key, final String def) {

    return System.getProperty(key, def);

  }

  /**
   * Get resource in privileged mode.
   * 
   * @param key
   * @param def
   * @return
   */
  public static URL getResource(final String name) {

    return Thread.currentThread().getContextClassLoader().getResource(name);

  }

  /**
   * Get resource as stream in privileged mode.
   * 
   * @param key
   * @param def
   * @return
   */
  public static InputStream getResourceAsStream(final String name) {

    return Thread.currentThread().getContextClassLoader().getResourceAsStream(name);

  }

}
