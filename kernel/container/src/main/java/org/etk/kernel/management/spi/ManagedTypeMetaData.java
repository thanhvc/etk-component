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
package org.etk.kernel.management.spi;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Meta data that describes a managed type.
 * 
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Jul 28, 2011  
 */
public class ManagedTypeMetaData extends ManagedMetaData {

   /** . */
   private final Class type;

   /** . */
   private final Map<String, ManagedPropertyMetaData> properties;

   /** . */
   private final Map<MethodKey, ManagedMethodMetaData> methods;

  public ManagedTypeMetaData(Class type) throws NullPointerException {
    if (type == null) {
      throw new NullPointerException();
    }

    //
    this.type = type;
    this.properties = new HashMap<String, ManagedPropertyMetaData>();
    this.methods = new HashMap<MethodKey, ManagedMethodMetaData>();
  }

  public Class getType() {
    return type;
  }

  public ManagedPropertyMetaData getProperty(String propertyName) {
    return properties.get(propertyName);
  }

  public void addProperty(ManagedPropertyMetaData property) {
    properties.put(property.getName(), property);
  }

  public void addMethod(ManagedMethodMetaData method) {
    methods.put(new MethodKey(method.getMethod()), method);
  }

  public Collection<ManagedMethodMetaData> getMethods() {
    return methods.values();
  }

  public Collection<ManagedPropertyMetaData> getProperties() {
    return properties.values();
  }

  private static class MethodKey {

    private final String         name;

    private final List<Class<?>> types;

    private MethodKey(Method method) {
      this.name = method.getName();
      this.types = Arrays.asList(method.getParameterTypes());
    }

    @Override
    public int hashCode() {
      int hashCode = name.hashCode();
      for (Class<?> type : types) {
        hashCode = hashCode * 41 + type.hashCode();
      }
      return hashCode;
    }

    @Override
    public boolean equals(Object obj) {
      if (obj == this) {
        return true;
      }
      if (obj instanceof MethodKey) {
        MethodKey that = (MethodKey) obj;
        return this.name.equals(that.name) && this.types.equals(that.types);
      }
      return false;
    }
  }
}
