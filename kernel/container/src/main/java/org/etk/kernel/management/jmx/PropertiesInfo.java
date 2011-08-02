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

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import org.etk.common.utils.AnnotationIntrospector;
import org.etk.kernel.management.jmx.annotations.NamingContext;
import org.etk.kernel.management.jmx.annotations.NameTemplate;
import org.etk.kernel.management.jmx.annotations.Property;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Jul 28, 2011  
 */
public class PropertiesInfo {

  /** . */
  private final Map<String, PropertyInfo> properties;

  public PropertiesInfo(Map<String, PropertyInfo> properties) {
    this.properties = properties;
  }

   public static PropertiesInfo resolve(Class clazz, Class<? extends Annotation> annotationClass)
   {
      Annotation tpl2 = AnnotationIntrospector.resolveClassAnnotations(clazz, annotationClass);
      Property[] blah = null;
    if (tpl2 instanceof NamingContext) {
      blah = ((NamingContext) tpl2).value();
    } else if (tpl2 instanceof NameTemplate) {
      blah = ((NameTemplate) tpl2).value();
    }
    if (blah != null) {
      Map<String, PropertyInfo> properties = new LinkedHashMap<String, PropertyInfo>();
      for (Property property : blah) {
        PropertyInfo propertyInfo = new PropertyInfo(clazz, property);
        properties.put(propertyInfo.getKey(), propertyInfo);
      }
      return new PropertiesInfo(properties);
    } else {
      return null;
    }
  }

  public Collection<PropertyInfo> getProperties() {
    return properties.values();
  }

  public MBeanScopingData resolve(Object instance) {
    MBeanScopingData props = new MBeanScopingData();
    for (PropertyInfo propertyInfo : properties.values()) {
      String key = propertyInfo.getKey();
      String value = propertyInfo.resolveValue(instance);
      props.put(key, value);
    }
    return props;
  }
}

