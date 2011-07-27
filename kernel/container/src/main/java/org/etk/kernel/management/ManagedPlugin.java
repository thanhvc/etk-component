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

import org.etk.kernel.container.component.BaseComponentPlugin;
import org.etk.kernel.management.annotations.Managed;
import org.etk.kernel.management.annotations.ManagedDescription;
import org.etk.kernel.management.annotations.ManagedName;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Jul 27, 2011  
 */
@Managed
public abstract class ManagedPlugin extends BaseComponentPlugin {

  @Managed
  @ManagedName("Name")
  @ManagedDescription("The plugin name.")
  public String getName() {
    return super.getName();
  }
  
  @Managed
  @ManagedName("Description")
  @ManagedDescription("The plugin description.")
  public String getDescription() {
    
    return super.getDescription();
  }
  
}
