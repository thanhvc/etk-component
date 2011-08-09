/**
 * Copyright (C) 2003-2008 eXo Platform SAS.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see<http://www.gnu.org/licenses/>.
 */

package org.exoplatform.services.security.web;

import javax.security.auth.Subject;
import javax.security.auth.login.LoginContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;

import org.exoplatform.services.security.ConversationRegistry;
import org.exoplatform.services.security.ConversationState;
import org.exoplatform.services.security.StateKey;

/**
 * @author <a href="mailto:andrew00x@gmail.com">Andrey Parfonov</a>
 * @version $Id: $
 */
public class JAASConversationStateListener extends ConversationStateListener {

  /**
   * {@inheritDoc}
   */
  @Override
  public void sessionDestroyed(HttpSessionEvent event) {
    HttpSession httpSession = event.getSession();
    StateKey stateKey = new HttpSessionStateKey(httpSession);
    try {
      ConversationRegistry conversationRegistry = (ConversationRegistry) getContainer(httpSession.getServletContext()).getComponentInstanceOfType(ConversationRegistry.class);

      ConversationState conversationState = conversationRegistry.unregister(stateKey);

      if (conversationState != null) {
        if (log.isDebugEnabled())
          log.debug("Remove conversation state " + httpSession.getId());
        if (conversationState.getAttribute(ConversationState.SUBJECT) != null) {
          Subject subject = (Subject) conversationState.getAttribute(ConversationState.SUBJECT);
          LoginContext ctx = new LoginContext("exo-domain", subject);
          ctx.logout();
        } else {
          log.warn("Subject was not found in ConversationState attributes.");
        }
      }

    } catch (Exception e) {
      log.error("Can't remove conversation state " + httpSession.getId());
    }
  }

}
