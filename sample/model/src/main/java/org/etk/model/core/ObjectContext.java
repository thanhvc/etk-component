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
package org.etk.model.core;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jcr.RepositoryException;

import org.etk.model.core.entity.EntityTypeInfo;
import org.etk.model.plugins.entity.binder.ObjectBinder;
import org.etk.model.plugins.instrument.MethodHandler;
import org.etk.model.plugins.vt2.ValueDefinition;
import org.etk.orm.api.ORMIOException;
import org.etk.orm.api.UndeclaredRepositoryException;
import org.etk.orm.core.ListType;
import org.etk.orm.plugins.common.CloneableInputStream;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Jul 15, 2011  
 */
public abstract class ObjectContext<O extends ObjectContext<O>> implements MethodHandler {

  /** . */
  private final Map<String, Object> propertyCache;
  public abstract ObjectBinder<O> getMapper();

  public abstract Object getObject();

  public abstract EntityContext getEntity();
  
  
  public ObjectContext() {
    this.propertyCache = new HashMap<String, Object>();
  }
  

  /**
   * Returns the type info associated with the context. Null is returned when the context is in transient
   * state, otherwise the type info of the corresponding node is returned.
   *
   * @return the type info
   */
  public abstract EntityTypeInfo getTypeInfo();

    public abstract EntitySession getSession();

  public final Object invoke(Object o, Method method) throws Throwable {
    MethodInvoker<O> invoker = getMapper().getInvoker(method);
    if (invoker != null) {
      return invoker.invoke((O)this);
    } else {
      throw createCannotInvokeError(method);
    }
  }

  public final Object invoke(Object o, Method method, Object arg) throws Throwable {
    MethodInvoker<O> invoker = getMapper().getInvoker(method);
    if (invoker != null) {
      return invoker.invoke((O)this, arg);
    } else {
      throw createCannotInvokeError(method, arg);
    }
  }

  public final Object invoke(Object o, Method method, Object[] args) throws Throwable {
    MethodInvoker<O> invoker = getMapper().getInvoker(method);
    if (invoker != null) {
      switch (args.length) {
        case 0:
          return invoker.invoke((O)this);
        case 1:
          return invoker.invoke((O)this, args[0]);
        default:
          return invoker.invoke((O)this, args);
      }
    } else {
      throw createCannotInvokeError(method, (Object[])args);
    }
  }

  private AssertionError createCannotInvokeError(Method method, Object... args) {
    StringBuilder msg = new StringBuilder("Cannot invoke method ").append(method.getName()).append("(");
    Class[] parameterTypes = method.getParameterTypes();
    for (int i = 0;i < parameterTypes.length;i++) {
      if (i > 0) {
        msg.append(',');
      }
      msg.append(parameterTypes[i].getName());
    }
    msg.append(") with arguments (");
    for (int i = 0;i < args.length;i++) {
      if (i > 0) {
        msg.append(',');
      }
      msg.append(String.valueOf(args[i]));
    }
    msg.append(")");
    return new AssertionError(msg);
  }

  public final <V> V getPropertyValue(String propertyName, ValueDefinition<?, V> type) throws RepositoryException {
 
    EntityTypeInfo typeInfo = getTypeInfo();
    return getPropertyValue(typeInfo, propertyName, type);
  }

  public final <V> List<V> getPropertyValues(String propertyName, ValueDefinition<?, V> simpleType, ListType listType) throws RepositoryException {
    //
    EntityTypeInfo typeInfo = getTypeInfo();
    return getPropertyValues(typeInfo, propertyName, simpleType, listType);
  }

  public final <V> void setPropertyValue(String propertyName, ValueDefinition<?, V> type, V o) throws RepositoryException {
   
    Object object = getObject();

    EntityTypeInfo typeInfo = getTypeInfo();

    //
    if (o instanceof InputStream) {
      CloneableInputStream in;
      try {
        in = new CloneableInputStream((InputStream)o);
      }
      catch (IOException e) {
        throw new ORMIOException("Could not read stream", e);
      }
      @SuppressWarnings("unchecked") V v = (V)in;
      setPropertyValue(typeInfo, propertyName, type, v);
    } else {
      setPropertyValue(typeInfo, propertyName, type, o);
    }
  }

  public final <V> void setPropertyValues(String propertyName, ValueDefinition<?, V> type, ListType listType, List<V> objects) throws RepositoryException {
    //
    EntityTypeInfo typeInfo = getTypeInfo();

    //
    setPropertyValues(typeInfo, propertyName, type, listType, objects);
  }
  
  /**
   * 
   * @param <V>
   * @param entityTypeInfo
   * @param propertyName
   * @param vt
   * @return
   */
  private <V> V getPropertyValue(EntityTypeInfo entityTypeInfo, String propertyName, ValueDefinition<?, V> vt) {
    try {
      

     //
      V value = null;
      //
      if (propertyCache != null) {
        // That must be ok
        value = (V)propertyCache.get(propertyName);
      }

      //
      if (value == null) {
        Object propertyValue = entityTypeInfo.getValue(propertyName);
        vt = (ValueDefinition<?, V>)ValueDefinition.get(propertyValue);
        value = vt.getValue(propertyValue);
      }
      //
      return value;
    }
    catch (RepositoryException e) {
      throw new UndeclaredRepositoryException(e);
    }
  }


  <V> List<V> getPropertyValues(EntityTypeInfo entityTypeInfo, String propertyName, ValueDefinition<?, V> vt, ListType listType) {
    
    return null;
  }

  /**
   * 
   * @param <V>
   * @param entityTypeInfo
   * @param propertyName
   * @param vt
   * @param propertyValue
   */
  private <V> void setPropertyValue(EntityTypeInfo entityTypeInfo, String propertyName, ValueDefinition<?, V> vt, V propertyValue) {
    //
    if (propertyCache != null) {
      if (propertyValue instanceof InputStream && (propertyValue instanceof CloneableInputStream)) {
        try {
          propertyValue = (V) new CloneableInputStream((InputStream) propertyValue);
        } catch (IOException e) {
          throw new ORMIOException("Could not read stream", e);
        }
      }
    }

    entityTypeInfo.setProperty(propertyName, propertyValue);

    //
    if (propertyCache != null) {
      if (propertyValue != null) {
        if (propertyValue instanceof InputStream) {
          CloneableInputStream stream = ((CloneableInputStream) propertyValue);
          propertyValue = (V) stream.clone();
        } else if (propertyValue instanceof Date) {
          propertyValue = (V) ((Date) propertyValue).clone();
        }
        propertyCache.put(propertyName, propertyValue);
      } else {
        propertyCache.remove(propertyName);
      }
    }
  
  }

  <V> void setPropertyValues(EntityTypeInfo entityTypeInfo, String propertyName, ValueDefinition<?, V> vt, ListType listType, List<V> propertyValues) {
   
  }
  
  
}
