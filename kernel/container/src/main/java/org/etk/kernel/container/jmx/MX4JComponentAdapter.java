package org.etk.kernel.container.jmx;

import java.lang.reflect.Method;
import java.util.List;

import org.etk.common.logging.Logger;
import org.etk.kernel.container.KernelContainer;
import org.etk.kernel.container.component.ComponentLifecycle;
import org.etk.kernel.container.component.ComponentPlugin;
import org.etk.kernel.container.configuration.ConfigurationManager;
import org.etk.kernel.container.xml.Component;
import org.etk.kernel.container.xml.ExternalComponentPlugins;
import org.etk.kernel.container.xml.InitParams;
import org.picocontainer.PicoContainer;
import org.picocontainer.defaults.AbstractComponentAdapter;

@SuppressWarnings("unchecked")
public class MX4JComponentAdapter extends AbstractComponentAdapter {
	/**
	 * Serial Version ID
	 */
	private static final long serialVersionUID = -9001193588034229411L;

	private volatile Object instance_;

	private Logger log = Logger.getLogger(MX4JComponentAdapter.class);

	public MX4JComponentAdapter(Object key, Class implementation) {
		super(key, implementation);
	}
 
	
	public Object getComponentInstance(PicoContainer container) {
		if (instance_ != null)
			return instance_;

		//
		KernelContainer kernelContainer = (KernelContainer) container;
		Component component = null;
		ConfigurationManager manager;
		String componentKey;
		try {
			InitParams params = null;
			boolean debug = false;
			synchronized (this) {
				// Avoid to create duplicate instances if it is called at the
				// same time by several threads
				if (instance_ != null)
					return instance_;
				// Get the component key
				Object key = getComponentKey();
				if (key instanceof String)
					componentKey = (String) key;
				else
					componentKey = ((Class) key).getName();
				
				//Gets the Component configuration information which stores in the ConfigurationManager class.
				manager = (ConfigurationManager) kernelContainer.getComponentInstanceOfType(ConfigurationManager.class);
				component = manager.getComponent(componentKey);
				if (component != null) {
					params = component.getInitParams();
					debug = component.getShowDeployInfo();
				}
				// Please note that we cannot fully initialize the Object
				// "instance_" before releasing other
				// threads because it could cause StackOverflowError due to
				// recursive calls
				instance_ = kernelContainer.createComponent(getComponentImplementation(), params);
			}

			
			if (debug) 
			  log.debug("==> created  component : " + instance_);
			//processing the add ComponentPlugin for current instance_
			if (component != null && component.getComponentPlugins() != null) {
				addComponentPlugin(debug, instance_, component.getComponentPlugins(), kernelContainer);
			}
			//processing the add ExternalComponentPlugin for current instance_
			ExternalComponentPlugins ecplugins = manager.getConfiguration().getExternalComponentPlugins(componentKey);
			if (ecplugins != null) {
				addComponentPlugin(debug, instance_, ecplugins.getComponentPlugins(), kernelContainer);
			}
			// check if component implement the ComponentLifecycle
			if (instance_ instanceof ComponentLifecycle) {
				ComponentLifecycle lc = (ComponentLifecycle) instance_;
				lc.initComponent(kernelContainer);
			}
		} catch (Exception ex) {
			String msg = "Cannot instantiate component " + getComponentImplementation();
			if (component != null) {
				msg = "Cannot instantiate component key=" + component.getKey()
						+ " type=" + component.getType() + " found at "
						+ component.getDocumentURL();
			}
			throw new RuntimeException(msg, ex);
		}

		return instance_;
	}

	/**
	 * Executes the add ComponentPlugin or ExternalComponentPlugin into the current instance_.
	 * @param debug
	 * @param component
	 * @param plugins
	 * @param container
	 * @throws Exception
	 */
	private void addComponentPlugin(boolean debug, Object component, List<org.etk.kernel.container.xml.ComponentPlugin> plugins, KernelContainer container) throws Exception {
		if (plugins == null)
			return;
		for (org.etk.kernel.container.xml.ComponentPlugin plugin : plugins) {

			try {
				Class clazz = Class.forName(plugin.getType());
				ComponentPlugin cplugin = (ComponentPlugin) container.createComponent(clazz, plugin.getInitParams());
				cplugin.setName(plugin.getName());
				cplugin.setDescription(plugin.getDescription());
				clazz = component.getClass();

				Method m = getSetMethod(clazz, plugin.getSetMethod());
				Object[] params = { cplugin };
				m.invoke(component, params);
				if (debug)
					log.debug("==> add component plugin: " + cplugin);

				cplugin.setName(plugin.getName());
				cplugin.setDescription(plugin.getDescription());
			} catch (Exception ex) {
				
        log.error("Failed to instanciate plugin " + plugin.getName() + "for component " + component + ": " + ex.getMessage(), ex);
			}
		}
	}

	//TODO needs to improvement here for performance.
	private Method getSetMethod(Class clazz, String name) {
	  //TODO Improvement here 
		Method[] methods = clazz.getMethods();
		for (Method m : methods) {
			if (name.equals(m.getName())) {
				Class[] types = m.getParameterTypes();
				if (types != null && types.length == 1 && ComponentPlugin.class.isAssignableFrom(types[0])) {
					return m;
				}
			}
		}
		return null;
	}

	public void verify(PicoContainer container) {
	  
	}

}
