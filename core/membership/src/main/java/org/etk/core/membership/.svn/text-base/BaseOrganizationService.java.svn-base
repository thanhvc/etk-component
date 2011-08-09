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
package org.exoplatform.services.organization;

import java.util.ArrayList;
import java.util.List;

import org.picocontainer.Startable;

import org.exoplatform.container.component.ComponentPlugin;

/**
 * Created by The eXo Platform SAS Author : Tuan Nguyen
 * tuan08@users.sourceforge.net Oct 13, 2005
 */
abstract public class BaseOrganizationService implements OrganizationService, Startable {
  protected UserHandler                          userDAO_;

  protected UserProfileHandler                   userProfileDAO_;

  protected GroupHandler                         groupDAO_;

  protected MembershipHandler                    membershipDAO_;

  protected MembershipTypeHandler                membershipTypeDAO_;

  protected List<OrganizationServiceInitializer> listeners_ = new ArrayList<OrganizationServiceInitializer>(3);

  public UserHandler getUserHandler() {
    return userDAO_;
  }

  public UserProfileHandler getUserProfileHandler() {
    return userProfileDAO_;
  }

  public GroupHandler getGroupHandler() {
    return groupDAO_;
  }

  public MembershipTypeHandler getMembershipTypeHandler() {
    return membershipTypeDAO_;
  }

  public MembershipHandler getMembershipHandler() {
    return membershipDAO_;
  }

  public void start() {
    for (OrganizationServiceInitializer listener : listeners_) {
      try {
        listener.init(this);
      } catch (Exception ex) {
        String msg = "Failed start Organization Service " + getClass().getName()
            + ", probably because of configuration error. Error occurs when initialize "
            + listener.getClass().getName();
        throw new RuntimeException(msg, ex);
      }
    }
  }

  public void stop() {
  }

  synchronized public void addListenerPlugin(ComponentPlugin listener) throws Exception {
    if (listener instanceof UserEventListener) {
      userDAO_.addUserEventListener((UserEventListener) listener);
    } else if (listener instanceof GroupEventListener) {
      groupDAO_.addGroupEventListener((GroupEventListener) listener);
    } else if (listener instanceof MembershipEventListener) {
      membershipDAO_.addMembershipEventListener((MembershipEventListener) listener);
    } else if (listener instanceof UserProfileEventListener) {
      userProfileDAO_.addUserProfileEventListener((UserProfileEventListener) listener);
    } else if (listener instanceof OrganizationServiceInitializer) {
      listeners_.add((OrganizationServiceInitializer) listener);
    } else {
      throw new RuntimeException(listener.getClass().getName() + " is an unknown listener type");
    }
  }
}
