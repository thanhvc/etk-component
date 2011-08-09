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

package org.exoplatform.services.security;

import javax.security.auth.login.LoginException;

/**
 * Created by The eXo Platform SAS . Component responsible for user
 * authentication (session creation) In JAAS LoginModule typically called in
 * login() method
 * 
 * @author Gennady Azarenkov
 * @version $Id:$
 */

public interface Authenticator {
  /**
   * Authenticate user and return userId which can be different to username.
   * 
   * @param credentials - list of users credentials (such as name/password, X509
   *          certificate etc)
   * @return userId
   * @throws LoginException
   * @throws Exception
   */
  String validateUser(Credential[] credentials) throws LoginException, Exception;

  /**
   * @param credentials - userId.
   * @return Identity
   * @throws Exception
   */
  Identity createIdentity(String userId) throws Exception;

}
