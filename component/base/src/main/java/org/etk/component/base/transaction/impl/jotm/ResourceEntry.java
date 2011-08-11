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
package org.etk.component.base.transaction.impl.jotm;

import java.util.List;

import javax.transaction.SystemException;
import javax.transaction.Transaction;
import javax.transaction.xa.XAException;

import org.etk.component.base.transaction.ExoResource;
import org.objectweb.transaction.jta.ResourceManagerEvent;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          thanhvucong.78@google.com
 * Aug 11, 2011  
 */
public class ResourceEntry implements ResourceManagerEvent
{

   List<?> jotmResourceList;

   final ExoResource resource;

   public ResourceEntry(ExoResource resource)
   {
      this.resource = resource;
   }

   public void enlistConnection(Transaction transaction) throws SystemException
   {
      try
      {
         /*
               if (LOG.isDebugEnabled())
                 LOG.debug("Enlist connection. Session: " + getSessionInfo() + ", " + this
                     + ", transaction: " + transaction);
         */
         resource.enlistResource();
      }
      catch (IllegalStateException e)
      {
         throw new SystemException(e.getMessage());
      }
      catch (XAException e)
      {
         throw new SystemException(e.getMessage());
      }
   }
}

