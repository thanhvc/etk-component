package org.etk.kernel.container;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.etk.common.logging.Logger;
import org.etk.common.utils.PropertyManager;
import org.etk.kernel.container.configuration.ConfigurationManager;
import org.etk.kernel.container.management.ManageableContainer;
import org.etk.kernel.container.util.ContainerUtil;
import org.etk.kernel.container.xml.Configuration;
import org.etk.kernel.container.xml.InitParams;
import org.picocontainer.PicoContainer;
import org.picocontainer.defaults.ComponentAdapterFactory;

public class KernelContainer extends ManageableContainer {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -8068506531004854036L;

	/**
	 * Returns an unmodifable set of profiles defined by the value returned by
	 * invoking {@link PropertyManager#getProperty(String)} with the
	 * {@link org.exoplatform.commons.utils.PropertyManager#RUNTIME_PROFILES}
	 * property.
	 * 
	 * @return the set of profiles
	 */
	public static Set<String> getProfiles() {
		//
		Set<String> profiles = new HashSet<String>();

		// Obtain profile list by runtime properties
		String profileList = PropertyManager.getProperty(PropertyManager.RUNTIME_PROFILES);
		if (profileList != null) {
			for (String profile : profileList.split(",")) {
				profiles.add(profile.trim());
			}
		}

		//
		return Collections.unmodifiableSet(profiles);
	}

	static Logger log = Logger.getLogger(KernelContainer.class);

	private Map<String, ComponentLifecyclePlugin> componentLifecylePlugin = new HashMap<String, ComponentLifecyclePlugin>();

	private List<ContainerLifecyclePlugin> containerLifecyclePlugin_ = new ArrayList<ContainerLifecyclePlugin>();

	protected KernelContainerContext context;

	protected PicoContainer parent;
	
	public KernelContainer() {
		context = new KernelContainerContext(this);
		context.setName(this.getClass().getName());
		registerComponentInstance(context);
		this.parent = null;
	}

	public KernelContainer(PicoContainer parent) {
		super(parent);
		context = new KernelContainerContext(this);
		context.setName(this.getClass().getName());
		registerComponentInstance(context);
		this.parent = parent;
	}

	public KernelContainer(ComponentAdapterFactory factory, PicoContainer parent) {
		super(factory, parent);
		context = new KernelContainerContext(this);
		context.setName(this.getClass().getName());
		registerComponentInstance(context);
		this.parent = parent;
	}

	public KernelContainerContext getContext() {
		return context;
	}

	/**
	 * @return the name of the plugin if it is not empty, the FQN of the plugin
	 *         otherwise
	 */
	private static String getPluginName(ContainerLifecyclePlugin plugin) {
		String name = plugin.getName();
		if (name == null || name.length() == 0) {
			name = plugin.getClass().getName();
		}
		return name;
	}

	/**
	 * Explicit calls are not allowed anymore
	 */
	@Deprecated
	public void initContainer() throws Exception {
	}

	private void initContainerInternal() {
		ConfigurationManager manager = (ConfigurationManager) getComponentInstanceOfType(ConfigurationManager.class);
		ContainerUtil.addContainerLifecyclePlugin(this, manager);
		ContainerUtil.addComponentLifecyclePlugin(this, manager);
		for (ContainerLifecyclePlugin plugin : containerLifecyclePlugin_) {
			try {
				plugin.initContainer(this);
			} catch (Exception e) {
				log.warn("An error occurs with the ContainerLifecyclePlugin '" + getPluginName(plugin) + "'", e);
			}
		}
		ContainerUtil.addComponents(this, manager);
	}

	@Override
	public void dispose() {
		destroyContainerInternal();
		super.dispose();
	}

	/**
	 * Starts the container
	 * 
	 * @param init
	 *            indicates if the container must be initialized first
	 */
	public void start(boolean init) {
		if (init) {
			// Initialize the container first
			initContainerInternal();
		}
		start();
	}

