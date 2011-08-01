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
package org.etk.core.service.test;

import org.etk.common.logging.Logger;
import org.etk.kernel.test.spi.AbstractApplicationTest;
import org.etk.service.foo.model.Foo;
import org.etk.service.foo.spi.FooService;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Jul 27, 2011  
 */
public class FooServicePluginTest extends AbstractApplicationTest {

  private Logger log = Logger.getLogger(FooServicePluginTest.class);
  
  public FooServicePluginTest() {
  }

  public FooServicePluginTest(String name) {
    super(name);
  }

  @Override
  protected void end() {
    end(false);
  }

  protected void end(boolean save) {
    super.end();
  }

  @Override
  protected void setUp() throws Exception {
  }

  @Override
  protected void tearDown() throws Exception {
    //
  }
  
  public void testFooService() throws Exception {
    FooService service = (FooService) getContainer().getComponentInstanceOfType(FooService.class);
    assertNotNull(service);
  }
  
  public void testCreateFoo() throws Exception {
    FooService service = (FooService) getContainer().getComponentInstanceOfType(FooService.class);
    Foo foo = new Foo("F0001", "Name for F0001");
    Foo first = service.createFoo(foo);
  }
  
  public void testUpdateFoo() throws Exception {
    FooService service = (FooService) getContainer().getComponentInstanceOfType(FooService.class);
    Foo foo = new Foo("F0002", "Name for F0002");
    Foo first = service.updateFoo(foo);
  }
  
  public void testDeleteFoo() throws Exception {
    FooService service = (FooService) getContainer().getComponentInstanceOfType(FooService.class);
    Foo foo = new Foo("F0003", "Name for F0003");
    service.deleteFoo(foo);
  }
}
