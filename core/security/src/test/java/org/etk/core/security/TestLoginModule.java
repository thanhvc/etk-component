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
package org.etk.core.security;

import java.net.URL;

import javax.security.auth.login.LoginContext;

import org.etk.core.security.jaas.BasicCallbackHandler;
import org.etk.kernel.container.StandaloneContainer;

import junit.framework.TestCase;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          thanhvucong.78@google.com
 * Aug 6, 2011  
 */
public class TestLoginModule extends TestCase {
  protected ConversationRegistry conversationRegistry;
  protected IdentityRegistry identityRegistry;
  protected Authenticator authenticator;
  
  public TestLoginModule(String name) {
    super(name);
  }
  
  protected void setUp() throws Exception {
    if (conversationRegistry == null) {
      ClassLoader cl = getClass().getClassLoader();
      URL containerConfURL = cl.getResource("conf/standalone/test-configuration.xml");
      assertNotNull(containerConfURL.toString());
      
      //
      String containerConf = containerConfURL.toString();
      URL loginConfURL = cl.getResource("conf/login.conf");
      //
      assertNotNull(loginConfURL.toString());
      String loginConf = loginConfURL.toString();
      //
      StandaloneContainer.addConfigurationURL(containerConf);
      
      if (System.getProperty("java.security.auth.login.config") == null) {
        System.setProperty("java.security.auth.login.config", loginConf);
      }
      //
      StandaloneContainer container = StandaloneContainer.getInstance();
      //
      authenticator = (DummyAuthenticatorImpl) container.getComponentInstancesOfType(DummyAuthenticatorImpl.class);
      assertNotNull(authenticator);
      //
      conversationRegistry = (ConversationRegistry) container.getComponentInstanceOfType(ConversationRegistry.class);
      assertNotNull(conversationRegistry);
      
      //
      identityRegistry = (IdentityRegistry) container.getComponentAdapterOfType(IdentityRegistry.class);
      assertNotNull(identityRegistry);
    }
    
    identityRegistry.clear();
    conversationRegistry.clear();
  }
  
  public void testLogin() throws Exception {
    BasicCallbackHandler handler = new BasicCallbackHandler("exo", "exo".toCharArray());
    LoginContext loginContext = new LoginContext("exo", handler);
    loginContext.login();
    
    assertNotNull(identityRegistry.getIdentity("exo"));
    assertEquals("exo", identityRegistry.getIdentity("exo").getUserId());
    assertEquals(1, identityRegistry.getIdentity("exo").getGroups().size());
    
    StateKey key = new SimpleStateKey("exo");
    conversationRegistry.register(key, new ConversationState(identityRegistry.getIdentity("exo")));
    
    
  }

  
}
