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
package org.etk.core.membership;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.naming.InvalidNameException;

import org.etk.common.utils.PageList;
import org.etk.core.membership.Group;
import org.etk.core.membership.GroupHandler;
import org.etk.core.membership.Membership;
import org.etk.core.membership.MembershipHandler;
import org.etk.core.membership.MembershipType;
import org.etk.core.membership.MembershipTypeHandler;
import org.etk.core.membership.OrganizationService;
import org.etk.core.membership.Query;
import org.etk.core.membership.User;
import org.etk.core.membership.UserHandler;
import org.etk.core.membership.UserProfile;
import org.etk.core.membership.UserProfileEventListener;
import org.etk.core.membership.UserProfileHandler;
import org.etk.kernel.container.ApplicationContainer;
import org.etk.kernel.test.BasicTestCase;

public class TestOrganizationService extends BasicTestCase {

  static String Group1 = "Group1";

  static String Group2 = "Group2";

  static String Group3 = "Group3";

  static String Benj = "Benj";

  static String Tuan = "Tuan";

  OrganizationService service_;

  UserHandler userHandler_;

  UserProfileHandler profileHandler_;

  GroupHandler groupHandler_;

  MembershipTypeHandler mtHandler_;

  MembershipHandler membershipHandler_;

  boolean runtest = true;

  private static final String USER = "test";

  private static final List<String> USERS;

  private static final int USERS_LIST_SIZE = 15;

  static {
    USERS = new ArrayList<String>(USERS_LIST_SIZE);
    for (int i = 0; i < USERS_LIST_SIZE; i++)
      USERS.add(USER + "_" + i);
  }

  public TestOrganizationService(String s) {
    super(s);
  }

  public void setUp() throws Exception {
    if (!runtest)
      return;
    ApplicationContainer manager = ApplicationContainer.getInstance();
    service_ = (OrganizationService) manager.getComponentInstanceOfType(OrganizationService.class);
    userHandler_ = service_.getUserHandler();
    profileHandler_ = service_.getUserProfileHandler();
    groupHandler_ = service_.getGroupHandler();
    mtHandler_ = service_.getMembershipTypeHandler();
    membershipHandler_ = service_.getMembershipHandler();
    
    Query query = new Query();
    query.setUserName(USER + "*");
    PageList users = userHandler_.findUsers(query);
    List<User> allUsers = users.getAll();
  }

  public void tearDown() throws Exception {
    Query query = new Query();
    query.setUserName(USER + "*");
    PageList users = userHandler_.findUsers(query);

    List<User> allUsers = users.getAll();

    for (int i = allUsers.size() - 1; i >= 0; i--) {
      String userName = allUsers.get(i).getUserName();
      userHandler_.removeUser(userName, true);
    }

  }

  public void testUserPageSize() throws Exception {
    for (String name : USERS)
      createUser(name);

    Query query = new Query();
    PageList users = userHandler_.findUsers(query);
    // newly created plus one 'demo' from configuration
    assertEquals(USERS_LIST_SIZE + 1, users.getAll().size());
    assertEquals(1, users.getAvailablePage());
    for (Object o : users.getPage(1)) {
      User u = (User) o;
      if (!u.getUserName().equals("demo"))
        assertTrue(USERS.contains(u.getUserName()));
    }
  }

