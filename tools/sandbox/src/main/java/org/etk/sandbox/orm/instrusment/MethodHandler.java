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
package org.etk.sandbox.orm.instrusment;

import java.lang.reflect.Method;

/**
 * It supports to receive the <code>Method</code> and <code>Object</code> target,
 * and then invokes the given method.
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          thanhvucong.78@google.com
 * Aug 18, 2011  
 */

public interface MethodHandler {

  /**
   * Invokes a zero argument method.
   *
   * @param o the target
   * @param method the method to invoke
   * @return the invocation returned value
   * @throws Throwable any throwable
   */
  Object invoke(Object o, Method method) throws Throwable;

  /**
   * Invokes a one argument method.
   *
   * @param o the target
   * @param method the method to invoke
   * @param arg the method argument
   * @return the invocation returned value
   * @throws Throwable any throwable
   */
  Object invoke(Object o, Method method, Object arg) throws Throwable;

  /**
   * Invokes a multi argument method.
   *
   * @param o the target
   * @param method the method to invoke
   * @param args the method arguments packed in an array
   * @return the invocation returned value
   * @throws Throwable any throwable
   */
  Object invoke(Object o, Method method, Object[] args) throws Throwable;

}