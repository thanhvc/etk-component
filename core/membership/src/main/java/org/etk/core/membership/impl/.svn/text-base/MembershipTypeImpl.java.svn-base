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

import java.util.Date;

import org.exoplatform.services.organization.MembershipType;

/**
 * Created by The eXo Platform SAS . Author : Tuan Nguyen
 * tuan08@users.sourceforge.net Date: Jun 14, 2003 Time: 1:12:22 PM
 * 
 * @hibernate.class table="EXO_MEMBERSHIP_TYPE"
 */
public class MembershipTypeImpl implements MembershipType {

  private String name;

  private String description;

  private String owner;

  private Date   createdDate;

  private Date   modifiedDate;

  public MembershipTypeImpl() {
  }

  public MembershipTypeImpl(String name, String owner, String desc) {
    this.name = name;
    this.owner = owner;
    this.description = desc;
  }

  /**
   * @hibernate.id generator-class="assigned"
   **/
  public String getName() {
    return name;
  }

  public void setName(String s) {
    name = s;
  }

  /**
   * @hibernate.property
   **/
  public String getDescription() {
    return description;
  }

  public void setDescription(String s) {
    description = s;
  }

  /**
   * @hibernate.property
   **/
  public String getOwner() {
    return owner;
  }

  public void setOwner(String s) {
    owner = s;
  }

  /**
   * @hibernate.property
   **/
  public Date getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(Date d) {
    createdDate = d;
  }

  /**
   * @hibernate.property
   **/
  public Date getModifiedDate() {
    return modifiedDate;
  }

  public void setModifiedDate(Date d) {
    modifiedDate = d;
  }
}
