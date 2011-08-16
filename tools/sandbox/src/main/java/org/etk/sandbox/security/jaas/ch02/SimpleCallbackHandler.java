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
package org.etk.sandbox.security.jaas.ch02;

import java.io.IOException;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          thanhvucong.78@google.com
 * Aug 9, 2011  
 */
public class SimpleCallbackHandler implements CallbackHandler {

  private String name;
  private String password;
  
  public SimpleCallbackHandler(String name, String password) {
    this.name = name;
    this.password = password;
    
  }
  @Override
  public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
   for(int i = 0; i< callbacks.length; i++) {
     Callback callback = callbacks[i];
     if (callback  instanceof NameCallback) {
       NameCallback nameCB = (NameCallback) callback;
       nameCB.setName(name);
     } else if (callback instanceof PasswordCallback) {
       PasswordCallback passwordCB = (PasswordCallback) callback;
       passwordCB.setPassword(password.toCharArray());
     }
   }
    
  }

}
