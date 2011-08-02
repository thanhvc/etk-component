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

import java.util.List;


/**
 * The managed resource provided by the kernel to a management provider. It gives access
 * to the resource itself, the meta data of the managed resource, the attached scoped data
 * and the before/after invoke contract when a resource is invoked from the management layer.
 * 
 * 
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Jul 28, 2011  
 */
/**
 
 *
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public interface ManagedResource extends ManagementContext
{

   /**
    * The resource implementation.
    *
    * @return the resource
    */
   Object getResource();

   /**
    * Returns the resource meta data.
    *
    * @return the meta data
    */
   ManagedTypeMetaData getMetaData();

   /**
    * Returns the scoping data of the context. The list contains the scoping properties
    * registered by the provider if a call has been made to the {@link #setScopingData(Class, Object)}
    * method plus the scoping properties of the parent context.
    *
    * @param scopeType the scope type
    * @param <S> the generic type of the scope type
    * @return the scoping properties
    */
   <S> List<S> getScopingData(Class<S> scopeType);

   /**
    * Callback made by the provider to the resource to signal that scoping data that is used for the managed resource.
    *
    * @param scopeType the scope type
    * @param <S> the generic type of the scope type
    * @param scopingData the scoping data
    */
   <S> void setScopingData(Class<S> scopeType, S scopingData);

   /**
    * Before a managed resource is invoked by the management layer.
    *
    * @param managedResource the managed resource
    */
   void beforeInvoke(Object managedResource);

   /**
    * After a managed resource is invoked by the management layer.
    *
    * @param managedResource the managed resource
    */
   void afterInvoke(Object managedResource);

}

