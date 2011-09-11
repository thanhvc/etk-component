/*
 * Copyright (C) 2010 eXo Platform SAS.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.etk.component.database;

import junit.framework.TestCase;

import org.etk.component.base.naming.InitialContextInitializer;
import org.etk.component.database.creator.DBConnectionInfo;
import org.etk.component.database.creator.DBCreator;
import org.etk.kernel.container.ApplicationContainer;
import org.etk.kernel.container.configuration.ConfigurationManager;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

/**
 * @author <a href="anatoliy.bazko@exoplatform.org">Anatoliy Bazko</a>
 * @version $Id: TestDBCreator.java 4584 2011-07-01 13:39:07Z tolusha $
 */
public class TestDBCreator extends TestCase {

  protected DBCreator dbCreator;

  private InitialContextInitializer initContext;

  private ApplicationContainer container;

  @Override
  public void setUp() throws Exception {
    container = ApplicationContainer.getInstance();
    dbCreator = (DBCreator) container.getComponentInstanceOfType(DBCreator.class);
    initContext = (InitialContextInitializer) container.getComponentInstanceOfType(InitialContextInitializer.class);
  }

  public void testDBCreate() throws Exception {
    assertNotNull(dbCreator);

    DBConnectionInfo dbInfo = dbCreator.createDatabase("testdb");
    DBConnectionInfo dbInfo1 = dbCreator.getDBConnectionInfo("testdb");

    Map<String, String> connProps = dbInfo.getProperties();
    Map<String, String> connProps1 = dbInfo1.getProperties();

    assertEquals(connProps.get("driverClassName"), connProps1.get("driverClassName"));
    assertEquals(connProps.get("username"), connProps1.get("username"));
    assertEquals(connProps.get("url"), connProps1.get("url"));
    assertEquals(connProps.get("password"), connProps1.get("password"));

    Map<String, String> refAddr = dbInfo.getProperties();

    initContext.getInitialContextBinder().bind("testjdbcjcr",
                                               "javax.sql.DataSource",
                                               "org.apache.commons.dbcp.BasicDataSourceFactory",
                                               null,
                                               refAddr);

    DataSource ds = (DataSource) initContext.getInitialContext().lookup("testjdbcjcr");
    assertNotNull(ds);

    Connection conn = ds.getConnection();
    assertNotNull(conn);
  }

  public void testDBCreateWithSpecificProperties() throws Exception {
    assertNotNull(dbCreator);

    String serverUrl = "jdbc:postgresql://localhost/txdb";
    Map<String, String> connectionProperties = new HashMap<String, String>();
    connectionProperties.put("driverClassName", "org.postgresql.Driver");
    connectionProperties.put("username", "root");
    connectionProperties.put("password", "gtn");

    ConfigurationManager cm = (ConfigurationManager) container.getComponentInstanceOfType(ConfigurationManager.class);
    DBCreator dbCreator = new DBCreator(serverUrl,
                                        connectionProperties,
                                        "classpath:/dbcreator/test.sql",
                                        "root",
                                        "gtn",
                                        cm);

    DBConnectionInfo dbInfo = dbCreator.createDatabase("testdb");
    DBConnectionInfo dbInfo1 = dbCreator.getDBConnectionInfo("testdb");

    Map<String, String> connProps = dbInfo.getProperties();
    Map<String, String> connProps1 = dbInfo1.getProperties();

    assertEquals(connProps.get("driverClassName"), connProps1.get("driverClassName"));
    assertEquals(connProps.get("username"), connProps1.get("username"));
    assertEquals(connProps.get("url"), connProps1.get("url"));
    assertEquals(connProps.get("password"), connProps1.get("password"));

    Map<String, String> refAddr = dbInfo.getProperties();

    initContext.getInitialContextBinder().bind("testjdbcjcr2",
                                               "javax.sql.DataSource",
                                               "org.apache.commons.dbcp.BasicDataSourceFactory",
                                               null,
                                               refAddr);

    DataSource ds = (DataSource) initContext.getInitialContext().lookup("testjdbcjcr2");
    assertNotNull(ds);

    Connection conn = ds.getConnection();
    assertNotNull(conn);
  }

  public void testDBCreateMultiThread() throws Exception {
    DBCreateThread[] queue = new DBCreateThread[10];

    for (int i = 0; i < queue.length; i++) {
      queue[i] = new DBCreateThread(i);
      queue[i].start();
    }

    for (int i = 0; i < queue.length; i++) {
      queue[i].join();
    }

    for (int i = 0; i < queue.length; i++) {
      DataSource ds = (DataSource) initContext.getInitialContext().lookup("testjdbcjcr_" + i);
      assertNotNull(ds);

      Connection conn = ds.getConnection();
      assertNotNull(conn);
    }

  }

  class DBCreateThread extends Thread {

    private final int threadNumber;

    DBCreateThread(int threadNumber) {
      this.threadNumber = threadNumber;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run() {
      try {
        DBConnectionInfo dbInfo = dbCreator.createDatabase("testdb_" + threadNumber);

        Map<String, String> refAddr = dbInfo.getProperties();

        initContext.getInitialContextBinder()
                   .bind("testjdbcjcr_" + threadNumber,
                         "javax.sql.DataSource",
                         "org.apache.commons.dbcp.BasicDataSourceFactory",
                         null,
                         refAddr);
      } catch (Exception e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
  }

}
