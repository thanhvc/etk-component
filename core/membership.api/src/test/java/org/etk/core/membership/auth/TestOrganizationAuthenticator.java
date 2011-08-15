/*
 * Copyright (C) 2009 eXo Platform SAS.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.etk.core.membership.auth;

import junit.framework.TestCase;

import org.etk.core.membership.auth.OrganizationAuthenticatorImpl;
import org.etk.core.security.Authenticator;
import org.etk.core.security.ConversationRegistry;
import org.etk.core.security.Credential;
import org.etk.core.security.Identity;
import org.etk.core.security.PasswordCredential;
import org.etk.core.security.UsernameCredential;
import org.etk.kernel.container.StandaloneContainer;


import java.net.URL;

/**
 * Created y the eXo platform team User: Benjamin Mestrallet Date: 28 avr. 2004
 */
public class TestOrganizationAuthenticator extends TestCase
{

   protected ConversationRegistry registry;

   protected Authenticator authenticator;

   public TestOrganizationAuthenticator(String name)
   {
      super(name);
   }

   protected void setUp() throws Exception
   {

      if (registry == null)
      {
         URL containerConfURL =
            TestOrganizationAuthenticator.class.getResource("/conf/standalone/test-configuration.xml");
         assertNotNull(containerConfURL);
         String containerConf = containerConfURL.toString();
         URL loginConfURL = TestOrganizationAuthenticator.class.getResource("/login.conf");
         assertNotNull(loginConfURL);
         String loginConf = loginConfURL.toString();
         StandaloneContainer.addConfigurationURL(containerConf);
         if (System.getProperty("java.security.auth.login.config") == null)
            System.setProperty("java.security.auth.login.config", loginConf);

         StandaloneContainer container = StandaloneContainer.getInstance();

         authenticator = (Authenticator)container.getComponentInstanceOfType(OrganizationAuthenticatorImpl.class);
         assertNotNull(authenticator);

         registry = (ConversationRegistry)container.getComponentInstanceOfType(ConversationRegistry.class);
         assertNotNull(registry);

      }

   }

   public void testAuthenticator() throws Exception
   {
      assertNotNull(authenticator);
      assertTrue(authenticator instanceof OrganizationAuthenticatorImpl);
      Credential[] cred = new Credential[]{new UsernameCredential("admin"), new PasswordCredential("admin")};
      String userId = authenticator.validateUser(cred);
      assertEquals("admin", userId);
      Identity identity = authenticator.createIdentity(userId);
      assertTrue(identity.getGroups().size() > 0);
   }
}
