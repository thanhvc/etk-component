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

import java.rmi.RemoteException;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.List;

import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.Xid;

import org.etk.common.logging.Logger;
import org.etk.common.utils.SecurityHelper;
import org.etk.component.base.naming.InitialContextInitializer;
import org.etk.component.base.transaction.ExoResource;
import org.etk.component.base.transaction.TransactionService;
import org.etk.kernel.container.xml.InitParams;
import org.objectweb.jotm.Current;
import org.objectweb.jotm.TransactionFactory;
import org.objectweb.jotm.TransactionFactoryImpl;
import org.objectweb.jotm.XidImpl;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          thanhvucong.78@google.com
 * Aug 11, 2011  
 */
public class TransactionServiceJotmImpl implements TransactionService
{

   protected static Logger log = Logger.getLogger(TransactionServiceJotmImpl.class);

   public static final String TRACK_WITHOT_TRANSACTION_PARAM = "track-without-transaction";

   private boolean trackWithoutTransaction = false;

   private Current current;

   public TransactionServiceJotmImpl(InitialContextInitializer initializer, InitParams params) throws RemoteException
   {
      current = Current.getCurrent();
      if (current == null)
      {
         try
         {
            SecurityHelper.doPrivilegedExceptionAction(new PrivilegedExceptionAction<Void>()
            {
               public Void run() throws Exception
               {
                  TransactionFactory tm = new TransactionFactoryImpl();
                  current = new Current(tm);
                  return null;
               }
            });
         }
         catch (PrivilegedActionException pae)
         {
            Throwable cause = pae.getCause();
            if (cause instanceof RemoteException)
            {
               throw (RemoteException)cause;
            }
            else if (cause instanceof RuntimeException)
            {
               throw (RuntimeException)cause;
            }
            else
            {
               throw new RuntimeException(cause);
            }
         }


         // Change the timeout only if JOTM is not initialized yet
         if (params != null)
         {
            if (params.getValueParam("timeout") != null)
            {

               int t = Integer.parseInt(params.getValueParam("timeout").getValue());
               current.setDefaultTimeout(t);
            }

            if (params.getValueParam(TRACK_WITHOT_TRANSACTION_PARAM) != null)
            {
               trackWithoutTransaction =
                  Boolean.parseBoolean(params.getValueParam(TRACK_WITHOT_TRANSACTION_PARAM).getValue());
            }
         }
      }
      else
      {
         log.info("Use externally initialized JOTM: " + current);
      }
   }

   /**
    * {@inheritDoc}
    */
   public TransactionManager getTransactionManager()
   {
      return current;
   }

   /**
    * {@inheritDoc}
    */
   public UserTransaction getUserTransaction()
   {
      return current;
   }

   /**
    * {@inheritDoc}
    */
   public void enlistResource(ExoResource exores) throws RollbackException, SystemException
   {
      XAResource xares = exores.getXAResource();
      ResourceEntry entry = new ResourceEntry(exores);
      exores.setPayload(entry);
      Transaction tx = getTransactionManager().getTransaction();
      if (tx != null)
      {
         current.getTransaction().enlistResource(xares);
      }
      else if (trackWithoutTransaction)
      {
         current.connectionOpened(entry);

         // actual only if current.connectionOpened(entry);
         // otherwise NPE inside the JOTM's Current 
         entry.jotmResourceList = popThreadLocalRMEventList();
         pushThreadLocalRMEventList(entry.jotmResourceList);
      }
   }

   /**
    * {@inheritDoc}
    */
   public void delistResource(ExoResource exores) throws RollbackException, SystemException
   {
      XAResource xares = exores.getXAResource();
      ResourceEntry entry = (ResourceEntry)exores.getPayload();
      Transaction tx = getTransactionManager().getTransaction();
      if (tx != null)
      {
         current.getTransaction().delistResource(xares, XAResource.TMNOFLAGS);
      }
      else if (trackWithoutTransaction)
      {
         current.connectionClosed(entry);

         // actual only if current.connectionClosed(entry);
         if (entry != null && entry.jotmResourceList != null)
         {
            entry.jotmResourceList.remove(xares);
         }
      }

      exores.setPayload(null);
   }

   /**
    * {@inheritDoc}
    */
   public Xid createXid()
   {
      return new XidImpl();
   }

   /**
    * {@inheritDoc}
    */
   public int getDefaultTimeout()
   {
      return current.getDefaultTimeout();
   }

   /**
    * {@inheritDoc}
    */
   public void setTransactionTimeout(int seconds) throws SystemException
   {
      current.setTransactionTimeout(seconds);
   }

   /**
    * Push a new event list on the stack of thread local resource event sets. The
    * list must contain only <code>ResourceManagerEvent</code> objects.
    * 
    * @param eventList the possibly null list of events to store forecoming
    *          <code>ResourceManagerEvent</code> events occuring in the current
    *          thread.
    */
   public void pushThreadLocalRMEventList(List eventList)
   {
      current.pushThreadLocalRMEventList(eventList);
   };

   /**
    * Pop the current set from the stack of thread local resource event sets The
    * list contains <code>ResourceManagerEvent</code> objects.
    * 
    * @return The possibly null <code>ResourceManagerEvent</code> list of events
    *         that have occured in the current thread since the last call of
    *         <code>pushThreadLocalRMEventList</code> or since the thread
    *         started.
    */
   public List popThreadLocalRMEventList()
   {
      return current.popThreadLocalRMEventList();
   };
}

