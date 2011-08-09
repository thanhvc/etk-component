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

import java.util.ArrayList;
import java.util.Collection;

import junit.framework.TestCase;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          thanhvucong.78@google.com
 * Aug 6, 2011  
 */
public class IdentityTest extends TestCase {

  Identity identity = null;
  Collection<MembershipEntry> memberships = null;
  
  
  public void setUp() {
    memberships = new ArrayList<MembershipEntry>();
    
    memberships.add(new MembershipEntry("/group1", "*"));
    memberships.add(new MembershipEntry("/group2" , "member"));
    memberships.add(new MembershipEntry("/group2" , "leader"));
    
    identity = new Identity("user", memberships);
  }
  
  
  public void testIsGroup() throws Exception {
    assertTrue("user in the group: /group", identity.isMemberOf("/group1"));
    assertTrue("user in group /group2", identity.isMemberOf("/group2"));
    assertFalse("user not in group /non/existing/group", identity.isMemberOf("/non/existing/group", "*"));
  }
  
  public void testHasMembership() throws Exception {
    assertTrue("membership * in group /group1", identity.isMemberOf("/group1", "*"));
    assertTrue("membership member in group /group2", identity.isMemberOf("/group2", "member"));
    assertTrue("membership member in group /group2", identity.isMemberOf("/group2", "leader"));
    assertTrue("membership * in group /group2", identity.isMemberOf("/group2", "*"));
    //is not belong to the group
    assertFalse("membership null in group group1", identity.isMemberOf("group1", null));
    assertFalse("membership null in group group1", identity.isMemberOf("group1"));
  }
  
}
