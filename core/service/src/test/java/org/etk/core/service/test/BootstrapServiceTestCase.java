package org.etk.core.service.test;

import org.etk.component.test.annotaions.ConfigurationUnit;
import org.etk.component.test.annotaions.ConfiguredBy;
import org.etk.component.test.annotaions.ContainerScope;
import org.etk.component.test.api.AbstractContainerTest;
import org.etk.storage.api.FooStorage;

@ConfiguredBy({
  @ConfigurationUnit(scope = ContainerScope.APPLICATION, path = "conf/configuration.xml")
})
public class BootstrapServiceTestCase extends AbstractContainerTest {

  public BootstrapServiceTestCase() {
  }

  public BootstrapServiceTestCase(String name) {
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
}
