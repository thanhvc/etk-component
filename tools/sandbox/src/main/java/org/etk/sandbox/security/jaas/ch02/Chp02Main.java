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

import java.io.File;

import javax.security.auth.Subject;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          thanhvucong.78@google.com
 * Aug 9, 2011  
 */
public class Chp02Main {
  
  public static void main(String[] args) throws Exception {
    File policyFile = new File("/conf/chp02.policy");
    testAccess(policyFile, "user", "passowrd");
    testAccess(policyFile, "sysadmin", "password");
  }
  
  /**
   * The method testAccess() is used to test a specific 
   * user’s ability to read the Policy file.
   * @param policyFile
   * @param userName
   * @param password
   * @throws LoginException
   */
  static void testAccess(final File policyFile, String userName, String password) throws LoginException {
    //a custom CallbackHandler , SimpleCallbackHandler is instantiated and passed
    //to the LoginMadule
    SimpleCallbackHandler cb = new SimpleCallbackHandler(userName, password);
    LoginContext ctx = new LoginContext("chp02", cb);
    ctx.login();
    Subject subject = ctx.getSubject();
    System.out.println("Logged in " + subject);
    
    try {
      
    } catch (SecurityException e) {
      System.out.println(userName + " can NOT access Policy file.");
    }
  }
  

}