  public void testUser() throws Exception {
    createUser(USER);
    User user = userHandler_.findUserByName(USER);
    assertTrue("Found user instance ", user != null);
    assertEquals("Expect user name is: ", USER, user.getUserName());
    UserProfile userProfile = profileHandler_.findUserProfileByName(USER);
    profileHandler_.removeUserProfile(USER, true);
    assertNull(profileHandler_.findUserProfileByName(USER));

    profileHandler_.createUserProfileInstance(USER);
    userProfile.getUserInfoMap().put("key", "value");
    profileHandler_.saveUserProfile(userProfile, true);
    userProfile = profileHandler_.findUserProfileByName(USER);
    assertTrue("Expect user profile is found: ", userProfile != null);
    assertEquals(userProfile.getUserInfoMap().get("key"), "value");

    PageList users = userHandler_.findUsers(new Query());
    assertTrue("Expect 1 user found ", users.getAvailable() >= 1);

    /* Update user's information */
    user.setFirstName("Exo(Update)");
    userHandler_.saveUser(user, false);
    userProfile.getUserInfoMap().put("user.gender", "male");
    profileHandler_.saveUserProfile(userProfile, true);
    userProfile = profileHandler_.findUserProfileByName(USER);

    assertEquals("expect first name is", "Exo(Update)", user.getFirstName());
    assertEquals("Expect profile is updated: user.gender is ",
                 "male",
                 userProfile.getUserInfoMap().get("user.gender"));

    PageList piterator = userHandler_.getUserPageList(10);
    // newly created 'test' and 'demo'
    assertEquals(2, piterator.currentPage().size());

    userHandler_.removeUser(USER, true);
    piterator = userHandler_.getUserPageList(10);
    // one 'demo'
    assertEquals(1, piterator.currentPage().size());
    assertNull("User: USER is removed: ", userHandler_.findUserByName(USER));
    assertNull(" user's profile of USER was removed:", profileHandler_.findUserProfileByName(USER));
  }

  public void testGroup() throws Exception {
    /* Create a parent group with name is: GroupParent */
    String parentName = "GroupParent";
    Group groupParent = groupHandler_.createGroupInstance();
    groupParent.setGroupName(parentName);
    groupParent.setDescription("This is description");
    groupHandler_.addChild(null, groupParent, true);
    groupParent = groupHandler_.findGroupById(groupParent.getId());
    assertEquals("GroupParent", groupParent.getGroupName());
    /* Create a child group with name: Group1 */
    Group groupChild1 = groupHandler_.createGroupInstance();
    groupChild1.setGroupName(Group1);
    groupHandler_.addChild(groupParent, groupChild1, true);
    groupChild1 = groupHandler_.findGroupById(groupChild1.getId());
    assertEquals(groupChild1.getParentId(), groupParent.getId());
    assertEquals("Expect group child's name is: ", Group1, groupChild1.getGroupName());
    /* Update groupChild's information */
    groupChild1.setLabel("GroupRenamed");
    groupChild1.setDescription("new description ");
    groupHandler_.saveGroup(groupChild1, true);
    assertEquals(groupHandler_.findGroupById(groupChild1.getId()).getLabel(), "GroupRenamed");
    /* Create a group child with name is: Group2 */
    Group groupChild2 = groupHandler_.createGroupInstance();
    groupChild2.setGroupName(Group2);
    groupHandler_.addChild(groupParent, groupChild2, true);
    groupChild2 = groupHandler_.findGroupById(groupChild2.getId());
    assertEquals(groupChild2.getParentId(), groupParent.getId());
    assertEquals("Expect group child's name is: ", Group2, groupChild2.getGroupName());
    /*
     * find all child group in groupParent Expect result: 2 child group: group1,
     * group2
     */
    assertEquals("Expect number of child group in parent group is: ",
                 2,
                 groupHandler_.findGroups(groupParent).size());
    /* Remove a child group */
    groupHandler_.removeGroup(groupHandler_.findGroupById(groupChild1.getId()), true);
    assertNull("Expect child group has been removed: ",
               groupHandler_.findGroupById(groupChild1.getId()));
    assertEquals("Expect only 1 child group in parent group",
                 1,
                 groupHandler_.findGroups(groupParent).size());
    /* Remove Parent group, all it's group child will be removed */
    groupHandler_.removeGroup(groupParent, true);
    assertEquals("Expect ParentGroup is removed:",
                 null,
                 groupHandler_.findGroupById(groupParent.getId()));
    assertEquals("Expect all child group is removed: ", 0, groupHandler_.findGroups(groupParent)
                                                                        .size());
  }

