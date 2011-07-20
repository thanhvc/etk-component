package org.etk.reflection.test;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;

public abstract class AbstractReflectTestCase extends TestCase {
  
  protected final Set<Class<?>> classes = new HashSet<Class<?>>();
  protected abstract void configure();
  
  protected abstract ReflectUnitTest getUnitTest();
  
  /**
   * Adds class to resolve which provides for UnitTesting
   * @param clazz
   */
  protected final void add(Class clazz) {
    classes.add(clazz);
  }
  
  private static List<UnitTestPlugin> plugins;
  /**
   * Loading the class form META-INF/services/plugins
   * @return
   * @throws Exception
   */
  private static List<UnitTestPlugin> getPlugins() throws Exception {
    if (plugins == null) {
      ClassLoader cl = Thread.currentThread().getContextClassLoader();
      String path = "META-INF/services" + UnitTestPlugin.class.getName();
      Enumeration<URL> e = cl.getResources(path);
      List<UnitTestPlugin> list = new ArrayList<UnitTestPlugin>();
      while(e.hasMoreElements()) {
        URL url = e.nextElement();
        InputStream in = new BufferedInputStream(url.openStream());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        for (int i = in.read();i != -1;i = in.read()) {
          baos.write(i);
        }
        in.close();
        baos.close();
        String fqn = baos.toString().trim();
        Class<?> clazz = cl.loadClass(fqn);
        UnitTestPlugin plugin = (UnitTestPlugin)clazz.newInstance();
        list.add(plugin);
        plugins = list;
      }
      
    }
    
    return plugins;
  }

  
  public void testExecute() throws Exception {

    //
    configure();

    //
    ReflectUnitTest unitTest = getUnitTest();

    //
    for (UnitTestPlugin plugin : getPlugins()) {
      plugin.execute(unitTest, classes);
    }
  }
}