	@Override
	public void start() {
		super.start();
		startContainerInternal();
	}

	@Override
	public void stop() {
		stopContainerInternal();
		super.stop();
	}

	/**
	 * Explicit calls are not allowed anymore
	 */
	@Deprecated
	public void startContainer() throws Exception {
	}

	private void startContainerInternal() {
		for (ContainerLifecyclePlugin plugin : containerLifecyclePlugin_) {
			try {
				plugin.startContainer(this);
			} catch (Exception e) {
				log.warn("An error occurs with the ContainerLifecyclePlugin '" + getPluginName(plugin) + "'", e);
			}
		}
	}

	/**
	 * Explicit calls are not allowed anymore
	 */
	@Deprecated
	public void stopContainer() throws Exception {
	}

	private void stopContainerInternal() {
		for (ContainerLifecyclePlugin plugin : containerLifecyclePlugin_) {
			try {
				plugin.stopContainer(this);
			} catch (Exception e) {
				log.warn("An error occurs with the ContainerLifecyclePlugin '" + getPluginName(plugin) + "'", e);
			}
		}
	}

	/**
	 * Explicit calls are not allowed anymore
	 */
	@Deprecated
	public void destroyContainer() throws Exception {
	}

	private void destroyContainerInternal() {
		for (ContainerLifecyclePlugin plugin : containerLifecyclePlugin_) {
			try {
				plugin.destroyContainer(this);
			} catch (Exception e) {
				log.warn("An error occurs with the ContainerLifecyclePlugin '" + getPluginName(plugin) + "'", e);
			}
		}
	}

	public void addComponentLifecylePlugin(ComponentLifecyclePlugin plugin) {
		List<String> list = plugin.getManageableComponents();
		for (String component : list)
			this.componentLifecylePlugin.put(component, plugin);
	}

	public void addContainerLifecylePlugin(ContainerLifecyclePlugin plugin) {
		containerLifecyclePlugin_.add(plugin);
	}

	public <T> T createComponent(Class<T> clazz) throws Exception {
		return createComponent(clazz, null);
	}

	public <T> T createComponent(Class<T> clazz, InitParams params) throws Exception {
		
		if (log.isDebugEnabled())
			log.debug(clazz.getName() + " " + ((params != null) ? params : "") + " added to " + getContext().getName());
			
		Constructor<?>[] constructors = new Constructor<?>[0];
		try {
			constructors = ContainerUtil.getSortedConstructors(clazz);
		} catch (NoClassDefFoundError err) {
			throw new Exception("Cannot resolve constructor for class " + clazz.getName(), err);
		}
		Class<?> unknownParameter = null;
		for (int k = 0; k < constructors.length; k++) {
			Constructor<?> constructor = constructors[k];
			Class<?>[] parameters = constructor.getParameterTypes();
			Object[] args = new Object[parameters.length];
			boolean satisfied = true;
			for (int i = 0; i < args.length; i++) {
				if (parameters[i].equals(InitParams.class)) {
					args[i] = params;
				} else {
					args[i] = getComponentInstanceOfType(parameters[i]);
					if (args[i] == null) {
						satisfied = false;
						unknownParameter = parameters[i];
						break;
					}
				}
			}
			if (satisfied)
				return clazz.cast(constructor.newInstance(args));
		}
		throw new Exception("Cannot find a satisfying constructor for " + clazz + " with parameter " + unknownParameter);
	}

	/**
	 * Gets the {@link ConfigurationManager} from the given {@link ExoContainer}
	 * if it exists, then returns the nested {@link Configuration} otherwise it
	 * returns <code>null</code>
	 */
	protected Configuration getConfiguration() {
		ConfigurationManager cm = (ConfigurationManager) getComponentInstanceOfType(ConfigurationManager.class);
		return cm == null ? null : cm.getConfiguration();
	}
}