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

import org.etk.common.logging.Logger;
import org.etk.service.foo.spi.FooLifeCycleEvent;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Jul 27, 2011  
 */
public class MockFooPublisher extends FooListenerPlugin {

  /**
   * The Logger.
   */
  private static final Logger log = Logger.getLogger(MockFooPublisher.class);
  
  @Override
  public void fooCreated(FooLifeCycleEvent event) {
    
    log.debug("fooCreated::Event Type = " + event.getType());
  }

  @Override
  public void fooRemoved(FooLifeCycleEvent event) {
    log.debug("fooRemoved::Event Type = " + event.getType());
  }

  @Override
  public void fooUpdated(FooLifeCycleEvent event) {
    log.debug("fooUpdated::Event Type = " + event.getType());
  }

}
