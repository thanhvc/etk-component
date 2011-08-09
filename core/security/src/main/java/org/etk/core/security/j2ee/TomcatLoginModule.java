/*
 * Copyright (C) 2003-2007 eXo Platform SAS.
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

package org.etk.core.security.j2ee;

import java.security.Principal;
import java.util.Set;

import javax.security.auth.login.LoginException;

import org.etk.core.security.jaas.DefaultLoginModule;
import org.etk.core.security.jaas.RolePrincipal;
import org.etk.core.security.jaas.UserPrincipal;

/**
 * Created by The eXo Platform SAS .
 * 
 * @author Gennady Azarenkov
 * @version $Id: TomcatLoginModule.java 23587 2008-11-21 15:41:44Z andrew00x $
 */

public class TomcatLoginModule extends DefaultLoginModule {

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean commit() throws LoginException {

    if (super.commit()) {

      Set<Principal> principals = subject.getPrincipals();

      for (String role : identity.getRoles())
        principals.add(new RolePrincipal(role));

      // username principal
      principals.add(new UserPrincipal(identity.getUserId()));

      return true;
    } else {
      return false;
    }
  }

}
