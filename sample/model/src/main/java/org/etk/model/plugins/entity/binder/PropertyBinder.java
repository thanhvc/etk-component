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
package org.etk.model.plugins.entity.binder;

import org.etk.model.core.MethodInvoker;
import org.etk.model.core.ObjectContext;
import org.etk.model.plugins.entity.PropertyInfo;
import org.etk.model.plugins.entity.binding.PropertyBinding;
import org.etk.orm.plugins.bean.ValueInfo;
import org.etk.orm.plugins.bean.ValueKind;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Jul 15, 2011  
 */
public abstract class PropertyBinder<P extends PropertyInfo<V, K>, V extends ValueInfo, O extends ObjectContext<O>, K extends ValueKind> {

  /** . */
  protected final Class<O> contextType;

  /** . */
  protected final PropertyBinding<P, V, K> info;

  public PropertyBinder(Class<O> contextType, PropertyBinding<P, V, K> info) {
    this.contextType = contextType;
    this.info = info;
  }

  public Class<O> getType() {
    return contextType;
  }

  public PropertyBinding<P, V, K> getInfo() {
    return info;
  }

  public Object get(O context) throws Throwable {
    throw new UnsupportedOperationException();
  }

  public void set(O context, Object value) throws Throwable {
    throw new UnsupportedOperationException();
  }

  public MethodInvoker<O> getGetter() {
    return getter;
  }

  public MethodInvoker<O> getSetter() {
    return setter;
  }

  private final MethodInvoker<O> getter = new MethodInvoker<O>() {
    public Object invoke(O ctx) throws Throwable {
      return get(ctx);
    }
    public Object invoke(O ctx, Object arg) throws Throwable {
      throw new AssertionError();
    }
    public Object invoke(O ctx, Object[] args) throws Throwable {
      throw new AssertionError();
    }
  };

  private final MethodInvoker<O> setter = new MethodInvoker<O>() {
    public Object invoke(O ctx) throws Throwable {
      throw new AssertionError();
    }
    public Object invoke(O ctx, Object arg) throws Throwable {
      set(ctx, arg);
      return null;
    }
    public Object invoke(O ctx, Object[] args) throws Throwable {
      throw new AssertionError();
    }
  };
}