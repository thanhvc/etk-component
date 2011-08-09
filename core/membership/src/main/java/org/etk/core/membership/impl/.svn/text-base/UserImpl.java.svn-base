package org.exoplatform.services.organization.impl;

import java.util.Date;

import org.exoplatform.services.organization.User;

/**
 * @hibernate.class table="EXO_USER"
 */
public class UserImpl implements User {

  private String           id             = null;

  private String           userName       = null;

  private transient String password       = null;

  private String           firstName      = null;

  private String           lastName       = null;

  private String           email          = null;

  private Date             createdDate;

  private Date             lastLoginTime;

  private String           organizationId = null;

  public UserImpl() {
  }

  public UserImpl(String username) {
    this.userName = username;
  }

  /**
   * @hibernate.id generator-class="assigned" unsaved-value="null"
   ***/
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  /**
   * @hibernate.property
   **/
  public String getUserName() {
    return userName;
  }

  public void setUserName(String name) {
    this.userName = name;
  }

  /**
   * @hibernate.property
   **/
  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  /**
   * @hibernate.property
   **/
  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  /**
   * @hibernate.property
   **/
  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  /**
   * @hibernate.property
   **/
  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  // wrapper method
  public String getFullName() {
    return getFirstName() + " " + getLastName();
  }

  public void setFullName(String fullName) {

  }

  /**
   * @hibernate.property
   **/
  public Date getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(Date t) {
    createdDate = t;
  }

  /**
   * @hibernate.property
   **/
  public Date getLastLoginTime() {
    return lastLoginTime;
  }

  public void setLastLoginTime(Date t) {
    lastLoginTime = t;
  }

  // toString
  public String toString() {
    return "User[" + id + "|" + userName + "]" + organizationId == null ? ""
                                                                       : ("@" + organizationId);
  }

  public String getOrganizationId() {
    return organizationId;
  }

  public void setOrganizationId(String organizationId) {
    this.organizationId = organizationId;
  }

}
