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

package org.etk.core.security.jaas;

import java.util.Map;

import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;

import org.etk.common.logging.Logger;
import org.etk.core.security.Authenticator;
import org.etk.core.security.Identity;
import org.etk.core.security.IdentityRegistry;
import org.etk.kernel.container.KernelContainer;
import org.etk.kernel.container.KernelContainerContext;
import org.etk.kernel.container.RootContainer;

/**
 * This LoginModule should be used after customer LoginModule, which makes
 * authentication. This one registers Identity for user in IdentityRegistry.
 * Required name of user MUST be passed to LM via sharedState (see method
 * {@link #initialize(Subject, CallbackHandler, Map, Map)}), with name
 * javax.security.auth.login.name.
 * 
 */
public class IdentitySetLoginModule implements LoginModule {

  /**
   * The name of the option to use in order to specify the name of the portal
   * container
   */
  private static final String OPTION_PORTAL_CONTAINER_NAME = "portalContainerName";

  /**
   * The default name of the portal container
   */
  private static final String DEFAULT_PORTAL_CONTAINER_NAME = "portal";

  /**
   * Login.
   */
  protected Logger log = Logger.getLogger(IdentitySetLoginModule.class);

  /**
   * @see {@link Subject} .
   */
  protected Subject subject;

  /**
   * Shared state.
   */
  @SuppressWarnings("unchecked")
  protected Map sharedState;

  /**
   * Is allowed for one user login again if he already login. If must set in LM
   * options.
   */
  protected boolean singleLogin = false;

  /**
   * The name of the portal container.
   */
  private String portalContainerName;

  /**
   * {@inheritDoc}
   */
  public boolean abort() throws LoginException {
    if (log.isDebugEnabled()) {
      log.debug("in abort");
    }

    return true;
  }

  /**
   * {@inheritDoc}
   */
  public boolean commit() throws LoginException {
    if (log.isDebugEnabled()) {
      log.debug("in commit");
    }

    String userId = (String) sharedState.get("javax.security.auth.login.name");
    try {
      Authenticator authenticator = (Authenticator) getContainer().getComponentInstanceOfType(Authenticator.class);

      if (authenticator == null)
        throw new LoginException("No Authenticator component found, check your configuration.");

      IdentityRegistry identityRegistry = (IdentityRegistry) getContainer().getComponentInstanceOfType(IdentityRegistry.class);

      if (singleLogin && identityRegistry.getIdentity(userId) != null)
        throw new LoginException("User " + userId + " already logined.");

      Identity identity = authenticator.createIdentity(userId);
      identity.setSubject(subject);

      identityRegistry.register(identity);

    } catch (Exception e) {
      e.printStackTrace();
      throw new LoginException(e.getMessage());
    }
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  public void initialize(Subject subject,
                         CallbackHandler callbackHandler,
                         Map sharedState,
                         Map options) {
    if (log.isDebugEnabled()) {
      log.debug("in initialize");
    }

    this.subject = subject;
    this.sharedState = sharedState;
    this.portalContainerName = getPortalContainerName(options);

    String sl = (String) options.get("singleLogin");
    if (sl != null && (sl.equalsIgnoreCase("yes") || sl.equalsIgnoreCase("true"))) {
      this.singleLogin = true;
    }
  }

  /**
   * {@inheritDoc}
   */
  public boolean login() throws LoginException {
    if (log.isDebugEnabled()) {
      log.debug("in login");
    }
    return true;
  }

  /**
   * {@inheritDoc}
   */
  public boolean logout() throws LoginException {
    if (log.isDebugEnabled()) {
      log.debug("in logout");
    }
    return true;
  }

  /**
   * @return actual ExoContainer instance.
   */
  protected KernelContainer getContainer() throws Exception {
    // TODO set correct current container
    KernelContainer container = KernelContainerContext.getCurrentContainer();
    if (container instanceof RootContainer) {
      container = RootContainer.getInstance().getPortalContainer(portalContainerName);
    }
    return container;
  }


  @SuppressWarnings("unchecked")
  private String getPortalContainerName(Map options) {
    if (options != null) {
      String optionValue = (String) options.get(OPTION_PORTAL_CONTAINER_NAME);
      if (optionValue != null && optionValue.length() > 0) {
        if (log.isDebugEnabled()) log.debug("The IdentitySetLoginModule will use the portal container " + optionValue);
        return optionValue;
      }
    }
    return DEFAULT_PORTAL_CONTAINER_NAME;
  }
}