  public void testMembershipType() throws Exception {
    /* Create a membershipType */
    String testType = "testType";
    MembershipType mt = mtHandler_.createMembershipTypeInstance();
    mt.setName(testType);
    mt.setDescription("This is a test");
    mt.setOwner("exo");
    mtHandler_.createMembershipType(mt, true);
    assertEquals("Expect mebershiptype is:", testType, mtHandler_.findMembershipType(testType)
                                                                 .getName());

    /* Update MembershipType's information */
    String desc = "This is a test (update)";
    mt.setDescription(desc);
    mtHandler_.saveMembershipType(mt, true);
    assertEquals("Expect membershiptype's description",
                 desc,
                 mtHandler_.findMembershipType(testType).getDescription());

    /* create another membershipType */
    mt = mtHandler_.createMembershipTypeInstance();
    mt.setName("anothertype");
    mt.setOwner("exo");
    mtHandler_.createMembershipType(mt, true);

    /*
     * find all membership type Expect result: 3 membershipType:
     * "testmembership", "anothertype" and "member"(default membership type, it
     * is created at startup time)
     */
    assertEquals("Expect 3 membership in collection: ", 3, mtHandler_.findMembershipTypes().size());

    /* remove "testmembership" */
    mtHandler_.removeMembershipType(testType, true);
    assertEquals("Membership type has been removed:", null, mtHandler_.findMembershipType(testType));
    assertEquals("Expect 2 membership in collection(1 is default): ",
                 2,
                 mtHandler_.findMembershipTypes().size());

    /* remove "anothertype" */
    mtHandler_.removeMembershipType("anothertype", true);
    assertEquals("Membership type has been removed:",
                 null,
                 mtHandler_.findMembershipType("anothertype"));
    assertEquals("Expect 1 membership in collection(default type): ",
                 1,
                 mtHandler_.findMembershipTypes().size());
    /* All membershipType was removed(except default membership) */
  }

  public void testMembership() throws Exception {
    /* Create 2 user: benj and tuan */
    User userBenj = createUser(Benj);
    User userTuan = createUser(Tuan);

    /* Create "Group1" */
    Group group1 = groupHandler_.createGroupInstance();
    group1.setGroupName(Group1);
    groupHandler_.addChild(null, group1, true);
    /* Create "Group2" */
    Group group2 = groupHandler_.createGroupInstance();
    group2.setGroupName(Group2);
    groupHandler_.addChild(null, group2, true);

    /* Create membership1 and assign Benj to "Group1" with this membership */
    MembershipType mt = mtHandler_.createMembershipTypeInstance();
    mt.setName("testmembership");
    mtHandler_.createMembershipType(mt, true);

    membershipHandler_.linkMembership(userBenj, group1, mt, true);
    membershipHandler_.linkMembership(userBenj, group2, mt, true);
    membershipHandler_.linkMembership(userTuan, group2, mt, true);

    mt = mtHandler_.createMembershipTypeInstance();
    mt.setName("membershipType2");
    mtHandler_.createMembershipType(mt, true);
    membershipHandler_.linkMembership(userBenj, group2, mt, true);

    mt = mtHandler_.createMembershipTypeInstance();
    mt.setName("membershipType3");
    try {
      membershipHandler_.linkMembership(userBenj, group2, mt, true);
      fail("Exception should be thrown");
    } catch (InvalidNameException e) {
    }

    mt = mtHandler_.createMembershipTypeInstance();
    mt.setName("membershipType3");
    mtHandler_.createMembershipType(mt, true);
    membershipHandler_.linkMembership(userBenj, group2, mt, true);

    /*
     * find all memberships in group2 Expect result: 4 membership: 3 for
     * Benj(testmebership, membershipType2, membershipType3) : 1 for
     * Tuan(testmembership)
     */
    assertEquals("Expect number of membership in group 2 is: ",
                 4,
                 membershipHandler_.findMembershipsByGroup(group2).size());

    /*
     * find all memberships in "Group2" relate with Benj Expect result: 3
     * membership
     */
    assertEquals("Expect number of membership in " + Group2 + " relate with benj is: ",
                 3,
                 membershipHandler_.findMembershipsByUserAndGroup(Benj, group2.getId()).size());

    /*
     * find all memberships of Benj in all group Expect result: 5 membership: 3
     * memberships in "Group2", 1 membership in "Users" (default) : 1 membership
     * in "group1"
     */
    assertEquals("expect membership is: ", 5, membershipHandler_.findMembershipsByUser(Benj).size());

    /*
     * find memberships of Benj in "Group2" with membership type: testType
     * Expect result: 1 membership with membershipType is "testType"
     * (testmembership)
     */
    Membership membership = membershipHandler_.findMembershipByUserGroupAndType(Benj,
                                                                                group2.getId(),
                                                                                "testmembership");
    assertNotNull("Expect membership is found:", membership);
    assertEquals("Expect membership type is: ", "testmembership", membership.getMembershipType());
    assertEquals("Expect groupId of this membership is: ", group2.getId(), membership.getGroupId());
    assertEquals("Expect user of this membership is: ", Benj, membership.getUserName());

    /*
     * find all groups of Benj Expect result: 3 group: "Group1", "Group2" and
     * "user" ("user" is default group)
     */
    assertEquals("expect group is: ", 3, groupHandler_.findGroupsOfUser(Benj).size());

    /*
     * find all groups has membership type "TYPE" relate with Benj expect
     * result: 2 group: "Group1" and "Group2"
     */
    assertEquals("expect group is: ",
                 2,
                 groupHandler_.findGroupByMembership(Benj, "testmembership").size());

    /* remove a membership */
    String memId = membershipHandler_.findMembershipByUserGroupAndType(Benj,
                                                                       group2.getId(),
                                                                       "membershipType3").getId();
    membershipHandler_.removeMembership(memId, true);
    assertNull("Membership was removed: ",
               membershipHandler_.findMembershipByUserGroupAndType(Benj,
                                                                   "/" + Group2,
                                                                   "membershipType3"));

    /*
     * remove a user Expect result: all membership related with user will be
     * remove
     */
    userHandler_.removeUser(Tuan, true);
    assertNull("This user was removed", userHandler_.findUserByName(Tuan));

    assertTrue("All membership related with this user was removed: ",
               membershipHandler_.findMembershipsByUser(Tuan).isEmpty());

    /*
     * Remove a group Expect result: all membership associate with this group
     * will be removed
     */
    groupHandler_.removeGroup(group1, true);
    assertNull("This group was removed ", groupHandler_.findGroupById(group1.getId()));
    assertTrue(membershipHandler_.findMembershipsByGroup(group1).isEmpty());

    /*
     * Remove a MembershipType Expect result: All membership have this type will
     * be removed
     */
    mtHandler_.removeMembershipType("testmembership", true);
    assertNull("This membershipType was removed: ", mtHandler_.findMembershipType("testmembership"));
    /*
     * Check all memberships associate with all groups to guarantee that no
     * membership associate with removed membershipType
     */
    for (Object o : groupHandler_.findGroups(null)) {
      Group g = (Group) o;
      for (Object o1 : membershipHandler_.findMembershipsByGroup(g)) {
        Membership m = (Membership) o1;
        assertFalse("MembershipType of this membership is not: \"testmembership\"",
                    m.getMembershipType().equalsIgnoreCase("testmembership"));
      }
    }
  }

