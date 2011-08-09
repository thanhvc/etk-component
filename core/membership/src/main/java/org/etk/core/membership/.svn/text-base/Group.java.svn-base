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
package org.exoplatform.services.organization;

/**
 * Created by The eXo Platform SAS Author : Mestrallet Benjamin
 * benjmestrallet@users.sourceforge.net Date: Aug 21, 2003 Time: 3:22:54 PM This
 * is the interface for the group data model. Note that after each set method is
 * called. The developer need to call @see GroupHandler.saveGroup(..) to persist
 * the change
 */
public interface Group {
  /**
   * @return the id of the group. The id should have the form
   *         /ancestor/parent/groupname
   */
  public String getId();

  /**
   * @return the id of the parent group. if the parent id is null , it mean that
   *         the group is at the first level. the child of root group.
   */
  public String getParentId();

  /**
   * @return the local name of the group
   */
  public String getGroupName();

  /**
   * @param name the local name for the group TODO This method should be called
   *          once only and should be set in the
   *          GroupHandler.createGroupInstance() method
   */
  public void setGroupName(String name);

  /**
   * @return The display label of the group.
   */
  public String getLabel();

  /**
   * @param name The new label of the group
   */
  public void setLabel(String name);

  /**
   * @return The group description
   */
  public String getDescription();

  /**
   * @param desc The new description of the group
   */
  public void setDescription(String desc);
}
