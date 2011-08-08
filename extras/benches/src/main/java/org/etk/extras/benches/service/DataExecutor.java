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
package org.etk.extras.benches.service;

import java.util.List;
import java.util.concurrent.ConcurrentMap;

import org.etk.extras.benches.service.common.InjectionException;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Aug 5, 2011  
 */
public interface DataExecutor {

  /**
   * Entry point to executes the DataInjector task.
   * The Task which was configured in the configuration.xml and adds task 
   * to InjectorCompletionService for executing.
   *   
   * @throws Exception
   */
  public void execute() throws InjectionException;
  
  
  public ConcurrentMap<String, List<?>> getExchanger();
}