  public void testRemoveMembershipByUser() throws Exception {
    String Benj = "B";
    String Tuan = "T";
    User userBenj = createUser(Benj);
    User userTuan = createUser(Tuan);

    String Group1 = "G1";
    String Group2 = "G2";
    String Group3 = "G3";
    Group group1 = groupHandler_.createGroupInstance();
    group1.setGroupName(Group1);
    groupHandler_.addChild(null, group1, true);
    Group group2 = groupHandler_.createGroupInstance();
    group2.setGroupName(Group2);
    groupHandler_.addChild(null, group2, true);
    Group group3 = groupHandler_.createGroupInstance();
    group3.setGroupName(Group3);
    groupHandler_.addChild(null, group3, true);

    MembershipType mt = mtHandler_.createMembershipTypeInstance();
    mt.setName("testmembership_");
    mtHandler_.createMembershipType(mt, true);

    membershipHandler_.linkMembership(userBenj, group1, mt, true);
    membershipHandler_.linkMembership(userBenj, group2, mt, true);
    membershipHandler_.linkMembership(userBenj, group3, mt, true);
    membershipHandler_.linkMembership(userTuan, group1, mt, true);

    assertEquals(membershipHandler_.removeMembershipByUser(Tuan, true).size(), 2);
    assertEquals(membershipHandler_.removeMembershipByUser(Benj, true).size(), 4);

    mtHandler_.removeMembershipType("testmembership_", true);
    userHandler_.removeUser(Tuan, true);
    userHandler_.removeUser(Benj, true);
    groupHandler_.removeGroup(group1, true);
    groupHandler_.removeGroup(group2, true);
    groupHandler_.removeGroup(group3, true);
  }

