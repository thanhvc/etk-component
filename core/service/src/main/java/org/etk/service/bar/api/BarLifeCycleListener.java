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

import org.etk.service.common.event.LifeCycleListener;


/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Jul 21, 2011  
 */
public interface BarLifeCycleListener extends LifeCycleListener<BarLifeCycleEvent> {

  /**
   * Invokes this method when a bar is created.
   *
   * @param event the Bar lifecycle event
   */
  void barCreated(BarLifeCycleEvent event);

  /**
   * Invokes this method when a bar is removed.
   *
   * @param event the Bar lifecyle event
   */
  void barRemoved(BarLifeCycleEvent event);
  
  /**
   * Invokes this method when a bar is updated.
   *
   * @param event the Bar lifecyle event
   */
  void barUpdated(BarLifeCycleEvent event);
}
