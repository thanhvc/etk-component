package org.etk.kernel.test.spi;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Set;
import java.util.Vector;

import org.etk.common.logging.Logger;


/**
 * The Base test classloader overrides the <code>getResources(String)</code> method to filter the resources
 * returned by the parent classloader in the following manner:
 * <ul>
 * <li>The loading of the <code>conf/configuration.xml</code> resource is replaced by the the configuration units
 * scoped at {@link ContainerScope#ROOT}.</li>
 * <li>The loading of the <code>conf/application/configuration.xml</code> resource is replaced by the the configuration units
 * scoped at {@link ContainerScope#APPICATION}.</li>
 * </ul>
 *
 */
public final class BaseTestClassLoader extends ClassLoader {

   /** . */
   private final Set<String> rootConfigPaths;

   /** . */
   private final Set<String> applicationConfigPaths;

   /** . */
   private Logger log = Logger.getLogger(BaseTestClassLoader.class);

  public BaseTestClassLoader(ClassLoader parent,
                             Set<String> rootConfigPaths,
                             Set<String> applicationConfigPaths) {
    super(parent);

    //
    this.rootConfigPaths = rootConfigPaths;
    this.applicationConfigPaths = applicationConfigPaths;
  }

   @Override
  public Enumeration<URL> getResources(String name) throws IOException {
    if ("conf/configuration.xml".equals(name)) {
      log.info("About to load root configuration");
      return getResourceURLs(rootConfigPaths);
    } else if ("conf/application/application-configuration.xml".equals(name)) {
      log.info("About to load application configuration");
      return getResourceURLs(applicationConfigPaths);
    } else if ("conf/application/test-configuration.xml".equals(name)) {
      return new Vector<URL>().elements();
    } else {
      return super.getResources(name);
    }
  }

  private Enumeration<URL> getResourceURLs(Set<String> paths) throws IOException {
    ArrayList<URL> urls = new ArrayList<URL>();
    for (String path : paths) {
      ArrayList<URL> resourceURLs = Collections.list(super.getResources(path));
      log.info("Want to load for resource named " + path + " the urls " + resourceURLs);
      urls.addAll(resourceURLs);
    }
    return Collections.enumeration(urls);
  }
}

