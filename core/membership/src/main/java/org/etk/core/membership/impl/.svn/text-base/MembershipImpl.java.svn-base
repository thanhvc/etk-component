package org.exoplatform.services.organization.impl;

import org.exoplatform.services.organization.Membership;

/**
 * @hibernate.class table="EXO_MEMBERSHIP"
 */
public class MembershipImpl implements Membership {

  private String id             = null;

  private String membershipType = "member";

  private String userName       = null;

  private String groupId        = null;

  public MembershipImpl() {
  }

  /**
   * @hibernate.id generator-class="assigned" unsaved-value="null"
   ***/
  public String getId() {
    return id;
  }

  public void setId(String id) {
    // new Exception("MODIFY MEMBERSHIP ID , old id: " + this.id + " new id : "
    // +id).printStackTrace() ;
    this.id = id;
  }

  /**
   * @hibernate.property
   **/
  public String getMembershipType() {
    return membershipType;
  }

  public void setMembershipType(String type) {
    this.membershipType = type;
  }

  /**
   * @hibernate.property
   **/
  public String getUserName() {
    return userName;
  }

  public void setUserName(String user) {
    this.userName = user;
  }

  /**
   * @hibernate.property
   **/
  public String getGroupId() {
    return groupId;
  }

  public void setGroupId(String group) {
    this.groupId = group;
  }

  // toString
  public String toString() {
    return "Membership[" + id + "]";
  }
}
