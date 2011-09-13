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
package org.etk.sandbox.entity.plugins.model.xml;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javolution.util.FastMap;

import org.etk.common.logging.Logger;
import org.jibx.runtime.BindingDirectory;
import org.jibx.runtime.IBindingFactory;
import org.jibx.runtime.IMarshallingContext;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Aug 26, 2011  
 */
public final class Configuration implements Cloneable {

  private static final Logger log = Logger.getLogger(Configuration.class);
  public static final String KERNEL_CONFIGURATION_1_0_URI = "http://www.exoplaform.org/xml/ns/kernel_1_0.xsd";
  
  /** The map which contains the Entities Map binding from entityDef.xml.*/
  private Map<String, Entity> entitiesMap = FastMap.newInstance();
  /** The map which contains the View Entities Map binding from entityDef.xml*/
  private Map<String, ViewEntity> viewsMap = FastMap.newInstance();
  private ArrayList<String> imports;
  
  /**
   * Adds the Entity to the EntityMap which contains the Entity objects 
   * to configure in the entitydef.xml
   *   
   * @param object
   */
  public void addEntity(Object object) {
    Entity entity = (Entity) object;
    String key = entity.getPackageName() + "." + entity.getEntityName();
    entitiesMap.put(key, entity);
  }
  
  /**
   * Adds the View Entity to the ViewMap which contains the View objects 
   * to configure in the entitydef.xml
   *   
   * @param object
   */
  public void addView(Object object) {
    ViewEntity entityView = (ViewEntity) object;
    String key = entityView.getPackageName() + "." + entityView.getViewName();
    viewsMap.put(key, entityView);
    
    //need to processing when view relate to provided Entity.
    
  }
  
  public boolean hasEntity() {
    return entitiesMap.size() > 0;
  }
  
  public boolean hasView() {
    return viewsMap.size() > 0;
  }
  
  /**
   * Gets the Entity Iterator for Binding.xml mapping
   * @return
   */
  public Iterator getEntityIterator() {
    return this.entitiesMap.values().iterator();
  }
  
  /**
   * Gets the View Iterator for Binding.xml mapping
   * @return
   */
  public Iterator getViewIterator() {
    return this.viewsMap.values().iterator();
  }
  
  public void addImport(String url) {
    if (imports == null)
      imports = new ArrayList<String>();
    imports.add(url);
  }

  public List getImports() {
    return imports;
  }
  
  /**
   * 
   * @return
   */
  public Collection getEntities() {
    return entitiesMap.values();
  }
  
  /**
   * 
   * @return
   */
  public Collection getViews() {
    return viewsMap.values();
  }

  public Entity getEntity(String packageName, String entityName) {
    String key = packageName + "." + entityName;
    return entitiesMap.get(key);
  }

  /**
   * Gets the Entity which uses the entityFullName = packageName + "." + entityName
   * @param entityFullName
   * @return
   */
  public Entity getEntity(String entityFullName) {
    return entitiesMap.get(entityFullName);
  }
  
  /**
   * Put all of the component, containerLifecyclePlugin, and componentLifecyclePlugin 
   * from other which was contained in other(different Configuration) 
   * to the current(Configuration).
   */
  public void mergeConfiguration(Configuration other) {
    this.entitiesMap.putAll(other.entitiesMap);
    this.viewsMap.putAll(other.viewsMap);
  }

  /**
   * Merge all the given configurations and return a safe copy of the result
   * 
   * @param configs the list of configurations to merge ordered by priority, the
   *          second configuration will override the configuration of the first
   *          one and so on.
   * @return the merged configuration
   */
  public static Configuration merge(Configuration... configs) {
    if (configs == null || configs.length == 0) {
      return null;
    }
    Configuration result = null;
    for (Configuration conf : configs) {
      if (conf == null) {
        // Ignore the null configuration
        continue;
      } else if (result == null) {
        try {
          // Initialize with the clone of the first non null configuration
          result = (Configuration) conf.clone();
        } catch (CloneNotSupportedException e) {
          log.warn("Could not clone the configuration", e);
          break;
        }
      } else {
        // The merge the current configuration with this new configuration
        result.mergeConfiguration(conf);
      }
    }
    return result;
  }

  /**
   * Dumps the configuration in XML format into the given {@link Writer}
   */
  public void toXML(Writer w) {
    try {
      IBindingFactory bfact = BindingDirectory.getFactory(Configuration.class);
      IMarshallingContext mctx = bfact.createMarshallingContext();
      mctx.setIndent(2);
      mctx.marshalDocument(this, "UTF-8", null, w);
    } catch (Exception e) {
      log.warn("Couldn't dump the runtime configuration in XML Format", e);
    }
  }

  /**
   * Dumps the configuration in XML format into a {@link StringWriter} and
   * returns the content
   */
  public String toXML() {
    StringWriter sw = new StringWriter();
    try {
      toXML(sw);
    } catch (Exception e) {
      log.warn("Cannot convert the configuration to XML format", e);
      return null;
    } finally {
      try {
        sw.close();
      } catch (IOException ignore) {
      }
    }
    return sw.toString();
  }

}
