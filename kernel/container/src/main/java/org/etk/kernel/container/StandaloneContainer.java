/*
 * Copyright (C) 2003-2011 eXo Platform SAS.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.etk.kernel.container;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.etk.kernel.container.configuration.ConfigurationException;
import org.etk.kernel.container.configuration.ConfigurationManager;
import org.etk.kernel.container.configuration.ConfigurationManagerImpl;
import org.etk.kernel.container.monitor.jvm.J2EEServerInfo;
import org.etk.kernel.container.util.ContainerUtil;
import org.etk.kernel.container.xml.Configuration;
/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Jul 13, 2011  
 */

public class StandaloneContainer extends KernelContainer implements SessionManagerContainer {

  private static final long serialVersionUID = 12L;

  private static StandaloneContainer container;

  // TODO use ONLY attribute from context instead
  private static URL configurationURL = null;

  private static boolean useDefault = true;

  private SessionManager smanager_;

  private ConfigurationManagerImpl configurationManager;
  
  /**
   * Private StandaloneContainer constructor.
   *
   * @param configClassLoader ClassLoader 
   */
  private StandaloneContainer(ClassLoader configClassLoader) {
     configurationManager = new ConfigurationManagerImpl(configClassLoader, KernelContainer.getProfiles());
     registerComponentInstance(ConfigurationManager.class, configurationManager);
     registerComponentImplementation(SessionManagerImpl.class);
  }
  
  /**
   * Shortcut for getInstance(null, null).
   * 
   * @return the StandaloneContainer instance
   * @throws Exception if error occurs
   */
  public static StandaloneContainer getInstance() throws Exception {
     return getInstance(null, null);
  }

  /**
   * Shortcut for getInstance(configClassLoader, null).
   * 
   * @param configClassLoader ClassLoader
   * @return the StandaloneContainer instance
   * @throws Exception if error occurs
   */
  public static StandaloneContainer getInstance(ClassLoader configClassLoader) throws Exception {
     return getInstance(configClassLoader, null);
  }
  
  /**
   * Shortcut for getInstance(null, components).
   * 
   * @param components Object[][]
   * @return the StandaloneContainer instance
   * @throws Exception if error occurs
   */
  public static StandaloneContainer getInstance(Object[][] components) throws Exception {
    return getInstance(null, components);
  }

  /**
   * A way to inject externally instantiated objects to container before it
   * starts Object[][] components - an array of components in form: {{"name1",
   * component1}, {"name2", component2}, ...}.
   * 
   * @param configClassLoader ClassLoader 
   * @param components Object[][] 
   * @return the StandaloneContainer instance
   * @throws Exception if error occurs
   */
  public static StandaloneContainer getInstance(ClassLoader configClassLoader, Object[][] components) throws Exception {
    if (container == null) {
      container = new StandaloneContainer(configClassLoader);

      KernelContainerContext.setTopContainer(container);

      if (useDefault)
        container.initDefaultConf();
      // initialize configurationURL
      initConfigurationURL(configClassLoader);
      container.populate(configurationURL);
      if (components != null)
        container.registerArray(components);

      container.start();
      System.out.println("StandaloneContainer initialized using:  " + configurationURL);
    }
     return container;
  }

  protected void registerArray(Object[][] components) {
    for (Object[] comp : components) {
      if (comp.length != 2)
        continue;
      if (comp[0] == null || comp[1] == null)
        continue;
      if (comp[0].getClass().getName() != String.class.getName())
        continue;
      String n = (String) comp[0];
      Object o = comp[1];
      container.registerComponentInstance(n, o);
      System.out.println("StandaloneContainer: injecting \"" + n + "\"");
    }
  }

  /**
   * Add configuration URL. Plugable way of configuration. Add the configuration to existing configurations set.
   *
   * @param url, URL of location to configuration file
   * @throws MalformedURLException if path is wrong
   */
  public static void addConfigurationURL(String url) throws MalformedURLException {
    if ((url == null) || (url.length() == 0))
      return;
    URL confURL = new URL(url);
    configurationURL = fileExists(confURL) ? confURL : null;
  }

  /**
   * Set configuration URL. The configuration should contains all required components configured.
   *
   * @param url, URL of location to configuration file
   * @throws MalformedURLException if path is wrong
   */
  public static void setConfigurationURL(String url) throws MalformedURLException {
    useDefault = false;
    addConfigurationURL(url);
  }
  
