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

import java.util.HashSet;
import java.util.Set;

import javax.security.auth.login.LoginException;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          thanhvucong.78@google.com
 * Aug 6, 2011  
 */
public class DummyAuthenticatorImpl implements Authenticator {

  private String[] acceptableUIDs = {"exo"};
  private RolesExtractor roleExtractor;
  
  public DummyAuthenticatorImpl(RolesExtractor roleExtractor) {
    this.roleExtractor = roleExtractor;
  }
  
  @Override
  public String validateUser(Credential[] credentials) throws LoginException, Exception {
    String myID = ((UsernameCredential) credentials[0]).getUsername();
    for (String id : this.acceptableUIDs) {
      if (id.equals(myID))
        return id;
    }

    throw new LoginException();
  }

  @Override
  public Identity createIdentity(String userId) throws Exception {
    Set<MembershipEntry> entries = new HashSet<MembershipEntry>();
    entries.add(new MembershipEntry(userId));
    return new Identity(userId, entries, roleExtractor.extractRoles(userId, entries));
  }
  

}
