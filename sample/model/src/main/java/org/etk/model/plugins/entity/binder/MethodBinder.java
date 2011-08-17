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

import java.lang.reflect.Method;

import org.etk.model.core.MethodInvoker;
import org.etk.model.core.ObjectContext;
import org.etk.reflect.api.MethodInfo;

/*
 * Author : eXoPlatform
 *          thanhvc@exoplatform.com
 * Jul 15, 2011  
 */
public class MethodBinder<C extends ObjectContext<C>> implements MethodInvoker<C> {

  /** . */
  private final MethodInfo method;

  public MethodBinder(MethodInfo method) {
    this.method = method;
  }

  public Object invoke(C context) throws Throwable {
    throw new UnsupportedOperationException();
  }

  public Object invoke(C context, Object args) throws Throwable {
    throw new UnsupportedOperationException();
  }

  public Object invoke(C context, Object[] args) throws Throwable {
    throw new UnsupportedOperationException();
  }

  public MethodInfo getMethod() {
    return method;
  }

  @Override
  public String toString() {
    return "MethodBinder[" + ((Method)method.unwrap()).getDeclaringClass().getName() + "#" + method.getName() + "]";
  }
  
}