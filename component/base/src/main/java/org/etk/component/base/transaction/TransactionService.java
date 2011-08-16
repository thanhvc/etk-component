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
package org.etk.component.base.transaction;

import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;
import javax.transaction.xa.Xid;
/**
 * Created by The eXo Platform SAS.<br/> The transaction service
 * 
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          thanhvucong.78@google.com
 * Aug 6, 2011  
 */
public interface TransactionService {

  /**
   * @return TransactionManager
   */
  TransactionManager getTransactionManager();

  /**
   * @return UserTransaction
   */
  UserTransaction getUserTransaction();

  /**
   * @return default timeout in seconds
   */
  int getDefaultTimeout();

  /**
   * Sets timeout in seconds,
   * 
   * @param seconds int
   * @throws SystemException
   */
  void setTransactionTimeout(int seconds) throws SystemException;

  /**
   * Enlists XA resource in transaction manager.
   * 
   * @param xares XAResource
   * @throws RollbackException
   * @throws SystemException
   */
  void enlistResource(ExoResource xares) throws RollbackException, SystemException;

  /**
   * Delists XA resource from transaction manager.
   * 
   * @param xares XAResource
   * @throws RollbackException
   * @throws SystemException
   */
  void delistResource(ExoResource xares) throws RollbackException, SystemException;

  /**
   * Creates unique XA transaction identifier.
   * 
   * @return Xid
   */
  Xid createXid();

}
