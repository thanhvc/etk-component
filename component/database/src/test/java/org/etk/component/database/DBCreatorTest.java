/*
 * Copyright (C) 2009 eXo Platform SAS.
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

import java.util.List;

import junit.framework.TestCase;

import org.etk.component.database.jdbc.CreateDBSchemaPlugin;
import org.etk.component.database.jdbc.DBSchemaCreator;
import org.etk.kernel.container.ApplicationContainer;

public class DBCreatorTest extends TestCase
{

   //  private StandaloneContainer container;
   private DBSchemaCreator dbcreator;

  public void setUp() throws Exception {
    // >>>>> to avoid two top-level container exception
    ApplicationContainer container = ApplicationContainer.getInstance();
    dbcreator = (DBSchemaCreator) container.getComponentInstanceOfType(DBSchemaCreator.class);
  }

   public void testConf() throws Exception
   {
      //    DBSchemaCreator dbcreator = (DBSchemaCreator) container.getComponentInstanceOfType(DBSchemaCreator.class);
      List plugins = (List)dbcreator.getPlugins();
      assertFalse(plugins.isEmpty());

      assertTrue(plugins.get(0) instanceof CreateDBSchemaPlugin);
      CreateDBSchemaPlugin plugin = (CreateDBSchemaPlugin)plugins.get(0);

      assertNotNull(plugin.getDataSource());
      assertNotNull(plugin.getScript());
   }

   public void tearDown() throws Exception
   {
      //    container.stop();
   }
}
