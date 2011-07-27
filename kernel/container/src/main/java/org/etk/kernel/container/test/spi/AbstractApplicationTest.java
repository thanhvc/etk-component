package org.etk.kernel.container.test.spi;



import org.etk.kernel.container.test.annotations.ConfigurationUnit;
import org.etk.kernel.container.test.annotations.ConfiguredBy;
import org.etk.kernel.container.test.annotations.ContainerScope;

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
