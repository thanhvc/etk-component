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
public class ProfileRest {

  private String fullName = "";
  private String avatarUrl = "";
  
  /**
   * Default Constructor
   */
  public ProfileRest(){
  }
  
  /**
   * Sets fullName of profile
   * @param fullName
   * @since 1.2.2
   */
  public void setFullName(String fullName){
    this.fullName = fullName;
  }
  
  /**
   * Gets fullname of profile
   * @return
   * @since 1.2.2
   */
  public String getFullName(){
    return this.fullName;
  }
  
  /**
   * Sets avatarURL of profile
   * @param avatarUrl
   * @since 1.2.2
   */
  public void setAvatarUrl(String avatarUrl){
      this.avatarUrl = avatarUrl;
    
  }

  /**
   * Gets avatarURL of profile
   * @param avatarUrl
   * @since 1.2.2
   */
  public String getAvatarUrl(){
    return avatarUrl;
  }
}
