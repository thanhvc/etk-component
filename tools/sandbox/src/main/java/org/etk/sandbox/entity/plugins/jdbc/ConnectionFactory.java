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
package org.etk.sandbox.entity.plugins.jdbc;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.etk.common.logging.Logger;
import org.etk.component.database.DatabaseService;
import org.etk.kernel.container.ApplicationContainer;
import org.etk.sandbox.entity.base.utils.UtilValidate;
import org.etk.sandbox.entity.core.GenericEntityException;
import org.etk.sandbox.entity.plugins.connection.ConnectionFactoryInterface;



/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Sep 19, 2011  
 */
public class ConnectionFactory {

  private final static Logger logger = Logger.getLogger(ConnectionFactory.class);
  private static ConnectionFactoryInterface factory = null;
  
  public static Connection getConnection(String driverName,
                                         String connectionUrl,
                                         Properties props,
                                         String userName,
                                         String password) throws SQLException {
    // first register the JDBC driver with the DriverManager
    if (driverName != null) {
      ConnectionFactory.loadDriver(driverName);
    }
    try {
      if (UtilValidate.isNotEmpty(userName))
        return DriverManager.getConnection(connectionUrl, userName, password);
      else if (props != null)
        return DriverManager.getConnection(connectionUrl, props);
      else
        return DriverManager.getConnection(connectionUrl);
    } catch (SQLException e) {
      logger.error("SQL Error obtaining JDBC connection", e);
      throw e;
    }
  }
  
  public static Connection getConnection() throws SQLException, GenericEntityException {
    ApplicationContainer container = ApplicationContainer.getInstance();
    DatabaseService dbService = (DatabaseService) container.getComponentInstanceOfType(DatabaseService.class);

    Connection con;
    try {
      con = dbService.getConnection();
      if (con == null) {
        logger.error("******* ERROR: No database connection found");
      }
      return con;
    } catch (Exception e) {
      throw new GenericEntityException(e.getCause());
    }

    
  }
  
  public static void loadDriver(String driverName) throws SQLException {
    if (DriverManager.getDriver(driverName) == null) {
      try {
        Driver driver = (Driver) Class.forName(driverName, true, Thread.currentThread().getContextClassLoader()).newInstance();
        DriverManager.registerDriver(driver);
      } catch (ClassNotFoundException e) {
        logger.warn("Unable to load driver [" + driverName + "]", e);
      } catch (InstantiationException e) {
        logger.warn("Unable to instantiate driver [" + driverName + "]", e);
      } catch (IllegalAccessException e) {
        logger.warn("Illegal access exception [" + driverName + "]", e);
      }
    }
  }
}
