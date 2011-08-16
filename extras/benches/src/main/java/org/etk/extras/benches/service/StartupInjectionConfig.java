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
package org.etk.extras.benches.service;

import java.util.Iterator;

import org.etk.common.logging.Logger;
import org.etk.kernel.container.ApplicationContainer;
import org.etk.kernel.container.component.RequestLifeCycle;
import org.etk.kernel.container.xml.InitParams;
import org.etk.kernel.container.xml.PropertiesParam;
import org.etk.kernel.container.xml.Property;
import org.picocontainer.Startable;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Aug 3, 2011  
 */
public class StartupInjectionConfig  implements Startable {
  private static final Logger LOG = Logger.getLogger(StartupInjectionConfig.class);

  private long fooAmount = 0;
  private long barAmount = 0;
  private long mulThreading = 0;
 
  private DataInjector     injector;

  /**
   * Example init-params :
   *
   * <pre>
   * &lt;init-params&gt;
   *   &lt;properties-param&gt;
   *     &lt;name&gt;inject.conf&lt;/name&gt;
   *     &lt;property name="foo" value="100"/&gt;
   *     &lt;property name="bar" value="100"/&gt;
   *   &lt;/properties-param&gt;
   * &lt;/init-params&gt;
   * </pre>
   *
   * @param params
   * @param injector
   */
  public StartupInjectionConfig(InitParams params, DataInjector injector) {
    this.injector = injector;
    PropertiesParam props = params.getPropertiesParam("inject.conf");
    if (props != null) {
      Iterator<Property> it = props.getPropertyIterator();
      while (it.hasNext()) {
        Property property = (Property) it.next();
        String name = property.getName();
        String value = property.getValue();
        Long longValue = longValue(name, value);
        if ("foo".equals(name)) {
          fooAmount = longValue;
        } else if ("bar".equals(name)) {
          barAmount = longValue;
        } else if ("multhread".equals(name)) {
          mulThreading = longValue;
        }
      }
    }
  }

  private long longValue(String property, String value) {
    try {
      if (value != null) {
        return Long.valueOf(value);
      }
    } catch (NumberFormatException e) {
      LOG.warn("Long number expected for property " + property);
    }
    return 0;
  }

  public void start() {
    try {

      //
      RequestLifeCycle.begin(ApplicationContainer.getInstance());
      inject();

    } catch (Exception e) {
      LOG.error("Data injeciton failed", e);

    } finally {
      RequestLifeCycle.end();
    }

  }

  private void inject() {
    LOG.info("starting...");
    boolean nothingWasDone = true;
    if (fooAmount > 0) {
      nothingWasDone = false;
      LOG.info("\t> about to inject " + fooAmount + " people.");
      injector.generateFoo(fooAmount, mulThreading);
    }
    if (barAmount > 0) {
      nothingWasDone = false;
      LOG.info("\t> about to inject " + barAmount + " connections.");
      injector.generateBar(barAmount);
    }
    
    if (nothingWasDone) {
      LOG.info("nothing to inject.");
    }
  }

  public void stop() {
    ;// nothing
  }

}