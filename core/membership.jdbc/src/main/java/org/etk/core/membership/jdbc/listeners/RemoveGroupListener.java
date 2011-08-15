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
import org.etk.core.membership.Group;
import org.etk.core.membership.GroupHandler;
import org.etk.core.membership.OrganizationService;
import org.etk.core.membership.jdbc.GroupDAOImpl;

import java.util.List;

/**
 * Created by The eXo Platform SAS Author : Le Bien Thuy lebienthuy@gmail.com
 * Jun 28, 2007
 */
public class RemoveGroupListener extends Listener<GroupDAOImpl, Group>
{

   protected static Logger log = Logger.getLogger(RemoveGroupListener.class);

   private OrganizationService service_;

   public RemoveGroupListener(OrganizationService service)
   {
      service_ = service;
   }

   @SuppressWarnings("unchecked")
   public void onEvent(Event<GroupDAOImpl, Group> event) throws Exception
   {
      log.info("Remove all Child of Group: " + event.getData().getId());
      GroupHandler membershipHanler = service_.getGroupHandler();
      List<Group> children = (List<Group>)membershipHanler.findGroups(event.getData());
      for (Group child : children)
      {
         membershipHanler.removeGroup(child, true);
      }
   }
}
