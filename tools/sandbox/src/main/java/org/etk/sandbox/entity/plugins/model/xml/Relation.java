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
package org.etk.sandbox.entity.plugins.model.xml;

import java.util.Collection;
import java.util.Iterator;

import javolution.util.FastList;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Aug 26, 2011  
 */
public class Relation {

  private String type = "";
  private String relationEntityName = "";
  Collection<KeyMap> keyMaps = FastList.newInstance();
  
  public String getType() {
    return type;
  }
  public void setType(String type) {
    this.type = type;
  }
  public String getRelationEntityName() {
    return relationEntityName;
  }
  public void setRelationEntityName(String relationEntityName) {
    this.relationEntityName = relationEntityName;
  }
  
  public void addKeyMap(Object object) {
    KeyMap keyMap = (KeyMap) object;
    keyMaps.add(keyMap);
  }
  
  public Iterator<KeyMap> getKeyMapIterator() {
    return keyMaps.iterator();
  }
  
  
}
