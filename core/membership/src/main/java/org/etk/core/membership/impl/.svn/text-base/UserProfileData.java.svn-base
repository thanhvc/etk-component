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
package org.exoplatform.services.organization.impl;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.XppDriver;

/**
 * Created by The eXo Platform SAS . Author : Tuan Nguyen
 * tuan08@users.sourceforge.net Date: Jun 14, 2003 Time: 1:12:22 PM
 * 
 * @hibernate.class table="EXO_USER_PROFILE"
 */
public class UserProfileData {
  static transient private XStream xstream_;

  private String                   userName;

  private String                   profile;

  public UserProfileData() {
  }

  public UserProfileData(String userName) {
    StringBuffer b = new StringBuffer();
    b.append("<user-profile>\n").append("  <userName>").append(userName).append("</userName>\n");
    b.append("</user-profile>\n");
    this.userName = userName;
    this.profile = b.toString();
  }

  /**
   * @hibernate.id generator-class="assigned"
   **/
  public String getUserName() {
    return userName;
  }

  public void setUserName(String s) {
    this.userName = s;
  }

  /**
   * @hibernate.property length="65535"
   *                     type="org.exoplatform.services.database.impl.TextClobType"
   **/
  public String getProfile() {
    return profile;
  }

  public void setProfile(String s) {
    profile = s;
  }

  public org.exoplatform.services.organization.UserProfile getUserProfile() {
    XStream xstream = getXStream();
    UserProfileImpl up = (UserProfileImpl) xstream.fromXML(profile);
    return up;
  }

  public void setUserProfile(org.exoplatform.services.organization.UserProfile up) {
    if (up == null) {
      profile = "";
      return;
    }
    UserProfileImpl impl = (UserProfileImpl) up;
    userName = up.getUserName();
    XStream xstream = getXStream();
    profile = xstream.toXML(impl);
  }

  static private XStream getXStream() {
    if (xstream_ == null) {
      xstream_ = new XStream(new XppDriver());
      xstream_.alias("user-profile", UserProfileImpl.class);
    }
    return xstream_;
  }
}
