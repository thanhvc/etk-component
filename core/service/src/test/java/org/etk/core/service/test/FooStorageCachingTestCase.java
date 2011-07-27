package org.etk.core.service.test;

import java.util.List;

import org.etk.common.logging.Logger;
import org.etk.component.test.api.AbstractContainerTest;
import org.etk.service.foo.FooFilter;
import org.etk.service.foo.model.Foo;
import org.etk.storage.api.FooStorage;

public class FooStorageCachingTestCase extends AbstractContainerTest {

  private Logger log = Logger.getLogger(FooStorageCachingTestCase.class);
  
  public FooStorageCachingTestCase() {
  }

  public FooStorageCachingTestCase(String name) {
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
  
  public void testFooComponentService() throws Exception {
    FooStorage storage = (FooStorage) getContainer().getComponentInstanceOfType(FooStorage.class);
    assertNotNull(storage);
  }
  
  public void testFindById() throws Exception {
    FooStorage storage = (FooStorage) getContainer().getComponentInstanceOfType(FooStorage.class);
    Foo first = storage.findById("F0001");
    
    assertEquals("Foo::Id must be equal F0001::", "F0001", first.getId());
    assertEquals("Foo::Description must be equal Description for F0001::", "Name for F0001", first.getName());
    
    log.debug("Second time to findById(F0001) get in Cache and Can not access to MockfooStorageImpl.findById(F0001)");
    
    Foo second = storage.findById("F0001");
    
    assertEquals("Foo::Id must be equal F0001::", "F0001", second.getId());
    assertEquals("Foo::Description must be equal Description for F0001::", "Name for F0001", second.getName());
  }
  
  public void testGetFooByFilter() throws Exception {
    FooStorage storage = (FooStorage) getContainer().getComponentInstanceOfType(FooStorage.class);
    FooFilter fooFilter = new FooFilter(); 
    fooFilter.setName("ThanhVC");
    List<Foo> first = storage.getFooByFilter(fooFilter, 0, 5);
    
    assertEquals("Foos must be equal 5::", 5, first.size());
        
    log.debug("Second time to getFooByFilter(fooFilter, 0, 5) get in Cache and Can not access to MockfooStorageImpl.getFooByFilter(fooFilter, 0, 5)");
    
    List<Foo> second = storage.getFooByFilter(fooFilter, 0, 5);
    
    assertEquals("Foos must be equal 5 and get from caching::", 5, second.size());
  }
}
