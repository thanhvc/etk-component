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

package org.etk.core.security.web;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.etk.common.logging.Logger;
import org.etk.core.security.ConversationRegistry;
import org.etk.core.security.ConversationState;
import org.etk.core.security.StateKey;
import org.etk.kernel.container.KernelContainer;
import org.etk.kernel.container.KernelContainerContext;
import org.etk.kernel.container.RootContainer;

/**
 * @author <a href="mailto:andrew00x@gmail.com">Andrey Parfonov</a>
 * @version $Id: $
 */
public class ConversationStateListener implements HttpSessionListener {

  /**
   * Logger.
   */
  protected Logger log = Logger.getLogger(ConversationStateListener.class);

  /**
   * {@inheritDoc}
   */
  public void sessionCreated(HttpSessionEvent event) {
    // nothing to do here
  }

  /**
   * Remove {@link ConversationState}. {@inheritDoc}
   */
  public void sessionDestroyed(HttpSessionEvent event) {
    HttpSession httpSession = event.getSession();
    StateKey stateKey = new HttpSessionStateKey(httpSession);

    ConversationRegistry conversationRegistry = (ConversationRegistry) getContainer(httpSession.getServletContext()).getComponentInstanceOfType(ConversationRegistry.class);

    ConversationState conversationState = conversationRegistry.unregister(stateKey);

    if (conversationState != null)
      if (log.isDebugEnabled())
        log.debug("Remove conversation state " + httpSession.getId());

  }

  /**
   * @return actual ExoContainer instance.
   * @deprecated use {@link #getContainer(ServletContext)} instead
   */
  protected KernelContainer getContainer() {
    KernelContainer container = KernelContainerContext.getCurrentContainer();
    if (container instanceof RootContainer) {
      container = RootContainer.getInstance().getPortalContainer("portal");
    }
    return container;
  }

  /**
   * @param sctx {@link ServletContext}
   * @return actual ExoContainer instance
   */
  protected KernelContainer getContainer(ServletContext sctx) {
    KernelContainer container = KernelContainerContext.getCurrentContainer();
    if (container instanceof RootContainer) {
      String containerName = null;
      // check attribute in servlet context first
      if (sctx.getAttribute(SetCurrentIdentityFilter.PORTAL_CONTAINER_NAME) != null)
        containerName = (String) sctx.getAttribute(SetCurrentIdentityFilter.PORTAL_CONTAINER_NAME);

      // if not set then use default name.
      if (containerName == null)
        containerName = "portal";
      container = RootContainer.getInstance().getPortalContainer(containerName);
    }
    return container;
  }

}
