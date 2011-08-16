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
package org.etk.service.bar;

import org.etk.service.bar.api.BarLifeCycleEvent;
import org.etk.service.bar.api.BarLifeCycleEvent.Type;
import org.etk.service.bar.api.BarLifeCycleListener;
import org.etk.service.common.event.AbstractLifeCycle;
import org.etk.service.foo.model.Bar;
/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Jul 21, 2011  
 */
public class BarLifeCycle extends AbstractLifeCycle<BarLifeCycleListener, BarLifeCycleEvent> {

  @Override
  protected void dispatchEvent(BarLifeCycleListener listener, BarLifeCycleEvent event) {
    switch (event.getType()) {
    
    case BAR_CREATED:
      listener.barCreated(event);
      break;
    case BAR_REMOVED:
      listener.barRemoved(event);
      break;
    case BAR_UPDATED:
      listener.barUpdated(event);
      break;
    default:
      break;
    }
  }
  
  public void barCreated(Bar bar, String executor) {
    broadcast(new BarLifeCycleEvent(bar, executor, Type.BAR_CREATED));
  }

  public void barDeleted(Bar bar, String executor) {
    broadcast(new BarLifeCycleEvent(bar, executor, Type.BAR_REMOVED));
  }
  
  public void barUpdated(Bar bar, String executor) {
    broadcast(new BarLifeCycleEvent(bar, executor, Type.BAR_UPDATED));
  }
}
