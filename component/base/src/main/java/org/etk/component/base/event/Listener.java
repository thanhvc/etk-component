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
package org.etk.component.base.event;

import org.etk.kernel.container.component.BaseComponentPlugin;


/**
 * 
 * 
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          thanhvucong.78@google.com
 * Aug 6, 2011  
 */
public abstract class Listener<S, D> extends BaseComponentPlugin {
   // TODO: Should have the event name here to avoid the conflict with the plugin
   // name

   /**
    * This method should be invoked when an event with the same name is
    * broadcasted
    */
   public abstract void onEvent(Event<S, D> event) throws Exception;

}
