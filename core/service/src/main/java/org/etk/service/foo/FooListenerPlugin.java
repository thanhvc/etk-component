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

import org.etk.kernel.management.ManagedPlugin;
import org.etk.service.foo.api.FooLifeCycleEvent;
import org.etk.service.foo.api.FooLifeCycleListener;


/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Jul 21, 2011  
 */
public abstract class FooListenerPlugin extends ManagedPlugin implements FooLifeCycleListener {

  /**
   * {@inheritDoc}
   */
  public abstract void fooCreated(FooLifeCycleEvent event);

  /**
   * {@inheritDoc}
   */
  public abstract void fooRemoved(FooLifeCycleEvent event);
  
  /**
   * {@inheritDoc}
   */
  public abstract void fooUpdated(FooLifeCycleEvent event);
}
