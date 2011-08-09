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

package org.exoplatform.services.security.impl;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import org.exoplatform.container.xml.InitParams;
import org.exoplatform.container.xml.ValueParam;
import org.exoplatform.services.security.MembershipEntry;
import org.exoplatform.services.security.RolesExtractor;

/**
 * Created by The eXo Platform SAS . Implements "default" roles extraction
 * algorythm applicable for current Organization service
 * 
 * @author Gennady Azarenkov
 * @version $Id:$
 */

public class DefaultRolesExtractorImpl implements RolesExtractor {

  protected String userRoleParentGroup = null;

  public DefaultRolesExtractorImpl(InitParams params) {
    if (params != null) {
      ValueParam param = params.getValueParam("user.role.parent.group");
      if (param != null && param.getValue().length() > 0) {
        userRoleParentGroup = param.getValue();
      }
    }

  }

  public DefaultRolesExtractorImpl() {
  }

  public void setUserRoleParentGroup(String userRoleParentGroup) {
    this.userRoleParentGroup = userRoleParentGroup;
  }

  /**
   * {@inheritDoc}
   */
  public Set<String> extractRoles(String userId, Set<MembershipEntry> memberships) {

    Set<String> roles = new HashSet<String>();
    for (MembershipEntry membership : memberships) {
      String[] splittedGroupName = StringUtils.split(membership.getGroup(), "/");

      if (userRoleParentGroup != null && splittedGroupName[0].equals(userRoleParentGroup)
          && splittedGroupName.length > 1) {
        roles.add(splittedGroupName[splittedGroupName.length - 1]);
      } else {
        roles.add(splittedGroupName[0]);
      }
    }
    return roles;
  }

}