  public void testUserProfileListener() throws Exception {
    UserProfileListener l = new UserProfileListener();
    profileHandler_.addUserProfileEventListener(l);
    User user = createUser(USER);
    assertNotNull(user);
    UserProfile profile = profileHandler_.createUserProfileInstance(user.getUserName());
    profileHandler_.saveUserProfile(profile, true);
    assertTrue(l.preSave && l.postSave);
    profileHandler_.removeUserProfile(user.getUserName(), true);
    assertTrue(l.preDelete && l.postDelete);
  }

  private static class UserProfileListener extends UserProfileEventListener {

    boolean preSave;

    boolean postSave;

    boolean preDelete;

    boolean postDelete;

    @Override
    public void postDelete(UserProfile profile) throws Exception {
      assertEquals(USER, profile.getUserName());
      postDelete = true;
    }

    @Override
    public void postSave(UserProfile profile, boolean isNew) throws Exception {
      assertEquals(USER, profile.getUserName());
      postSave = true;
    }

    @Override
    public void preDelete(UserProfile profile) throws Exception {
      assertEquals(USER, profile.getUserName());
      preDelete = true;
    }

    @Override
    public void preSave(UserProfile profile, boolean isNew) throws Exception {
      assertEquals(USER, profile.getUserName());
      preSave = true;
    }

  }

  public User createUser(String userName) throws Exception {
    User user = userHandler_.createUserInstance(userName);
    user.setPassword("default");
    user.setFirstName("default");
    user.setLastName("default");
    user.setEmail("exo@exoportal.org");
    userHandler_.createUser(user, true);
    return user;
  }

  public void testSearchWithSpecialCharacter() throws Exception {
    // create user
    User user = userHandler_.createUserInstance("TestName");
    user.setPassword("default");
    user.setFirstName("L'test");
    user.setLastName("default");
    user.setEmail("exo@exoportal.org");
    userHandler_.createUser(user, true);

    // search user
    Query query = new Query();
    query.setFirstName("L'test");
    PageList list = userHandler_.findUsers(query);
    assertEquals(1, list.getAll().size());
    assertEquals(1, list.getPage(1).size());
  }

  /**
   * Find users using query and check it count.
   */
  public void testFindUsers() throws Exception {

    Calendar calendar = Calendar.getInstance();
    calendar.set(2008, 1, 1);

    User u = userHandler_.createUserInstance("tolik");
    u.setEmail("email@test");
    u.setFirstName("first");
    u.setLastName("last");
    u.setPassword("pwd");
    userHandler_.createUser(u, true);

    try {
      Query query = new Query();

      query.setEmail("email@test");
      assertEquals(userHandler_.findUsers(query).getAll().size(), 1);
      query.setEmail(null);

      query.setUserName("*tolik*");
      assertEquals(userHandler_.findUsers(query).getAll().size(), 1);

      query.setUserName("tolik*");
      assertEquals(userHandler_.findUsers(query).getAll().size(), 1);

      query.setUserName("tolik");
      assertEquals(userHandler_.findUsers(query).getAll().size(), 1);

      query.setFirstName("First");
      query.setLastName("laSt");
      assertEquals(userHandler_.findUsers(query).getAll().size(), 1);
      query.setFirstName(null);
      query.setLastName(null);

      Calendar calc = Calendar.getInstance();
      calc.set(2007, 1, 1);
      query.setFromLoginDate(calc.getTime());
      query.setUserName("*tolik*");
      assertEquals(userHandler_.findUsers(query).getAll().size(), 1);

      calc.set(2050, 1, 1);
      query.setFromLoginDate(calc.getTime());
      assertEquals(userHandler_.findUsers(query).getAll().size(), 0);
      query.setFromLoginDate(null);

      calc.set(2007, 1, 1);
      query.setToLoginDate(calc.getTime());
      assertEquals(userHandler_.findUsers(query).getAll().size(), 0);

      calc.set(2050, 1, 1);
      query.setToLoginDate(calc.getTime());
      assertEquals(userHandler_.findUsers(query).getAll().size(), 1);
      query.setUserName(null);
      query.setToLoginDate(null);

    } catch (Exception e) {
      e.printStackTrace();
      fail("Exception should not be thrown.");
    } finally {
      userHandler_.removeUser("tolik", true);
    }
  }

}
