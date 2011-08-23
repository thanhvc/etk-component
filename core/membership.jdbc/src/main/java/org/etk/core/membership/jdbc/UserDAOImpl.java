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


import java.util.Calendar;
import java.util.List;

import org.etk.common.logging.Logger;
import org.etk.common.utils.LazyPageList;
import org.etk.common.utils.ListAccess;
import org.etk.component.base.event.ListenerService;
import org.etk.component.database.DBObjectMapper;
import org.etk.component.database.DBObjectQuery;
import org.etk.component.database.ExoDatasource;
import org.etk.component.database.StandardSQLDAO;
import org.etk.core.membership.ExtendedUserHandler;
import org.etk.core.membership.Group;
import org.etk.core.membership.GroupHandler;
import org.etk.core.membership.Membership;
import org.etk.core.membership.MembershipHandler;
import org.etk.core.membership.OrganizationService;
import org.etk.core.membership.Query;
import org.etk.core.membership.User;
import org.etk.core.membership.UserEventListener;
import org.etk.core.membership.UserHandler;
import org.etk.core.security.PasswordEncrypter;
import org.etk.kernel.container.ApplicationContainer;

/**
 * Created by The eXo Platform SAS Apr 7, 2007
 */
public class UserDAOImpl extends StandardSQLDAO<UserImpl> implements UserHandler,
    ExtendedUserHandler {

  protected static Logger   log = Logger.getLogger(UserDAOImpl.class);

  protected ListenerService listenerService_;

  public UserDAOImpl(ListenerService lService,
                     ExoDatasource datasource,
                     DBObjectMapper<UserImpl> mapper) {
    super(datasource, mapper, UserImpl.class);
    listenerService_ = lService;
  }

  public User createUserInstance() {
    return new UserImpl();
  }

  public User createUserInstance(String username) {
    return new UserImpl(username);
  }

  public void createUser(User user, boolean broadcast) throws Exception {
    if (log.isDebugEnabled())
      log.debug("----------- CREATE USER " + user.getUserName());
    UserImpl userImpl = (UserImpl) user;
    if (broadcast)
      listenerService_.broadcast(UserHandler.PRE_CREATE_USER_EVENT, this, userImpl);
    super.save(userImpl);
    if (broadcast)
      listenerService_.broadcast(UserHandler.POST_CREATE_USER_EVENT, this, userImpl);
  }

  public boolean authenticate(String username, String password) throws Exception {
    return authenticate(username, password, null);
  }

  public boolean authenticate(String username, String password, PasswordEncrypter pe) throws Exception {
    User user = findUserByName(username);
    if (user == null) {
      return false;
    }

    boolean authenticated;
    if (pe == null) {
      authenticated = user.getPassword().equals(password);
    } else {
      String encryptedPassword = new String(pe.encrypt(user.getPassword().getBytes()));
      authenticated = encryptedPassword.equals(password);
    }

    if (log.isDebugEnabled()) {
      log.debug("+++++++++++AUTHENTICATE USERNAME " + username + " AND PASS " + password + " - "
          + authenticated);
    }
    if (authenticated) {
      UserImpl userImpl = (UserImpl) user;
      userImpl.setLastLoginTime(Calendar.getInstance().getTime());
      saveUser(userImpl, false);
    }
    return authenticated;
  }

  public User findUserByName(String userName) throws Exception {
    DBObjectQuery<UserImpl> query = new DBObjectQuery<UserImpl>(UserImpl.class);
    query.addLIKE("USER_NAME", userName);
    User user = loadUnique(query.toQuery());
    ;
    if (log.isDebugEnabled())
      log.debug("+++++++++++FIND USER BY USER NAME " + userName + " - " + (user != null));
    return user;
  }

  public LazyPageList<User> findUsers(Query orgQuery) throws Exception {
    return new LazyPageList<User>(findUsersByQuery(orgQuery), 20);
  }

  /**
   * Query( name = "" , standardSQL = "..." oracleSQL = "..." )
   */
  public ListAccess<User> findUsersByQuery(Query orgQuery) throws Exception {
    DBObjectQuery dbQuery = new DBObjectQuery<UserImpl>(UserImpl.class);
    if (orgQuery.getUserName() != null) {
      dbQuery.addLIKE("UPPER(USER_NAME)", orgQuery.getUserName().toUpperCase());
    }
    if (orgQuery.getFirstName() != null) {
      dbQuery.addLIKE("UPPER(FIRST_NAME)", orgQuery.getFirstName().toUpperCase());
    }
    if (orgQuery.getLastName() != null) {
      dbQuery.addLIKE("UPPER(LAST_NAME)", orgQuery.getLastName().toUpperCase());
    }
    dbQuery.addLIKE("EMAIL", orgQuery.getEmail());
    dbQuery.addGT("LAST_LOGIN_TIME", orgQuery.getFromLoginDate());
    dbQuery.addLT("LAST_LOGIN_TIME", orgQuery.getToLoginDate());

    return new SimpleJDBCUserListAccess(this, dbQuery.toQuery(), dbQuery.toCountQuery());
  }

  public LazyPageList<User> findUsersByGroup(String groupId) throws Exception {
    return new LazyPageList<User>(findUsersByGroupId(groupId), 20);
  }

  public ListAccess<User> findUsersByGroupId(String groupId) throws Exception {
    if (log.isDebugEnabled())
      log.debug("+++++++++++FIND USER BY GROUP_ID " + groupId);
    ApplicationContainer manager = ApplicationContainer.getInstance();
    OrganizationService service = (OrganizationService) manager.getComponentInstanceOfType(OrganizationService.class);
    MembershipHandler membershipHandler = service.getMembershipHandler();
    GroupHandler groupHandler = service.getGroupHandler();
    Group group = groupHandler.findGroupById(groupId);
    @SuppressWarnings("unchecked")
    List<Membership> members = (List<Membership>) membershipHandler.findMembershipsByGroup(group);

    DBObjectQuery dbQuery = new DBObjectQuery<UserImpl>(UserImpl.class);
    for (Membership member : members) {
      dbQuery.addLIKE("USER_NAME", member.getUserName());
      /*
       * User g = findUserByName(member.getUserName()); if (g != null)
       * users.add(g);
       */
    }

    return new SimpleJDBCUserListAccess(this, dbQuery.toQueryUseOR(), dbQuery.toCountQueryUseOR());
  }

  public LazyPageList<User> getUserPageList(int pageSize) throws Exception {
    return new LazyPageList<User>(findAllUsers(), pageSize);
  }

  public ListAccess<User> findAllUsers() throws Exception {
    DBObjectQuery dbQuery = new DBObjectQuery<UserImpl>(UserImpl.class);
    return new SimpleJDBCUserListAccess(this, dbQuery.toQuery(), dbQuery.toCountQuery());
  }

  public User removeUser(String userName, boolean broadcast) throws Exception {
    UserImpl userImpl = (UserImpl) findUserByName(userName);
    if (userImpl == null)
      return null;
    if (broadcast)
      listenerService_.broadcast(UserHandler.PRE_DELETE_USER_EVENT, this, userImpl);
    super.remove(userImpl);
    if (broadcast)
      listenerService_.broadcast(UserHandler.POST_DELETE_USER_EVENT, this, userImpl);
    return userImpl;
  }

  public void saveUser(User user, boolean broadcast) throws Exception {
    UserImpl userImpl = (UserImpl) user;
    if (broadcast)
      listenerService_.broadcast(UserHandler.PRE_UPDATE_USER_EVENT, this, userImpl);
    super.update(userImpl);
    if (broadcast)
      listenerService_.broadcast(UserHandler.POST_UPDATE_USER_EVENT, this, userImpl);
  }

  @SuppressWarnings("unused")
  public void addUserEventListener(UserEventListener listener) {
  }
}
