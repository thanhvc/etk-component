/*
 * Copyright (C) 2003-2007 eXo Platform SAS.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see<http://www.gnu.org/licenses/>.
 */
package org.exoplatform.services.database;

import java.sql.Connection;

import org.exoplatform.services.transaction.TransactionService;

/**
 * Created by The eXo Platform SAS Author : Tuan Nguyen
 * tuan08@users.sourceforge.net Apr 4, 2006 This service should provide a single
 * interface to access the diffent datasource.
 */
public interface DatabaseService {

  /**
   * This method should return the default datasource of the application
   * 
   * @return
   * @throws Exception
   */
  public ExoDatasource getDatasource() throws Exception;

  /**
   * This method should look up the datasouce by the datasource name and return.
   * If the datasource is not found then the method should return null
   * 
   * @param dsname
   * @return
   * @throws Exception
   */
  public ExoDatasource getDatasource(String dsname) throws Exception;

  // TODO: This method should be removed and used the getDataSource method
  public Connection getConnection() throws Exception;

  // TODO: This method should be removed and used the getDataSource method
  public Connection getConnection(String dsName) throws Exception;

  // TODO: This method should be removed and used the getDataSource method
  public void closeConnection(Connection conn) throws Exception;

  /**
   * This method should return the transaction service
   * 
   * @return
   * @throws Exception
   */
  public TransactionService getTransactionService() throws Exception;

}
