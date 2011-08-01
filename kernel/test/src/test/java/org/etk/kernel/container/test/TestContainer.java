package org.etk.kernel.container.test;


import org.etk.kernel.container.ApplicationContainer;
import org.etk.kernel.container.RootContainer;
import org.etk.kernel.test.spi.AbstractApplicationTest;
import org.etk.kernel.test.spi.AbstractContainerTest;

public class TestContainer extends AbstractApplicationTest {

  public void setUp() throws Exception {
    System.setProperty("maven.etk.dir", TestContainer.class.getResource("/").getFile());
  }

  public void testPortalContainer() throws Exception {
    RootContainer rootContainer = RootContainer.getInstance();
    ApplicationContainer pcontainer = rootContainer.getPortalContainer("standalone");
    Object parent = pcontainer.getParent();
    assertTrue("Root container should not be null", parent != null);
  }
}
