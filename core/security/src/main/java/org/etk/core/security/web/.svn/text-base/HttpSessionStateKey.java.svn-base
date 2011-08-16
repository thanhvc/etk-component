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

import javax.servlet.http.HttpSession;

import org.exoplatform.services.security.StateKey;

/**
 * @author <a href="mailto:andrew00x@gmail.com">Andrey Parfonov</a>
 * @version $Id: $
 */
public final class HttpSessionStateKey implements StateKey {
  
  /**
   * HTTP session ID. 
   */
  private final String sessionId;
  
  public HttpSessionStateKey(HttpSession httpSession) {
    this.sessionId = httpSession.getId();
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals(Object obj) {
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    return sessionId.equals(((HttpSessionStateKey) obj).sessionId);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    return sessionId.hashCode();
  }
  
}
