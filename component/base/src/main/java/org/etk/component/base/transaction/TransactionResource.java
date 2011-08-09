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

/**
 * Created by The eXo Platform SAS.<br/> Internal resource manager for support
 * transaction
 * 
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          thanhvucong.78@google.com
 * Aug 6, 2011  
 */
public interface TransactionResource {

  /**
   * called when transaction is starting (by
   * javax.transaction.xa.XAResource#start(javax.transaction.xa.Xid, int))
   * 
   * @throws TransactionException
   */
  void start() throws TransactionException;

  /**
   * called when transaction is committing (by
   * javax.transaction.xa.XAResource#commit(javax.transaction.xa.Xid, boolean))
   * 
   * @throws TransactionException
   */
  void commit() throws TransactionException;

  /**
   * called when transaction is rolling back (by
   * javax.transaction.xa.XAResource#rollback(javax.transaction.xa.Xid))
   * 
   * @throws TransactionException
   */
  void rollback() throws TransactionException;

}

