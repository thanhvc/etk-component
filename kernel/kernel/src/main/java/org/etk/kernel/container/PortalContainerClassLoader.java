package org.etk.kernel.container;

import java.util.Set;


class PortalContainerClassLoader extends UnifiedClassLoader {

	/**
	 * The related portal container
	 */
	private final ApplicationContainer container;

	PortalContainerClassLoader(ApplicationContainer container) {
		super(getClassLoaders(container));
		this.container = container;
	}

	/**
	 * Retrieves the list of all the {@link ClassLoader} that are associated to
	 * the given portal container
	 */
	private static ClassLoader[] getClassLoaders(ApplicationContainer container) {
		final Set<WebAppInitContext> contexts = container.getWebAppInitContexts();
		final ClassLoader[] cls = new ClassLoader[contexts.size()];
		int i = 0;
		for (WebAppInitContext ctx : contexts) {
			cls[i++] = ctx.getWebappClassLoader();
		}
		return cls;
	}

	/**
	 * {@inheritDoc}
	 */
	protected ClassLoader[] getClassLoaders() {
		return getClassLoaders(container);
	}
}
