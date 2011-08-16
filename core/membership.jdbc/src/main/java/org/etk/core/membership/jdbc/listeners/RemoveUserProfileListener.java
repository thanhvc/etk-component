/*
 * Copyright (C) 2009 eXo Platform SAS.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.etk.core.membership.jdbc.listeners;

import org.etk.common.logging.Logger;
import org.etk.component.base.event.Event;
import org.etk.component.base.event.Listener;
import org.etk.core.membership.OrganizationService;
import org.etk.core.membership.User;
import org.etk.core.membership.jdbc.UserDAOImpl;

/**
 * Created by The eXo Platform SAS Author : Le Bien Thuy lebienthuy@gmail.com
 * Jun 28, 2007
 */
public class RemoveUserProfileListener extends Listener<UserDAOImpl, User> {
  private OrganizationService service_;

  protected static Logger log = Logger.getLogger(RemoveUserProfileListener.class);

  public RemoveUserProfileListener(OrganizationService service) {
    service_ = service;
  }

  public void onEvent(Event<UserDAOImpl, User> event) throws Exception {
    log.info("Delete User Profile: " + event.getData().getUserName());
    service_.getUserProfileHandler().removeUserProfile(event.getData().getUserName(), true);
  }
}
