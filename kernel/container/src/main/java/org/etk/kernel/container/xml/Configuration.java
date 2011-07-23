package org.etk.kernel.container.xml;
        
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.etk.common.logging.Logger;
import org.jibx.runtime.BindingDirectory;
import org.jibx.runtime.IBindingFactory;
import org.jibx.runtime.IMarshallingContext;
/**
 * Define the rule for root element which is configured in the binding.xml. 
 * JIBX library base on the binding.xml file for mapping to the Java Object.
 * 
 * Example: The content which configures in the binding.xml
 * 
 * <!-- configuration object mapping -->
  <mapping name="configuration" class="org.etk.kernel.core.container.xml.Configuration">
    <collection item-type="org.etk.kernel.core.container.xml.ContainerLifecyclePlugin" usage="optional"
                add-method="addContainerLifecyclePlugin" iter-method="getContainerLifecyclePluginIterator"
                test-method="hasContainerLifecyclePlugin"/>

    <collection item-type="org.etk.kernel.core.container.xml.ComponentLifecyclePlugin" usage="optional"
                add-method="addComponentLifecyclePlugin" iter-method="getComponentLifecyclePluginIterator"
                test-method="hasComponentLifecyclePlugin"/>

    <collection item-type="org.etk.kernel.core.container.xml.Component" usage="optional"
                add-method="addComponent" iter-method="getComponentIterator"
                test-method="hasComponent"/>

    <collection item-type="org.etk.kernel.core.container.xml.ExternalComponentPlugins" usage="optional"
                add-method="addExternalComponentPlugins" iter-method="getExternalComponentPluginsIterator"
                test-method="hasExternalComponentPlugins"/>
    <collection item-type="java.lang.String" field="imports_" usage="optional">
      <value name="import" style="element"/>
    </collection>

    <collection item-type="java.lang.String"  field="removeConfiguration_" usage="optional">
      <value name="remove-configuration" style="element"/>
    </collection>
  </mapping> 
 * 
 * 
 * @author thanh_vucong
 *
 */
public class Configuration implements Cloneable {

  private static final Logger log = Logger.getLogger(Configuration.class);
  
  public static final String KERNEL_CONFIGURATION_1_0_URI = "http://www.exoplaform.org/xml/ns/kernel_1_0.xsd";

  private Map<String, ContainerLifecyclePlugin> containerLifecyclePlugin    = new HashMap<String, ContainerLifecyclePlugin>();

  private Map<String, ComponentLifecyclePlugin> componentLifecyclePlugin    = new HashMap<String, ComponentLifecyclePlugin>();

  private Map<String, Component>                component                    = new HashMap<String, Component>();

  private Map<String, ExternalComponentPlugins> externalComponentPlugins    = new HashMap<String, ExternalComponentPlugins>();

  private ArrayList<String>                     imports;

  private ArrayList<String>                     removeConfiguration_;

  public Collection<ContainerLifecyclePlugin> getContainerLifecyclePlugins() {
    List<ContainerLifecyclePlugin> plugins = new ArrayList<ContainerLifecyclePlugin>(this.containerLifecyclePlugin.values());
    Collections.sort(plugins);
    return plugins;
  }

  /**
   * <collection item-type="org.etk.kernel.core.container.xml.ContainerLifecyclePlugin" usage="optional"
   * add-method="addContainerLifecyclePlugin" iter-method="getContainerLifecyclePluginIterator"
   * test-method="hasContainerLifecyclePlugin"/>
   * 
   * @param object Type of org.etk.kernel.core.container.xml.ContainerLifecyclePlugin
   */
  public void addContainerLifecyclePlugin(Object object) {
    ContainerLifecyclePlugin plugin = (ContainerLifecyclePlugin) object;
    String key = plugin.getType();
    this.containerLifecyclePlugin.put(key, plugin);
  }

  /**
   * Retrieving the ContainerLifecyclePlugin().getItrator
   * @return
   */
  public Iterator<ContainerLifecyclePlugin> getContainerLifecyclePluginIterator() {
    return getContainerLifecyclePlugins().iterator();
  }

  /**
   * Determines the containerLifecyclePlugin has size > 0.
   * 
   * @return containerLifecyclePlugin
   */
  public boolean hasContainerLifecyclePlugin() {
    return this.containerLifecyclePlugin.size() > 0;
  }

  public Collection getComponentLifecyclePlugins() {
    return this.componentLifecyclePlugin.values();
  }

