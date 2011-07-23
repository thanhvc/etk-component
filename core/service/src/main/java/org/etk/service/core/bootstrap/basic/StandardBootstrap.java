package org.etk.service.core.bootstrap.basic;

import org.etk.kernel.bootstrap.AbstractBootstrap;
import org.etk.kernel.container.RootContainer;
import org.etk.kernel.container.configuration.ConfigurationManager;


public class StandardBootstrap extends AbstractBootstrap {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		RootContainer container = RootContainer.getInstance();
		ConfigurationManager cm = (ConfigurationManager) container.getComponentInstanceOfType(ConfigurationManager.class);
	}

}
