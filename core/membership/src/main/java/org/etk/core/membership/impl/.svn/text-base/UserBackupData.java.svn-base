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
package org.exoplatform.services.organization.impl;

import java.util.Collection;

/**
 * May 28, 2004
 * 
 * @author: Tuan Nguyen
 * @email: tuan08@users.sourceforge.net
 * @version: $ID$
 **/
public class UserBackupData {
  private UserImpl        user;

  private UserProfileImpl userProfile;

  private Collection      memberships;

  public UserBackupData(UserImpl u, UserProfileImpl up, Collection mbs) {
    user = u;
    userProfile = up;
    memberships = mbs;
  }

  public UserImpl getUser() {
    return user;
  }

  public UserProfileImpl getUserProfile() {
    return userProfile;
  }

  public Collection getMemberships() {
    return memberships;
  }
}
