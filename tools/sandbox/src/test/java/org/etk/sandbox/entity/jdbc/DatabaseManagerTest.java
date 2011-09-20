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
package org.etk.sandbox.entity.jdbc;

import java.util.List;
import java.util.TreeSet;

import javolution.util.FastList;

import org.etk.common.logging.Logger;
import org.etk.sandbox.entity.BaseEntityTestCase;
import org.etk.sandbox.entity.plugins.model.xml.Configuration;

/**
 * Created by The eXo Platform SAS Author : eXoPlatform exo@exoplatform.com Sep
 * 20, 2011
 */
public class DatabaseManagerTest extends BaseEntityTestCase {

  private Logger log = Logger.getLogger(DatabaseManagerTest.class);

  @Override
  protected void end() {
    end(false);
  }

  protected void end(boolean save) {
    super.end();
  }

  @Override
  protected void setUp() throws Exception {
    super.setUp();
  }

  @Override
  protected void tearDown() throws Exception {
    super.tearDown();
  }

  public void testGetTableNames() throws Exception {
    Configuration entityModelConf = dbManager.getEntityModelConf();
    assertNotNull(entityModelConf);

    List<String> messages = FastList.newInstance();
    TreeSet<String> tableNames = dbManager.getTablesNames(messages);
    assertNotNull(tableNames);
    
  }
  
  public void testCheckDb() throws Exception {
    Configuration entityModelConf = dbManager.getEntityModelConf();
    assertNotNull(entityModelConf);

    List<String> messages = FastList.newInstance();
    dbManager.checkDb(entityModelConf.getEntityMap(), messages, true);
    
  }
}
