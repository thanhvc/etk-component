/*
 * Copyright (C) 2009 eXo Platform SAS.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.etk.core.membership.impl.mock;

import org.etk.common.utils.ListAccess;
import org.etk.core.membership.User;

import java.util.ArrayList;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public class LazyListImpl extends ArrayList<User> implements ListAccess<User> {
  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  public User[] load(int index, int length) throws Exception, IllegalArgumentException {
    User[] users = new User[length];
    toArray(users);
    return users;
  }

  public int getSize() throws Exception {
    return size();
  }
}
