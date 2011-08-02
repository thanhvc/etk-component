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
package org.etk.kernel.management.jmx;

import java.util.Map;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

import org.etk.kernel.management.jmx.annotations.NameTemplate;


/**
 * A builder for object name templates.
 * 
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Jul 28, 2011  
 */
public class ObjectNameBuilder<T>
{

   /** . */
   private String domain;

   /** . */
   private Class<? extends T> clazz;

   /**
    * Create a new builder.
    *
    * @param clazz the class
    * @throws IllegalArgumentException if the object is null
    */
   public ObjectNameBuilder(String domain, Class<? extends T> clazz) throws IllegalArgumentException
   {
      if (clazz == null)
      {
         throw new IllegalArgumentException("Clazz cannot be null");
      }
      this.domain = domain;
      this.clazz = clazz;
   }

   /**
    * Build the object name or return null if the class is not annotated by
    * {@link NameTemplate}.
    *
    * @param object the object
    * @return the built name
    * @throws IllegalStateException raised by a build time issue 
    */
   public ObjectName build(T object) throws IllegalStateException
   {
      PropertiesInfo info = PropertiesInfo.resolve(clazz, NameTemplate.class);

      //
      if (info != null)
      {

         try
         {
            Map<String, String> props = info.resolve(object);
            return JMX.createObjectName(domain, props);
         }
         catch (MalformedObjectNameException e)
         {
            throw new IllegalArgumentException("ObjectName template is malformed", e);
         }
      }

      //
      return null;
   }
}

