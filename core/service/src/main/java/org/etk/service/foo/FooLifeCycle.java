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
package org.etk.service.foo;

import org.etk.service.core.event.AbstractLifeCycle;
import org.etk.service.foo.api.FooLifeCycleEvent;
import org.etk.service.foo.api.FooLifeCycleListener;
import org.etk.service.foo.api.FooLifeCycleEvent.Type;
import org.etk.service.foo.model.Foo;
/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Jul 21, 2011  
 */
public class FooLifeCycle extends AbstractLifeCycle<FooLifeCycleListener, FooLifeCycleEvent> {

  @Override
  protected void dispatchEvent(FooLifeCycleListener listener, FooLifeCycleEvent event) {
    switch (event.getType()) {
    
    case FOO_CREATED:
      listener.fooCreated(event);
      break;
    case FOO_REMOVED:
      listener.fooRemoved(event);
      break;
    case FOO_UPDATED:
      listener.fooUpdated(event);
      break;
    default:
      break;
    }
  }
  
  
  public void fooCreated(Foo foo, String executor) {
    broadcast(new FooLifeCycleEvent(foo, executor, Type.FOO_CREATED));
  }

  public void fooDeleted(Foo foo, String executor) {
    broadcast(new FooLifeCycleEvent(foo, executor, Type.FOO_REMOVED));
  }
  
  public void fooUpdated(Foo foo, String executor) {
    broadcast(new FooLifeCycleEvent(foo, executor, Type.FOO_UPDATED));
  }
}