  /**
   * Add configuration path. Plugable way of configuration. Add the configuration to existing configurations set.
   *
   * @param path, path to configuration file
   * @throws MalformedURLException if path is wrong
   */
  public static void addConfigurationPath(final String path) throws MalformedURLException {
    if ((path == null) || (path.length() == 0))
      return;

    URL confURL = new File(path).toURI().toURL();
    configurationURL = fileExists(confURL) ? confURL : null;
  }

  /**
   * Set configuration path. The configuration should contains all required
   * components configured.
   * 
   * @param path, path to configuration file
   * @throws MalformedURLException if path is wrong
   */
  public static void setConfigurationPath(String path) throws MalformedURLException {
    useDefault = false;
    addConfigurationPath(path);
  }
  
  /**
   * Get configurationURL.
   *
   * @return URL
   */
  public URL getConfigurationURL()
  {
     return configurationURL;
  }

  /**
   * 
   * @return
   */
  public String getConfigurationXML() {
    Configuration config = getConfiguration();
    if (config == null) {
      return null;
    }
    return config.toXML();
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void stop() {
     super.stop();
     KernelContainerContext.setTopContainer(null);
  }
  
  /**
   * 
   * 
   * @param url
   * @return
   */
  private static boolean fileExists(final URL url) {
    try {
      url.openStream().close();
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * Implements strategy of choosing configuration for this container.
   * 
   * @throws MalformedURLException
   * @throws ConfigurationException
   */
  private static void initConfigurationURL(ClassLoader configClassLoader) throws MalformedURLException {
     // (1) set by setConfigurationURL or setConfigurationPath
     // or
    if (configurationURL == null) {
      final J2EEServerInfo env = new J2EEServerInfo();

      // (2) exo-configuration.xml in AS (standalone) home directory
      configurationURL = (new File(env.getServerHome() + "/configuration.xml")).toURI().toURL();

      // (3) AS_HOME/conf/exo-conf (JBossAS usecase)
      if (!fileExists(configurationURL)) {
        configurationURL = (new File(env.getExoConfigurationDirectory() + "/configuration.xml")).toURI().toURL();

      }

      // (4) conf/exo-configuration.xml in war/ear(?)
      if (!fileExists(configurationURL) && configClassLoader != null) {
        configurationURL = configClassLoader.getResource("conf/test-configuration.xml");
      }
    }
  }

  private void initDefaultConf() throws Exception {
     configurationManager.addConfiguration(ContainerUtil.getConfigurationURL("conf/configuration.xml"));
     configurationManager.addConfiguration(ContainerUtil.getConfigurationURL("conf/application/test-configuration.xml"));
    try {
      configurationManager.addConfiguration("war:/conf/configuration.xml");
    } catch (Exception ex) {
      // TODO https://jira.jboss.org/jira/browse/EXOJCR-198
      // System.err.println("Error of default config init: ");
      // ex.printStackTrace();
    }
  }

  /**
   * Mapping the components in the configuration.xml file to StandaloneContainer
   * @param conf
   * @throws Exception
   */
  private void populate(URL conf) throws Exception {
    configurationManager.addConfiguration(conf);
    configurationManager.processRemoveConfiguration();
    ContainerUtil.addComponents(StandaloneContainer.this, configurationManager);
  }
  
  /**
   * Ccreate SessionContainer.
   * 
   * @param id String
   * @return SessionContainer instance
   */
  public SessionContainer createSessionContainer(String id) {
    SessionContainer scontainer = getSessionManager().getSessionContainer(id);
    if (scontainer != null)
      getSessionManager().removeSessionContainer(id);
    scontainer = new SessionContainer(id, null);
    getSessionManager().addSessionContainer(scontainer);
    SessionContainer.setInstance(scontainer);
    return scontainer;
  }

  /**
   * {@inheritDoc}
   */
  public SessionContainer createSessionContainer(String id, String owner) {
    return createSessionContainer(id);
  }

  /**
   * {@inheritDoc}
   */
  public List<SessionContainer> getLiveSessions() {
    return getSessionManager().getLiveSessions();
  }

  /**
   * {@inheritDoc}
   */
  public void removeSessionContainer(String sessionID) {
    getSessionManager().removeSessionContainer(sessionID);
  }

  @Override
  public SessionManager getSessionManager() {
     if (smanager_ == null)
        smanager_ = (SessionManager)this.getComponentInstanceOfType(SessionManager.class);
     return smanager_;
  }
}
