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
package org.etk.kernel.container;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class SessionManagerImpl extends Hashtable<String, SessionContainer> implements SessionManager {

  public List<SessionContainer> getLiveSessions() {
    List<SessionContainer> list = new ArrayList<SessionContainer>(size() + 1);
    list.addAll(values());
    return list;
  }

  final public SessionContainer getSessionContainer(String id) {
    return get(id);
  }

  final public void removeSessionContainer(String id) {
    remove(id);
  }

  final public void addSessionContainer(SessionContainer scontainer) {
    put(scontainer.getSessionId(), scontainer);
  }
}
