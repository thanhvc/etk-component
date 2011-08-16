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
package org.etk.core.membership.jdbc;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.etk.common.exception.UniqueObjectException;
import org.etk.common.logging.Logger;
import org.etk.component.base.event.ListenerService;
import org.etk.component.database.DBObjectMapper;
import org.etk.component.database.DBObjectQuery;
import org.etk.component.database.DBPageList;
import org.etk.component.database.ExoDatasource;
import org.etk.component.database.StandardSQLDAO;
import org.etk.core.membership.Group;
import org.etk.core.membership.GroupEventListener;
import org.etk.core.membership.GroupHandler;
import org.etk.core.membership.Membership;
import org.etk.core.membership.MembershipHandler;
import org.etk.core.membership.OrganizationService;
import org.etk.kernel.container.ApplicationContainer;

/**
 * Created by The eXo Platform SAS Apr 7, 2007
 */
public class GroupDAOImpl extends StandardSQLDAO<GroupImpl> implements GroupHandler {

  protected static Logger log = Logger.getLogger(GroupDAOImpl.class);

  protected ListenerService listenerService_;

  public GroupDAOImpl(ListenerService lService,
                      ExoDatasource datasource,
                      DBObjectMapper<GroupImpl> mapper) {
    super(datasource, mapper, GroupImpl.class);
    listenerService_ = lService;
  }

  public Group createGroupInstance() {
    return new GroupImpl();
  }

  public void createGroup(Group group, boolean broadcast) throws Exception {
    addChild(null, group, broadcast);
  }

  public void addChild(Group parent, Group child, boolean broadcast) throws Exception {

    GroupImpl childImpl = (GroupImpl) child;
    String groupId = "/" + child.getGroupName();
    Connection connection = eXoDS_.getConnection();
    childImpl.setParentId("/");
    DBObjectQuery<GroupImpl> query = new DBObjectQuery<GroupImpl>(GroupImpl.class);
    if (parent != null) {
      query.addLIKE("GROUP_ID", parent.getId());
      Group parentGroup = super.loadUnique(connection, query.toQuery());
      groupId = parentGroup.getId() + "/" + child.getGroupName();
      childImpl.setParentId(parentGroup.getId());
    } else if (child.getId() != null) {
      groupId = child.getId();
      childImpl.setParentId("/");
    }

    query.getParameters().clear();
    query.addLIKE("GROUP_ID", groupId);
    Group o = super.loadUnique(connection, query.toQuery());
    if (o != null) {
      Object[] args = { child.getGroupName() };
      throw new UniqueObjectException("OrganizationService.unique-group-exception", args);
    }

    if (broadcast)
      listenerService_.broadcast("organization.group.preSave", this, childImpl);
    childImpl.setId(groupId);
    if (log.isDebugEnabled())
      log.debug("----------ADD GROUP " + child.getId() + " into Group" + child.getParentId());

    try {
      if (childImpl.getDBObjectId() == -1) {
        childImpl.setDBObjectId(eXoDS_.getIDGenerator().generateLongId(childImpl));
      }
      long id = childImpl.getDBObjectId();
      execute(connection, eXoDS_.getQueryBuilder().createInsertQuery(type_, id), childImpl);
      if (broadcast)
        listenerService_.broadcast("organization.group.postSave", this, childImpl);
    } catch (Exception e) {
      throw e;
    } finally {
      eXoDS_.closeConnection(connection);
    }
  }

  public Group findGroupById(String groupId) throws Exception {
    DBObjectQuery<GroupImpl> query = new DBObjectQuery<GroupImpl>(GroupImpl.class);
    query.addLIKE("GROUP_ID", groupId);
    Group g = super.loadUnique(query.toQuery());
    if (log.isDebugEnabled())
      log.debug("----------FIND GROUP BY ID: " + groupId + " _ " + (g != null));
    return g;
  }

  @SuppressWarnings("unchecked")
  public Collection findGroupByMembership(String userName, String membershipType) throws Exception {

    if (userName == null || membershipType == null)
      return null;
    MembershipHandler membershipHandler = getMembershipHandler();
    List<Membership> members = (List<Membership>) membershipHandler.findMembershipsByUser(userName);
    List<Group> groups = new ArrayList<Group>();
    for (Membership member : members) {
      if (!member.getMembershipType().equals(membershipType))
        continue;
      Group g = findGroupById(member.getGroupId());
      if (g != null)
        groups.add(g);
    }
    if (log.isDebugEnabled())
      log.debug("----------FIND GROUP BY USERNAME AND TYPE: " + userName + " - " + membershipType
          + " - ");
    return groups;
  }

  public Collection findGroups(Group parent) throws Exception {
    String parentId = "/";
    if (parent != null)
      parentId = parent.getId();
    DBObjectQuery<GroupImpl> query = new DBObjectQuery<GroupImpl>(GroupImpl.class);
    query.addLIKE("PARENT_ID", parentId);
    DBPageList<GroupImpl> pageList = new DBPageList<GroupImpl>(20, this, query);
    if (log.isDebugEnabled()) {
      log.debug("----------FIND GROUP BY PARENT: " + parent);
      log.debug(" Size = " + pageList.getAvailable());
    }
    return pageList.getAll();
  }

  @SuppressWarnings("unchecked")
  public Collection findGroupsOfUser(String user) throws Exception {
    MembershipHandler membershipHandler = getMembershipHandler();
    List<Membership> members = (List<Membership>) membershipHandler.findMembershipsByUser(user);
    List<Group> groups = new ArrayList<Group>();
    for (Membership member : members) {
      Group g = findGroupById(member.getGroupId());
      if (g != null && !hasGroup(groups, g))
        groups.add(g);
    }
    if (log.isDebugEnabled())
      log.debug("----------FIND GROUP BY USER: " + user + " - " + (groups != null));
    return groups;
  }

  private boolean hasGroup(List<Group> list, Group g) {
    for (Group ele : list) {
      if (ele.getId().endsWith(g.getId()))
        return true;
    }
    return false;
  }

  public Collection getAllGroups() throws Exception {
    DBObjectQuery<GroupImpl> query = new DBObjectQuery<GroupImpl>(GroupImpl.class);
    DBPageList<GroupImpl> pageList = new DBPageList<GroupImpl>(20, this, query);
    return pageList.getAll();
  }

  public void saveGroup(Group group, boolean broadcast) throws Exception {
    GroupImpl groupImpl = (GroupImpl) group;
    if (broadcast)
      listenerService_.broadcast(GroupHandler.PRE_UPDATE_GROUP_EVENT, this, groupImpl);
    super.update(groupImpl);
    if (broadcast)
      listenerService_.broadcast(GroupHandler.POST_UPDATE_GROUP_EVENT, this, groupImpl);
  }

  public Group removeGroup(Group group, boolean broadcast) throws Exception {
    GroupImpl groupImpl = (GroupImpl) group;
    if (broadcast)
      listenerService_.broadcast(GroupHandler.PRE_DELETE_GROUP_EVENT, this, groupImpl);
    super.remove(groupImpl);
    if (broadcast)
      listenerService_.broadcast(GroupHandler.POST_DELETE_GROUP_EVENT, this, groupImpl);
    return group;
  }

  @SuppressWarnings("unused")
  public void addGroupEventListener(GroupEventListener listener) {
  }

  private MembershipHandler getMembershipHandler() {
    ApplicationContainer manager = ApplicationContainer.getInstance();
    OrganizationService service = (OrganizationService) manager.getComponentInstanceOfType(OrganizationService.class);
    return service.getMembershipHandler();
  }

}
