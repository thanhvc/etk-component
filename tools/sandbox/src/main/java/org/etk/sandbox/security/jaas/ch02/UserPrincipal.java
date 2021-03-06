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
package org.etk.sandbox.security.jaas.ch02;

import java.io.Serializable;
import java.security.Principal;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          thanhvucong.78@google.com
 * Aug 9, 2011  
 */
public class UserPrincipal implements Principal, Serializable {

  private String name;
  
  public UserPrincipal(String name) {
    this.name = name;
    
  }
  
  @Override
  public String getName() {
    return name;
  }
  
  public boolean equals(Object obj) {
    if (!(obj instanceof UserPrincipal)) {
      return false;
    }

    UserPrincipal other = (UserPrincipal) obj;
    return getName().equals(other.getName());
  }

  public int hashCode() {
    return getName().hashCode();
  }

  public String toString() {
    return "(UserPrincipal: name=" + getName() + ")";
  }

}