  /**
   * This method shows you how to configure for xml mapping.
   * <collection item-type="org.etk.kernel.core.container.xml.ComponentLifecyclePlugin" usage="optional"
     add-method="addComponentLifecyclePlugin" iter-method="getComponentLifecyclePluginIterator"
     test-method="hasComponentLifecyclePlugin"/>
                
   * @param object
   */
  public void addComponentLifecyclePlugin(Object object) {
    ComponentLifecyclePlugin plugin = (ComponentLifecyclePlugin) object;
    String key = plugin.getClass().getName();
    this.componentLifecyclePlugin.put(key, plugin);
  }

  public Iterator getComponentLifecyclePluginIterator() {
    return this.componentLifecyclePlugin.values().iterator();
  }

  public boolean hasComponentLifecyclePlugin() {
    return this.componentLifecyclePlugin.size() > 0;
  }

  public Component getComponent(String s) {
    return component.get(s);
  }

  /**
   *  <collection item-type="org.etk.kernel.core.container.xml.Component" usage="optional"
   *  add-method="addComponent" iter-method="getComponentIterator"
   *  test-method="hasComponent"/>
   *             
   * @param object
   */
  public void addComponent(Object object) {
    Component comp = (Component) object;
    String key = comp.getKey();
    if (key == null) {
      key = comp.getType();
      comp.setKey(key);
    }
    component.put(key, comp);
  }

  /**
   * 
   * @return
   */
  public Collection getComponents() {
    return component.values();
  }

  /**
   * Provides to the binding mechanism to get element via iterator.
   * 
   * @return Iterator in the Component collection.
   */
  public Iterator getComponentIterator() {
    return component.values().iterator();
  }

  /**
   * Provides the method to binding.xml configuration.
   * test-method="hasComponent"
   * 
   * @return
   */
  public boolean hasComponent() {
    return component.size() > 0;
  }

  public ExternalComponentPlugins getExternalComponentPlugins(String s) {
    return externalComponentPlugins.get(s);
  }

  public void addExternalComponentPlugins(Object o) {
    if (o instanceof ExternalComponentPlugins) {
      ExternalComponentPlugins eps = (ExternalComponentPlugins) o;

      // Retrieve potential existing external component
      // plugins with same target component.
      String targetComponent = eps.getTargetComponent();
      ExternalComponentPlugins foundExternalComponentPlugins = (ExternalComponentPlugins) externalComponentPlugins.get(targetComponent);

      if (foundExternalComponentPlugins == null) {
        // No external component plugins found. Create a new entry.
        externalComponentPlugins.put(targetComponent, eps);
      } else {
        // Found external component plugins. Add the specified one.
        foundExternalComponentPlugins.merge(eps);
      }
    }
  }

  public Iterator getExternalComponentPluginsIterator() {
    return externalComponentPlugins.values().iterator();
  }

  public boolean hasExternalComponentPlugins() {
    return externalComponentPlugins.size() > 0;
  }

  public void addImport(String url) {
    if (imports == null)
      imports = new ArrayList<String>();
    imports.add(url);
  }

  public List getImports() {
    return imports;
  }

  public void addRemoveConfiguration(String type) {
    if (removeConfiguration_ == null)
      removeConfiguration_ = new ArrayList<String>();
    removeConfiguration_.add(type);
  }

  public List getRemoveConfiguration() {
    return removeConfiguration_;
  }

  public void removeConfiguration(String type) {
    component.remove(type);
  }

  // -------------------------end new mapping configuration--------------------

  /**
   * Put all of the component, containerLifecyclePlugin, and componentLifecyclePlugin 
   * from other which was contained in other(different Configuration) 
   * to the current(Configuration).
   */
  public void mergeConfiguration(Configuration other) {
    component.putAll(other.component);
    this.componentLifecyclePlugin.putAll(other.componentLifecyclePlugin);
    this.containerLifecyclePlugin.putAll(other.containerLifecyclePlugin);

    // merge the external plugins
    Iterator i = other.externalComponentPlugins.values().iterator();
    while (i.hasNext()) {
      addExternalComponentPlugins(i.next());
    }

    if (other.getRemoveConfiguration() == null)
      return;
    if (removeConfiguration_ == null)
      removeConfiguration_ = new ArrayList<String>();
    removeConfiguration_.addAll(other.getRemoveConfiguration());
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
