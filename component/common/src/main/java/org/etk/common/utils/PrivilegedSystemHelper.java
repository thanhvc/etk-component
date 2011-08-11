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
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Properties;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          thanhvucong.78@google.com
 * Aug 11, 2011  
 */
public class PrivilegedSystemHelper
{

   /**
    * Gets system property in privileged mode.
    * 
    * @param key
    * @return
    */
   public static String getProperty(final String key)
   {
      PrivilegedAction<String> action = new PrivilegedAction<String>()
      {
         public String run()
         {
            return System.getProperty(key);
         }
      };
      return AccessController.doPrivileged(action);
   }

   /**
    * Gets system properties in privileged mode.
    * 
    * @param key
    * @return
    */
   public static Properties getProperties()
   {
      PrivilegedAction<Properties> action = new PrivilegedAction<Properties>()
      {
         public Properties run()
         {
            return System.getProperties();
         }
      };
      return AccessController.doPrivileged(action);
   }

   /**
    * Gets system property in privileged mode.
    * 
    * @param key
    * @return
    */
   public static void setProperty(final String key, final String value)
   {
      PrivilegedAction<Void> action = new PrivilegedAction<Void>()
      {
         public Void run()
         {
            System.setProperty(key, value);
            return null;
         }
      };
      AccessController.doPrivileged(action);
   }

   /**
    * Gets system property in privileged mode.
    * 
    * @param key
    * @param def
    * @return
    */
   public static String getProperty(final String key, final String def)
   {
      PrivilegedAction<String> action = new PrivilegedAction<String>()
      {
         public String run()
         {
            return System.getProperty(key, def);
         }
      };
      return AccessController.doPrivileged(action);
   }

   /**
    * Get resource in privileged mode.
    * 
    * @param key
    * @param def
    * @return
    */
   public static URL getResource(final String name)
   {
      PrivilegedAction<URL> action = new PrivilegedAction<URL>()
      {
         public URL run()
         {
            return Thread.currentThread().getContextClassLoader().getResource(name);
         }
      };
      return AccessController.doPrivileged(action);
   }

   /**
    * Get resource as stream in privileged mode.
    * 
    * @param key
    * @param def
    * @return
    */
   public static InputStream getResourceAsStream(final String name)
   {
      PrivilegedAction<InputStream> action = new PrivilegedAction<InputStream>()
      {
         public InputStream run()
         {
            return Thread.currentThread().getContextClassLoader().getResourceAsStream(name);
         }
      };
      return AccessController.doPrivileged(action);
   }

}

