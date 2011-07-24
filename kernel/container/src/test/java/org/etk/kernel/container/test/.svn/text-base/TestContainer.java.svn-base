package org.exoplatform.container.test;

import java.util.List;

import org.exoplatform.container.PortalContainer;
import org.exoplatform.container.RootContainer;
import org.exoplatform.container.component.ComponentPlugin;
import org.exoplatform.mocks.MockService;
import org.exoplatform.mocks.PriorityService;
import org.exoplatform.test.BasicTestCase;

/**
 * Created by the Exo Development team. Author : Mestrallet Benjamin
 * benjamin.mestrallet@exoplatform.com
 */

public class TestContainer extends BasicTestCase {

  public void setUp() throws Exception {
    System.setProperty("maven.exoplatform.dir", TestContainer.class.getResource("/").getFile());
  }

  public void testPortalContainer() throws Exception {
    RootContainer rootContainer = RootContainer.getInstance();
    PortalContainer pcontainer = rootContainer.getPortalContainer("portal");
    Object parent = pcontainer.getParent();
    assertTrue("Root container should not be null", parent != null);
    pcontainer.createSessionContainer("sessioncontainer1", "anon");
    pcontainer.createSessionContainer("sessioncontainer2", "anon");
    List sessions = pcontainer.getLiveSessions();
    assertEquals("expect 2 session container", 2, sessions.size());
    // performance test

    int INSERTLOOP = 0;
    long start = System.currentTimeMillis();
    for (int i = 0; i < INSERTLOOP; i++) {
      rootContainer.getPortalContainer("name-" + Integer.toString(i));
    }
    System.out.println("Insert 1000 components " + (System.currentTimeMillis() - start) + "ms");

    int LOOP = 10000000;
    start = System.currentTimeMillis();
    for (int i = 0; i < LOOP; i++) {
      pcontainer = (PortalContainer) rootContainer.getComponentInstance("portal");
      assertTrue("not null", pcontainer != null);
    }
    System.out.println("Retrieve compoponent 10M times " + (System.currentTimeMillis() - start)
        + "ms");
    System.out.println("AVG = " + (System.currentTimeMillis() - start) / LOOP + "ms");
    System.out.println("-------------------------------------------------------------------------");
  }

  public void testComponent() throws Exception {
    RootContainer rootContainer = RootContainer.getInstance();
    MockService mservice = (MockService) rootContainer.getComponentInstance("MockService");
    assertTrue(mservice != null);
    assertTrue(mservice.getPlugins().size() == 2);
  }

  public void testComponent2() throws Exception {
    System.out.println("-------------------------MULTIBLE COMPONENT-------------------------");
    RootContainer rootContainer = RootContainer.getInstance();
    PortalContainer pcontainer = (PortalContainer) rootContainer.getComponentInstance("portal");
    assertNotNull(pcontainer);
    MultibleComponent c = (MultibleComponent) pcontainer.getComponentInstanceOfType(MultibleComponent.class);
    assertNotNull(c);
    System.out.println("First instance MultibleComponent:  " + c.hash());
    c = (MultibleComponent) pcontainer.getComponentInstanceOfType(MultibleComponent.class);
    assertNotNull(c);
    System.out.println("Second instance MultibleComponent: " + c.hash());
    c = (MultibleComponent) pcontainer.getComponentInstanceOfType(MultibleComponent.class);
    assertNotNull(c);
    System.out.println("Third instance MultibleComponent:  " + c.hash());
    System.out.println("-------------------------------------------------------------------------");
  }

  public void testComponent3() throws Exception {
    System.out.println("-------------------------DEFAULT COMPONENT-------------------------");
    RootContainer rootContainer = RootContainer.getInstance();
    PortalContainer pcontainer = (PortalContainer) rootContainer.getComponentInstance("portal");
    assertNotNull(pcontainer);
    DefaultComponent c = (DefaultComponent) pcontainer.getComponentInstanceOfType(DefaultComponent.class);
    assertNotNull(c);
    System.out.println("First instance DefaultComponent:  " + c.hash());
    c = (DefaultComponent) pcontainer.getComponentInstanceOfType(DefaultComponent.class);
    assertNotNull(c);
    System.out.println("Second instance DefaultComponent: " + c.hash());
    c = (DefaultComponent) pcontainer.getComponentInstanceOfType(DefaultComponent.class);
    assertNotNull(c);
    System.out.println("Third instance DefaultComponent:  " + c.hash());
    System.out.println("-------------------------------------------------------------------------");
  }
  
  public void testPriorityPlugins() {
    RootContainer rootContainer = RootContainer.getInstance();
    PortalContainer pcontainer = (PortalContainer) rootContainer.getComponentInstance("portal");
    assertNotNull(pcontainer);
    PriorityService ps = (PriorityService) pcontainer.getComponentInstanceOfType(PriorityService.class);
    assertNotNull(ps);
    List<ComponentPlugin> l = ps.getPlugins();
    assertNotNull(l);
    assertEquals(3, l.size());
    assertEquals("PluginPriority3", l.get(0).getName());
    assertEquals("PluginPriority1", l.get(1).getName());
    assertEquals("PluginPriority2", l.get(2).getName());
  }

  // public void testClientType() throws Exception {
  // System.setProperty("javax.xml.parsers.DocumentBuilderFactory",
  // "javax.xml.parsers.DocumentBuilderFactory") ;
  // ClientTypeMap.getInstance() ;
  // }
}
