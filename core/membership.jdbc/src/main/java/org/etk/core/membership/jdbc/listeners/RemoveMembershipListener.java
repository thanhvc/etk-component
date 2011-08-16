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
import org.etk.component.database.DBObjectQuery;
import org.etk.core.membership.Group;
import org.etk.core.membership.Membership;
import org.etk.core.membership.MembershipHandler;
import org.etk.core.membership.MembershipType;
import org.etk.core.membership.OrganizationService;
import org.etk.core.membership.User;
import org.etk.core.membership.jdbc.MembershipDAOImpl;
import org.etk.core.membership.jdbc.MembershipImpl;

import java.util.List;

/**
 * Created by The eXo Platform SAS Author : Le Bien Thuy thuy.le@exoplatform.com
 * Jun 28, 2007
 */
public class RemoveMembershipListener extends Listener<Object, Object>
{
   private OrganizationService service_;

   protected static Logger log = Logger.getLogger(RemoveMembershipListener.class);

   public RemoveMembershipListener(OrganizationService service)
   {
      service_ = service;
   }

   @SuppressWarnings("unchecked")
   public void onEvent(Event<Object, Object> event) throws Exception
   {

      Object target = event.getData();
      MembershipHandler membershipHanler = service_.getMembershipHandler();
      if (target instanceof User)
      {
         User user = (User)target;
         log.info("Remove all Membership by User: " + user.getUserName());
         membershipHanler.removeMembershipByUser(user.getUserName(), true);
      }
      else if (target instanceof Group)
      {
         Group group = (Group)target;
         log.info("Remove all Membership by Group: " + group.getGroupName());
         List<Membership> members = (List<Membership>)membershipHanler.findMembershipsByGroup(group);
         for (Membership member : members)
         {
            membershipHanler.removeMembership(member.getId(), true);
         }
      }
      else if (target instanceof MembershipType)
      {
         try
         {
            MembershipType memberType = (MembershipType)target;
            MembershipDAOImpl mtHandler = (MembershipDAOImpl)service_.getMembershipHandler();
            DBObjectQuery<MembershipImpl> query = new DBObjectQuery<MembershipImpl>(MembershipImpl.class);
            query.addLIKE("MEMBERSHIP_TYPE", memberType.getName());
            mtHandler.removeMemberships(query, true);
         }
         catch (Exception e)
         {
            log.error("Error while removing a Membership", e);
         }
      }
   }
}
