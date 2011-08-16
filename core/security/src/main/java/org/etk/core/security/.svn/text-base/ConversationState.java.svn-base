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

package org.exoplatform.services.security;

import java.util.HashMap;
import java.util.Set;

/**
 * Created by The eXo Platform SAS .
 * 
 * @author Gennady Azarenkov
 * @version $Id: $
 */

public class ConversationState {

  /**
   * "subject".
   */
  public static final String                    SUBJECT = "subject";

  /**
   * ThreadLocal keeper for ConversationState.
   */
  private static ThreadLocal<ConversationState> current = new ThreadLocal<ConversationState>();

  /**
   * See {@link Identity}.
   */
  private Identity                              identity;

  /**
   * Additions attributes of ConversationState.
   */
  private HashMap<String, Object>               attributes;

  public ConversationState(Identity identity) {
    this.identity = identity;
    this.attributes = new HashMap<String, Object>();
  }

  /**
   * @return current ConversationState or null if it was not preset
   */
  public static ConversationState getCurrent() {
    return current.get();
  }

  /**
   * Preset current ConversationState.
   * 
   * @param state ConversationState
   */
  public static void setCurrent(ConversationState state) {
    current.set(state);
  }

  /**
   * @return Identity
   */
  public Identity getIdentity() {
    return identity;
  }

  /**
   * sets attribute.
   * 
   * @param key
   * @param value
   */
  public void setAttribute(String name, Object value) {
    this.attributes.put(name, value);
  }

  /**
   * @param key
   * @return attribute
   */
  public Object getAttribute(String name) {
    return this.attributes.get(name);
  }

  /**
   * @return all attribute names
   */
  public Set<String> getAttributeNames() {
    return attributes.keySet();
  }

  /**
   * removes attribute.
   * 
   * @param key
   */
  public void removeAttribute(String name) {
    this.attributes.remove(name);
  }

}
