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
package org.etk.sandbox.ws.model;


/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Sep 27, 2011  
 */
public class CommentRest {

  private String id = "";
  private String text = "";
  private IdentityRest posterIdentity;
  private long postedTime;
  private String createdAt = "";
  
  /**
   * Default constructor
   */
  public CommentRest(){
    
  }
  
  public String getId() {
    return this.id;
  }
  public void setId(String id) {
    this.id = id;
  }
  
  public IdentityRest getPosterIdentity(){
    return this.posterIdentity;
  }
  
  public void setPosterIdentity(IdentityRest posterIdentity) {
    this.posterIdentity = posterIdentity;
  }  
  
  public String getText() {
    return this.text;
  }
  public void setText(String text) {
    this.text = text;
  }
  
  public Long getPostedTime() {
    return this.postedTime;
  }
  public void setPostedTime(Long postedTime) {
    this.postedTime = postedTime;
  }
  
  public String getCreatedAt() {
    return this.createdAt;
  }
  public void setCreatedAt(String createdAt) {
    this.createdAt = createdAt;
  }
}
