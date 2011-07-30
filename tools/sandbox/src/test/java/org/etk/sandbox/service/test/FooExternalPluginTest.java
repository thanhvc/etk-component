package org.etk.sandbox.service.test;

import org.etk.common.logging.Logger;
import org.etk.kernel.test.spi.AbstractApplicationTest;
import org.etk.service.foo.model.Foo;
import org.etk.service.foo.spi.FooService;

public class FooExternalPluginTest extends AbstractApplicationTest {

  private Logger log = Logger.getLogger(FooExternalPluginTest.class);
  
  public FooExternalPluginTest() {
  }

  public FooExternalPluginTest(String name) {
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