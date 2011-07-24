package org.etk.kernel.container.test;

import junit.framework.TestCase;

import org.etk.kernel.container.ApplicationContainer;
import org.etk.kernel.container.RootContainer;
import org.etk.kernel.container.definition.ApplicationContainerConfig;

/**
 * Created by the Exo Development team. Author : Mestrallet Benjamin
 * benjamin.mestrallet@exoplatform.com
 */

public class TestContainer extends TestCase {

  public void setUp() throws Exception {
    System.setProperty("maven.exoplatform.dir", TestContainer.class.getResource("/").getFile());
  }

  public void testPortalContainer() throws Exception {
    RootContainer rootContainer = RootContainer.getInstance();
    ApplicationContainer pcontainer = rootContainer.getPortalContainer("standalone");
    Object parent = pcontainer.getParent();
    assertTrue("Root container should not be null", parent != null);
  }
}
