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
package org.exoplatform.services.database;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by The eXo Platform SAS Author : Nhu Dinh Thuan
 * nhudinhthuan@exoplatform.com Mar 29, 2007
 */
public class ReflectionUtil {

  public final static void setValue(Object bean, Field field, Object value) throws Exception {
    Class clazz = bean.getClass();
    Method method = getMethod("set", field, clazz);
    if (method != null)
      method.invoke(bean, new Object[] { value });
    method = getMethod("put", field, clazz);
    if (method != null)
      method.invoke(bean, new Object[] { value });
    field.setAccessible(true);
    field.set(bean, value);
  }

  public final static Object getValue(Object bean, Field field) throws Exception {
    Class clazz = bean.getClass();
    Method method = getMethod("get", field, clazz);
    if (method != null)
      return method.invoke(bean, new Object[] {});
    method = getMethod("is", field, clazz);
    if (method != null)
      return method.invoke(bean, new Object[] {});
    field.setAccessible(true);
    return field.get(bean);
  }

  public final static Method getMethod(String prefix, Field field, Class clazz) throws Exception {
    StringBuilder name = new StringBuilder(field.getName());
    name.setCharAt(0, Character.toUpperCase(name.charAt(0)));
    name.insert(0, prefix);
    try {
      Method method = clazz.getDeclaredMethod(name.toString(), new Class[] {});
      return method;
    } catch (Exception e) {
    }
    return null;
  }

  public final static List<Method> getMethod(Class clazz, String name) throws Exception {
    Method[] methods = clazz.getDeclaredMethods();
    List<Method> list = new ArrayList<Method>();
    for (Method method : methods) {
      if (method.getName().equals(name))
        list.add(method);
    }
    return list;
  }
}
