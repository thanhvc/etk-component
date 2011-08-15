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

package org.etk.core.security;

import java.util.Map;

/**
 * Created by The eXo Platform SAS .
 * 
 * @author Gennady Azarenkov
 * @version $Id:$
 */

public class PasswordCredential implements Credential {

  /**
   * Serial version UID.
   */
  private static final long serialVersionUID = -5754701608445078686L;

  /**
   * Password.
   */
  private String password;

  /**
   * Digest Authorization Request Context. Here we're going to keep some
   * information passed through request: qop, nonce, cnonce, algorithm, ns, etc.
   * All context is defined in <a
   * href=http://www.apps.ietf.org/rfc/rfc2617.html#sec-3.2.2>RFC-2617</a>.
   */
  private Map<String, String> passwordContext = null;

  /**
   * Create new PasswordCredential.
   * 
   * @param password password
   */
  public PasswordCredential(String password) {
    this.password = password;
  }

  /**
   * Create new PasswordCredential.
   * 
   * @param password password
   * @param passwordContext password context passed through Digest Authorization
   *          request
   */
  public PasswordCredential(String password, Map<String, String> passwordContext) {
    this.password = password;
    this.passwordContext = passwordContext;
  }

  /**
   * @return password context
   */
  public Map<String, String> getPasswordContext() {
    return this.passwordContext;
  }

  /**
   * @return password
   */
  public String getPassword() {
    return this.password;
  }
}
