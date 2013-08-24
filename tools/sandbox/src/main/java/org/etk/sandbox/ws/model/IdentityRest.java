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
public class IdentityRest {

  private String id = "";
  private String remoteId = "";
  private String providerId = "";
  private ProfileRest profile = null;

  /**
   * Default constructor
   */
  public IdentityRest() {
  }
  
  /**
   * Gets Id of Identity
   * @since 1.2.2
   */
  public String getId() {
    return this.id;
  }
  
  /**
   * Sets Id of Identity
   * @param id
   */
  public void setId(String id) {
    this.id = id;
  }
  
  /**
   * Gets remoteId of Identity
   * @return
   * @since 1.2.2
   */
  public String getRemoteId() {
    return remoteId;
  }
  
  /**
   * Sets remoteId of Identity
   * @param remoteId
   * @since 1.2.2
   */
  public void setRemoteId(String remoteId) {
    this.remoteId = remoteId;
  }
  
  /**
   * Gets providerId of Identity
   * @param remoteId
   * @since 1.2.2
   */
  public String getProviderId() {
    return this.providerId;
  }
  
  /**
   * Sets providerId of Identity
   * @param remoteId
   * @since 1.2.2
   */  
  public void setProviderId(String providerId) {
    this.providerId = providerId;
  }
  
  /**
   * Sets profile of Identity
   * @param remoteId
   * @since 1.2.2
   */  
  public void setProfile(ProfileRest profile){
    this.profile = profile;
  }
  
  /**
   * Gets profile of Identity
   * @param remoteId
   * @since 1.2.2
   */
  public ProfileRest getProfile(){
    return this.profile;
  }
}
