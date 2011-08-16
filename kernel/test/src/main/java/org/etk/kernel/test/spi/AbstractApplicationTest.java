package org.etk.kernel.test.spi;



import org.etk.kernel.test.annotations.ConfigurationUnit;
import org.etk.kernel.test.annotations.ConfiguredBy;
import org.etk.kernel.test.annotations.ContainerScope;

@ConfiguredBy({
  @ConfigurationUnit(scope = ContainerScope.APPLICATION, path = "conf/application/org.etk.kernel.cache-configuration.xml"),
  @ConfigurationUnit(scope = ContainerScope.APPLICATION, path = "conf/application/org.etk.core.service-configuration.xml"),
  @ConfigurationUnit(scope = ContainerScope.APPLICATION, path = "conf/application/org.etk.core.rest-configuration.xml"),
  @ConfigurationUnit(scope = ContainerScope.APPLICATION, path = "conf/application/org.etk.extras.benches-configuration.xml"),
  @ConfigurationUnit(scope = ContainerScope.APPLICATION, path = "conf/application/org.etk.tools.sandbox-configuration.xml")
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
