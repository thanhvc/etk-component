package org.etk.core.membership.impl;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.XppDriver;

import java.security.PrivilegedAction;

import org.etk.common.utils.SecurityHelper;
import org.etk.core.membership.UserProfile;

/**
 * Created by The eXo Platform SAS . Author : Tuan Nguyen
 * tuan08@users.sourceforge.net Date: Jun 14, 2003 Time: 1:12:22 PM
 * 
 * @hibernate.class table="EXO_USER_PROFILE"
 */
public class UserProfileData {
  static transient private XStream xstream_;

  private String userName;

  private String profile;

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
   * @hibernate.property length="65535" type="org.etk.component.database.impl.TextClobType"
   **/
  public String getProfile() {
    return profile;
  }

  public void setProfile(String s) {
    profile = s;
  }

  public UserProfile getUserProfile() {
    final XStream xstream = getXStream();
    UserProfileImpl up = SecurityHelper.doPrivilegedAction(new PrivilegedAction<UserProfileImpl>() {
      public UserProfileImpl run() {
        return (UserProfileImpl) xstream.fromXML(profile);
      }
    });
    return up;
  }

  public void setUserProfile(UserProfile up) {
    if (up == null) {
      profile = "";
      return;
    }
    final UserProfileImpl impl = (UserProfileImpl) up;
    userName = up.getUserName();
    final XStream xstream = getXStream();
    profile = SecurityHelper.doPrivilegedAction(new PrivilegedAction<String>() {
      public String run() {
        return xstream.toXML(impl);
      }
    });
  }

  static private XStream getXStream() {
    if (xstream_ == null) {
      xstream_ = SecurityHelper.doPrivilegedAction(new PrivilegedAction<XStream>() {
        public XStream run() {
          return new XStream(new XppDriver());
        }
      });
      xstream_.alias("user-profile", UserProfileImpl.class);
    }
    return xstream_;
  }
}
