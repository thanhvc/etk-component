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
package org.etk.kernel.management.jmx;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.management.InstanceNotFoundException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.modelmbean.ModelMBeanInfo;

import org.etk.common.logging.Logger;
import org.etk.kernel.management.jmx.annotations.NameTemplate;
import org.etk.kernel.management.spi.ManagedResource;
import org.etk.kernel.management.spi.ManagementProvider;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Jul 28, 2011  
 */
public class JMXManagementProvider implements ManagementProvider {

  /**
   * The logger
   */
  private static final Logger LOG = Logger.getLogger(JMXManagementProvider.class);

  /** . */
  private final MBeanServer   server;

  public JMXManagementProvider() {
    this(MBeanServerFactory.createMBeanServer());
  }

  public JMXManagementProvider(MBeanServer server) {
    this.server = server;
  }

  /**
   * MBeanServer to add the Component to management.
   */
  public Object manage(ManagedResource context) {
    ExoModelMBean mbean = null;
    try {
      ExoMBeanInfoBuilder infoBuilder = new ExoMBeanInfoBuilder(context.getMetaData());
      ModelMBeanInfo info = infoBuilder.build();
      mbean = new ExoModelMBean(context, context.getResource(), info);
    } catch (Exception e) {
      LOG.warn("Could not create the ExoModelMBean for the class "
                   + (context == null ? null : (context.getResource() == null ? null : context.getResource().getClass())),e);
    }

    //
    if (mbean != null) {
      ObjectName on = null;
      PropertiesInfo oni = PropertiesInfo.resolve(context.getResource().getClass(), NameTemplate.class);
      if (oni != null) {
        try {
          Map<String, String> foo = oni.resolve(context.getResource());
          on = JMX.createObjectName("exo", foo);
        } catch (MalformedObjectNameException e) {
          LOG.warn("Could not create the ObjectName for the class " + context.getResource().getClass(), e);
        }
      }

      if (on != null) {
        // Merge with the container hierarchy context
        try {
          Map<String, String> props = new LinkedHashMap<String, String>();

          // Merge scoping properties
          List<MBeanScopingData> list = context.getScopingData(MBeanScopingData.class);
          // Read in revert order because wee received list of parents in upward
          // order
          for (int i = list.size(); i > 0; i--) {
            MBeanScopingData scopingData = list.get(i - 1);
            props.putAll(scopingData);
          }

          // Julien : I know it's does not look great but it's necessary
          // for compiling under Java 5 and Java 6 properly. The methods
          // ObjectName#getKeyPropertyList() returns an Hashtable with Java 5
          // and a Hashtable<String, String> with Java 6.
          for (Object o : on.getKeyPropertyList().entrySet()) {
            Map.Entry entry = (Map.Entry) o;
            String key = (String) entry.getKey();
            String value = (String) entry.getValue();
            props.put(key, value);
          }

          //
          on = JMX.createObjectName(on.getDomain(), props);

          //
          attemptToRegister(on, mbean);

          //
          return on;
        } catch (MalformedObjectNameException e) {
          LOG.warn("Could not register the MBean for the class " + context.getResource().getClass(), e);
        }
      }
    }

    //
    return null;
  }

  /**
   *  Register a component
   *  
   * @param name
   * @param mbean
   */
  private void attemptToRegister(final ObjectName name, final Object mbean) {
    synchronized (server) {
      if (server.isRegistered(name)) {
        if (LOG.isTraceEnabled()) {
          LOG.trace("The MBean '" + name + " has already been registered, it will be unregistered and then re-registered");
        }
        try {
          server.unregisterMBean(name);
        } catch (Exception e) {
          throw new RuntimeException("Failed to unregister MBean '" + name + " due to " + e.getMessage(), e);
        }
      }
      try {
        server.registerMBean(mbean, name);
      } catch (Exception e) {
        throw new RuntimeException("Failed to register MBean '" + name + " due to " + e.getMessage(), e);
      }
    }
  }

  public void unmanage(Object key) {
    final ObjectName name = (ObjectName) key;
    try {
        server.unregisterMBean(name);
    } catch (InstanceNotFoundException e) {
      LOG.warn("Could not unregister the MBean " + name, e);
    } catch (MBeanRegistrationException e) {
      LOG.warn("Could not unregister the MBean " + name, e);
    }
  }
}
