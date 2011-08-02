package org.etk.kernel.container;

import java.util.HashMap;
import java.util.Set;

import org.etk.common.logging.Logger;


@SuppressWarnings("serial")
public final class KernelContainerContext implements java.io.Serializable {

	private static ThreadLocal<KernelContainer> currentContainer = new ThreadLocal<KernelContainer>();

	private static volatile KernelContainer topContainer;

	private HashMap<String, Object> attributes = new HashMap<String, Object>();

	private KernelContainer container;

	private String name;

	private static final Logger log = Logger.getLogger(KernelContainerContext.class);

	public KernelContainerContext(KernelContainer container) {
		this.container = container;
	}

	public KernelContainer getContainer() {
		return container;
	}

	/**
	 * @return if the embedded container is a {@link PortalContainer}, it will
	 *         return the name the portal container otherwise it will return
	 *         <code>null</code>
	 */
	public String getPortalContainerName() {
		if (container instanceof ApplicationContainer) {
			return ((ApplicationContainer) container).getName();
		}
		return null;
	}

	/**
	 * @return if the embedded container is a {@link PortalContainer}, it will
	 *         return the name of the rest context related to the portal
	 *         container otherwise it will return the default name
	 */
	public String getRestContextName() {
		if (container instanceof ApplicationContainer) {
			return ((ApplicationContainer) container).getRestContextName();
		}
		return ApplicationContainer.DEFAULT_REST_CONTEXT_NAME;
	}

	/**
	 * @return if the embedded container is a {@link PortalContainer}, it will
	 *         return the name of the realm related to the portal container
	 *         otherwise it will return the default name
	 */
	public String getRealmName() {
		if (container instanceof ApplicationContainer) {
			return ((ApplicationContainer) container).getRealmName();
		}
		return ApplicationContainer.DEFAULT_REALM_NAME;
	}

	/**
	 * @return if the embedded container is a {@link PortalContainer}, it will
	 *         return the value of the setting related to the portal container
	 *         otherwise it will return <code>null</code>
	 */
	public Object getSetting(String settingName) {
		if (container instanceof ApplicationContainer) {
			return ((ApplicationContainer) container).getSetting(settingName);
		}
		return null;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public static KernelContainer getTopContainer() {
		if (topContainer == null)
			topContainer = RootContainer.getInstance();
		return topContainer;
	}

	static void setTopContainer(KernelContainer cont) {
		if (topContainer != null && cont != null)
			throw new IllegalStateException("Two top level containers created, but must be only one.");
		log.info("Set the top container in its context");
		topContainer = cont;
	}

	public static KernelContainer getCurrentContainer() {
		KernelContainer container = currentContainer.get();
		if (container == null)
			container = getTopContainer();
		return container;
	}

	public static KernelContainer getCurrentContainerIfPresent() {
		KernelContainer container = currentContainer.get();
		if (container == null)
			return topContainer;
		return container;
	}

	public static void setCurrentContainer(KernelContainer instance) {
		currentContainer.set(instance);
	}

	public static KernelContainer getContainerByName(String name) {
		KernelContainerContext containerContext = topContainer.getContext();
		String name1 = containerContext.getName();
		if (name1.equals(name))
			return topContainer;
		return (KernelContainer) topContainer.getComponentInstance(name);
	}

	public Set<String> getAttributeNames() {
		return attributes.keySet();
	}

	public Object getAttribute(String name) {
		return attributes.get(name);
	}

	public void setAttribute(String name, Object value) {
		attributes.put(name, value);
	}
}