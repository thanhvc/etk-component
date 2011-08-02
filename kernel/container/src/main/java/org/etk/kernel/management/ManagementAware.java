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
package org.etk.kernel.management;

import org.etk.kernel.management.spi.ManagementContext;


/**
 * Defines the contract for managed object that wants to access a management context.
 * 
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Jul 28, 2011  
 */

public interface ManagementAware {

  /**
   * Sets the management context on the management aware object. If the context
   * is not null it means the start of the management life cycle and if the
   * scope is null it means the management life cycle ends.
   * 
   * @param context the management context
   */
  void setContext(ManagementContext context);

}
