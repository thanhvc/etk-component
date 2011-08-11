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
import java.security.Principal;
import java.util.Map;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;


/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          thanhvucong.78@google.com
 * Aug 9, 2011  
 */
public class SimpleLoginModule implements LoginModule {

  private Subject subject;
  private CallbackHandler callBackHandler;
  private String name;
  private String password;
  @Override
  public void initialize(Subject subject,
                         CallbackHandler callbackHandler,
                         Map<String, ?> sharedState,
                         Map<String, ?> options) {
    this.subject = subject;
    this.callBackHandler = callbackHandler;
  }

  @Override
  public boolean login() throws LoginException {
    //Each callback is responsible for collecting the credential
    // needed to authenticate the user.
    NameCallback nameCB = new NameCallback("Username");
    PasswordCallback passwordCB = new PasswordCallback("Password", false);
    Callback[] callbacks = new Callback[] {nameCB, passwordCB};
    try {
      callBackHandler.handle(callbacks);
    } catch (IOException e) {
      e.printStackTrace();
      LoginException ex = new LoginException("IOException logging in.");
      ex.initCause(e);
      throw ex;
    } catch (UnsupportedCallbackException e) {
      String className = e.getCallback().getClass().getName();
      LoginException ex = new LoginException(className + " is not a supported Callback.");
      ex.initCause(e);
      throw ex;
    }

    // Now that the CallbackHandler has gathered the
    // username and password, use them to
    // authenticate the user against the expected passwords.
    name = nameCB.getName();
    password = String.valueOf(passwordCB.getPassword());

    if ("sysadmin".equals(name) && "password".equals(password))
      //login sysadmin
      return true;
    else if ("user".equals(name) && "password".equals(password))
      //login user
      return true;
    else
      return false;
      

  }

  @Override
  public boolean commit() throws LoginException {
    //If this method is called, the user successfully
    //authenticated, we can add the appropriate
    //Principles to the Subject
    if("sysadmin".equals(name)) {
      Principal p = new SysAdminPrincipal(name);
      subject.getPrincipals().add(p);
      password = null;
      return true;
    } else if ("user".equals(name)) {
      Principal p = new UserPrincipal(name);
      subject.getPrincipals().add(p);
      password = null;
      return true;
    } else {
      return false;
    }

    
  }

  @Override
  public boolean abort() throws LoginException {
    name = null;
    password = null;
    return true;


  }

  @Override
  public boolean logout() throws LoginException {
    name = null;
    password = null;
    return true;

  }

}
