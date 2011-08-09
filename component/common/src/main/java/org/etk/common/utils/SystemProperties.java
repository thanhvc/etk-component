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
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          thanhvucong.78@google.com
 * Aug 5, 2011  
 */
public class SystemProperties {

  public static final String SYSTEM_PROPERTIES_LOAD =
    "system.properties.load";

  public static final String SYSTEM_PROPERTIES_FINAL =
    "system.properties.final";

  public static final String TMP_DIR = "java.io.tmpdir";

  public static String get(String key) {
    String value = _instance._props.get(key);

    if (value == null) {
      value = System.getProperty(key);
    }

    return value;
  }

  public static void set(String key, String value) {
    System.setProperty(key, value);

    _instance._props.put(key, value);
  }

  public static String[] getArray(String key) {
    String value = get(key);

    if (value == null) {
      return new String[0];
    }
    else {
      return StringUtil.split(value);
    }
  }

  public static Properties getProperties() {
    return PropertiesUtil.fromMap(_instance._props);
  }

  private SystemProperties() {
    Properties p = new Properties();

    ClassLoader classLoader = getClass().getClassLoader();

    // system.properties

    try {
      URL url = classLoader.getResource("system.properties");

      if (url != null) {
        InputStream is = url.openStream();

        p.load(is);

        is.close();

        System.out.println("Loading " + url);
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }

    // system-ext.properties

    try {
      URL url = classLoader.getResource("system-ext.properties");

      if (url != null) {
        InputStream is = url.openStream();

        p.load(is);

        is.close();

        System.out.println("Loading " + url);
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }

    // Set environment properties

    SystemEnv.setProperties(p);

    // Set system properties

    boolean systemPropertiesLoad = GetterUtil.getBoolean(
      System.getProperty(SYSTEM_PROPERTIES_LOAD), true);

    boolean systemPropertiesFinal = GetterUtil.getBoolean(
      System.getProperty(SYSTEM_PROPERTIES_FINAL), true);

    if (systemPropertiesLoad) {
      Enumeration<String> enu = (Enumeration<String>)p.propertyNames();

      while (enu.hasMoreElements()) {
        String key = enu.nextElement();

        if (systemPropertiesFinal ||
          Validator.isNull(System.getProperty(key))) {

          System.setProperty(key, p.getProperty(key));
        }
      }
    }

    _props = new ConcurrentHashMap<String, String>();

    // Use a fast concurrent hash map implementation instead of the slower
    // java.util.Properties

    PropertiesUtil.fromProperties(p, _props);
  }

  private static SystemProperties _instance = new SystemProperties();

  private Map<String, String> _props;

}
