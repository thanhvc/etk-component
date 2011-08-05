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
package org.etk.service.bar.api;

import org.etk.service.common.event.LifeCycleEvent;
import org.etk.service.foo.model.Bar;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Jul 21, 2011  
 */
public class BarLifeCycleEvent extends LifeCycleEvent<String, Bar> {

  public enum Type {
    BAR_CREATED, BAR_REMOVED, BAR_UPDATED
  };

  private Type type;
  
  public BarLifeCycleEvent(Bar bar, String target, Type eventType) {
    super(target, bar);
    this.type = eventType;
  }
  
  
 public Type getType() {
    return type;
  }


/**
  *  
  * @return
  */
 public Bar getBar() {
   return payload;
 }
 
 public String getTarget() {
   return source;
 }
 
 /**
  * Gets toString
  */
 public String toString() {
   return source + ":" + type + "@" + payload.getId();
 }
 
 

  

}
