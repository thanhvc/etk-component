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
package org.etk.kernel.management.spi;

/**
 * A context for managed objects that wants to do more.
 * 
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Jul 28, 2011  
 */
public interface ManagementContext {

   /**
    * Register an object as a managed object.
    *
    * @param o the object to be managed
    * @throws IllegalArgumentException if the object is not manageable
    * @throws NullPointerException if the object is null
    */
   void register(Object o) throws IllegalArgumentException, NullPointerException;

   /**
    * Unregisters an object from its managed life cycle.
    *
    * @param o the object to be unmanaged
    * @throws IllegalArgumentException if the object is not manageable
    * @throws NullPointerException if the object is null
    */
   void unregister(Object o) throws IllegalArgumentException, NullPointerException;

}
