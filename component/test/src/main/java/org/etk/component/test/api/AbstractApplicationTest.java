package org.etk.component.test.api;

import org.etk.component.test.annotaions.ConfigurationUnit;
import org.etk.component.test.annotaions.ConfiguredBy;
import org.etk.component.test.annotaions.ContainerScope;

@ConfiguredBy({
  @ConfigurationUnit(scope = ContainerScope.APPLICATION, path = "conf/application-configuration.xml")
})
public abstract class AbstractApplicationTest extends AbstractContainerTest {

  public AbstractApplicationTest() {
  }

  public AbstractApplicationTest(String name) {
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
}
