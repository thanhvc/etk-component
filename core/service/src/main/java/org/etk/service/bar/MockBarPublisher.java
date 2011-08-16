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

import org.etk.common.logging.Logger;
import org.etk.service.bar.MockBarPublisher;
import org.etk.service.bar.api.BarLifeCycleEvent;


/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Jul 27, 2011  
 */
public class MockBarPublisher extends BarListenerPlugin {

  /**
   * The Logger.
   */
  private static final Logger log = Logger.getLogger(MockBarPublisher.class);
  
  @Override
  public void barCreated(BarLifeCycleEvent event) {
    
    log.debug("barCreated::Event Type = " + event.getType());
  }

  @Override
  public void barRemoved(BarLifeCycleEvent event) {
    log.debug("barRemoved::Event Type = " + event.getType());
  }

  @Override
  public void barUpdated(BarLifeCycleEvent event) {
    log.debug("barUpdated::Event Type = " + event.getType());
  }
  

}
