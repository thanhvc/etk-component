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
package org.etk.sandbox.orm.core;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.jcr.RepositoryException;

import org.etk.model.core.MethodInvoker;
import org.etk.model.core.entity.EntityTypeInfo;
import org.etk.model.plugins.vt2.ValueDefinition;
import org.etk.sandbox.orm.binder.ObjectBinder;
import org.etk.sandbox.orm.info.ETKInfo;
import org.etk.sandbox.orm.instrusment.MethodHandler;



/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          thanhvucong.78@google.com
 * Aug 18, 2011  
 */
public abstract class ObjectContext<O extends ObjectContext<O>> implements MethodHandler {

  private final Map<String, Object> propertyCache;
  public abstract ObjectBinder<O> getMapper();

  public abstract Object getObject();
  public abstract EntityContext getEntity();
  
  public ObjectContext() {
    this.propertyCache = new HashMap<String, Object>();
  }
  
  public abstract ETKInfo getETKInfo();
 
  /**
   * Invokes the method
   */
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
  
  public final <V> V getPropertyValue(String propertyName, int valueType) {
    
    ETKInfo typeInfo = getETKInfo();
    return getPropertyValue(typeInfo, propertyName, type);
  }

  private V getPropertyValue(ETKInfo typeInfo, String propertyName, int valueType) {
    return null;
  }
}
