/*
 * Copyright (C) 2003-2007 eXo Platform SAS.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see<http://www.gnu.org/licenses/>.
 */
package org.etk.component.database.impl;

import java.io.Serializable;
import java.net.URL;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.etk.common.exception.ObjectNotFoundException;
import org.etk.common.logging.Logger;
import org.etk.component.database.HibernateService;
import org.etk.component.database.ObjectQuery;
import org.etk.kernel.cache.CacheService;
import org.etk.kernel.container.KernelContainer;
import org.etk.kernel.container.component.ComponentPlugin;
import org.etk.kernel.container.component.ComponentRequestLifecycle;
import org.etk.kernel.container.xml.InitParams;
import org.etk.kernel.container.xml.PropertiesParam;
import org.etk.kernel.container.xml.Property;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.dialect.Dialect;
import org.hibernate.tool.hbm2ddl.SchemaUpdate;

public class HibernateServiceImpl implements HibernateService, ComponentRequestLifecycle {
  private ThreadLocal<Session> threadLocal_;

  private static Logger log = Logger.getLogger(HibernateServiceImpl.class);

  private HibernateConfigurationImpl conf;

  private SessionFactory sessionFactory;

  private HashSet<String>  mappings = new HashSet<String>();

  @SuppressWarnings("unchecked")
  public HibernateServiceImpl(InitParams initParams, CacheService cacheService) {
    threadLocal_ = new ThreadLocal<Session>();
    PropertiesParam param = initParams.getPropertiesParam("hibernate.properties");
    HibernateSettingsFactory settingsFactory = new HibernateSettingsFactory(new ExoCacheProvider(cacheService));
    conf = new HibernateConfigurationImpl(settingsFactory);
    Iterator properties = param.getPropertyIterator();
    while (properties.hasNext()) {
      Property p = (Property) properties.next();

      //
      String name = p.getName();
      String value = p.getValue();

      // Julien: Don't remove that unless you know what you are doing
      if (name.equals("hibernate.dialect")) {
        Package pkg = Dialect.class.getPackage();
        String dialect = value.substring(22);
        value = pkg.getName() + "." + dialect; // 22 is the length of
                                               // "org.hibernate.dialect"
        log.info("Using dialect " + dialect);
      }

      //
      conf.setProperty(name, value);
    }

    // Replace the potential "java.io.tmpdir" variable in the connection URL
    String connectionURL = conf.getProperty("hibernate.connection.url");
    if (connectionURL != null) {
      connectionURL = connectionURL.replace("${java.io.tmpdir}",
                                            System.getProperty("java.io.tmpdir"));
      conf.setProperty("hibernate.connection.url", connectionURL);
    }
  }

  public void addPlugin(ComponentPlugin plugin) {
    if (plugin instanceof AddHibernateMappingPlugin) {
      AddHibernateMappingPlugin impl = (AddHibernateMappingPlugin) plugin;
      try {
        List path = impl.getMapping();
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        for (int i = 0; i < path.size(); i++) {
          String relativePath = (String) path.get(i);
          if (!mappings.contains(relativePath)) {
            mappings.add(relativePath);
            URL url = cl.getResource(relativePath);
            log.info("Adding  Hibernate Mapping: " + relativePath);
            conf.addURL(url);
          }
        }
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    }
  }

  public ComponentPlugin removePlugin(String name) {
    return null;
  }

  public Collection getPlugins() {
    return null;
  }

  public Configuration getHibernateConfiguration() {
    return conf;
  }

  /**
   * @return the SessionFactory
   */
  public SessionFactory getSessionFactory() {
    if (sessionFactory == null) {
      sessionFactory = conf.buildSessionFactory();
      new SchemaUpdate(conf).execute(false, true);
    }
    return sessionFactory;
  }

  public Session openSession() {
    Session currentSession = threadLocal_.get();
    if (currentSession == null) {
      if (log.isDebugEnabled())
        log.debug("open new hibernate session in openSession()");
      currentSession = getSessionFactory().openSession();
      threadLocal_.set(currentSession);
    }
    return currentSession;
  }

  public Session openNewSession() {
    Session currentSession = threadLocal_.get();
    if (currentSession != null) {
      closeSession(currentSession);
    }
    currentSession = getSessionFactory().openSession();
    threadLocal_.set(currentSession);
    return currentSession;
  }

  public void closeSession(Session session) {
    if (session == null)
      return;
    try {
      session.close();
      if (log.isDebugEnabled())
        log.debug("close hibernate session in openSession(Session session)");
    } catch (Throwable t) {
      log.error("Error closing hibernate session : " + t.getMessage(), t);
    }
    threadLocal_.set(null);
  }

  final public void closeSession() {
    Session s = threadLocal_.get();
    if (s != null)
      s.close();
    threadLocal_.set(null);
  }

  public Object findExactOne(Session session, String query, String id) throws Exception {
    Object res = session.createQuery(query).setString(0, id).uniqueResult();
    if (res == null) {
      throw new ObjectNotFoundException("Cannot find the object with id: " + id);
    }
    return res;
  }

  public Object findOne(Session session, String query, String id) throws Exception {
    List l = session.createQuery(query).setString(0, id).list();
    if (l.size() == 0) {
      return null;
    } else if (l.size() > 1) {
      throw new Exception("Expect only one object but found" + l.size());
    } else {
      return l.get(0);
    }
  }

  public Object findOne(Class clazz, Serializable id) throws Exception {
    Session session = openSession();
    Object obj = session.get(clazz, id);
    return obj;
  }

  public Object findOne(ObjectQuery q) throws Exception {
    Session session = openSession();
    List l = session.createQuery(q.getHibernateQuery()).list();
    if (l.size() == 0) {
      return null;
    } else if (l.size() > 1) {
      throw new Exception("Expect only one object but found" + l.size());
    } else {
      return l.get(0);
    }
  }

  public Object create(Object obj) throws Exception {
    Session session = openSession();
    session.save(obj);
    session.flush();
    return obj;
  }

  public Object update(Object obj) throws Exception {
    Session session = openSession();
    session.update(obj);
    session.flush();
    return obj;
  }

  public Object save(Object obj) throws Exception {
    Session session = openSession();
    session.merge(obj);
    session.flush();
    return obj;
  }

  public Object remove(Object obj) throws Exception {
    Session session = openSession();
    session.delete(obj);
    session.flush();
    return obj;
  }

  public Object remove(Class clazz, Serializable id) throws Exception {
    Session session = openSession();
    Object obj = session.get(clazz, id);
    session.delete(obj);
    session.flush();
    return obj;
  }

  public Object remove(Session session, Class clazz, Serializable id) throws Exception {
    Object obj = session.get(clazz, id);
    session.delete(obj);
    return obj;
  }

  public void startRequest(KernelContainer container) {

  }

  public void endRequest(KernelContainer container) {
    closeSession();
  }
}
