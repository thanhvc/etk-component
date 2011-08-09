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

package org.exoplatform.services.security;

/**
 * Created by The eXo Platform SAS .<br/>
 * 
 * @author Gennady Azarenkov
 * @version $Id:$
 */

public final class MembershipEntry {

  public final static String ANY_TYPE = "*";

  private String             membershipType;

  private String             group;

  /**
   * Constructor with undefined membership type
   * 
   * @param group
   */
  public MembershipEntry(String group) {
    this(group, null);
  }

  /**
   * @param group
   * @param membershipType
   */
  public MembershipEntry(String group, String membershipType) {
    this.membershipType = membershipType != null ? membershipType : ANY_TYPE;
    if (group == null)
      throw new NullPointerException("Group is null");
    this.group = group;
  }

  /**
   * @return the real membership type or "*" if not defined
   */
  public String getMembershipType() {
    return membershipType;
  }

  /**
   * @return the group name
   */
  public String getGroup() {
    return group;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals(Object obj) {
    if (obj == null || !(obj instanceof MembershipEntry))
      return false;
    MembershipEntry me = (MembershipEntry) obj;
    if (membershipType.equals(ANY_TYPE) || me.membershipType.equals(ANY_TYPE))
      return this.group.equals(me.group);
    return this.group.equals(me.group) && this.membershipType.equals(me.membershipType);
  }

  public static MembershipEntry parse(String identityStr) {

    if (identityStr.indexOf(":") != -1) {
      String membershipName = identityStr.substring(0, identityStr.indexOf(":"));
      String groupName = identityStr.substring(identityStr.indexOf(":") + 1);
      return new MembershipEntry(groupName, membershipName);
    }

    return null;

  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return getMembershipType() + ":" + getGroup();
  }
}
