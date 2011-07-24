package org.exoplatform.container.test;

import java.util.Iterator;
import java.util.Set;

import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.ObjectInstance;
import javax.management.ObjectName;

import org.exoplatform.container.PortalContainer;
import org.exoplatform.container.RootContainer;
import org.exoplatform.container.configuration.ConfigurationManager;
import org.exoplatform.container.jmx.JMXProxyComponent;
import org.exoplatform.mocks.MockService;
import org.exoplatform.mocks.MockServiceA;
import org.exoplatform.test.BasicTestCase;

/**
 * Created by the Exo Development team. Author : Mestrallet Benjamin
 * benjamin.mestrallet@exoplatform.com
 */
public class TestJMXRegistration extends BasicTestCase {

  public void testJMXRegistration() throws Exception {
    PortalContainer pcontainer = PortalContainer.getInstance();
    pcontainer.getComponentInstanceOfType(ConfigurationManager.class);
    assertNotNull(pcontainer.getComponentInstanceOfType(MockService.class));
    MBeanServer mbeanServer = pcontainer.getMBeanServer();
    pcontainer.printMBeanServer();
    System.out.println("Default domain : " + mbeanServer.getDefaultDomain());
    String name = "component:type=" + MockService.class.getName();
    ObjectName objectName = new ObjectName(name);
    ObjectInstance instance = mbeanServer.getObjectInstance(objectName);

    System.out.println("===> OBJECT INSTANCE: " + instance);

    assertNotNull(instance);
    Object result = mbeanServer.invoke(objectName, "hello", null, null);
    System.out.println("Result : " + result);
    System.out.println("=================ROOT CONTAINER====================");
    RootContainer.getInstance().printMBeanServer();
    java.util.List servers = MBeanServerFactory.findMBeanServer(null);
    for (int i = 0; i < servers.size(); i++) {
      MBeanServer server = (MBeanServer) servers.get(i);
      System.out.println("Server with default domain : " + server.getDefaultDomain());
    }

    Set names = mbeanServer.queryNames(null, null);
    Iterator i = names.iterator();
    while (i.hasNext()) {
      ObjectName oname = (ObjectName) i.next();
      System.out.println("object name = " + oname.getCanonicalName());
    }
  }

  public void testProxyInvocation() throws Exception {
    System.out.println("\n\n\n");
    PortalContainer pcontainer = PortalContainer.getInstance();
    assertNotNull(pcontainer.getComponentInstanceOfType(MockServiceA.class));
    MBeanServer mbeanServer = pcontainer.getMBeanServer();
    String name = "component:type=" + MockServiceA.class.getName();
    ObjectName objectName = new ObjectName(name);
    MockServiceA mservice = (MockServiceA) JMXProxyComponent.getComponent(MockServiceA.class,
                                                                          mbeanServer,
                                                                          objectName);
    mservice.methodServiceA();
    mservice.testArguments("hello", 9);
  }
}
