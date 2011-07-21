package org.etk.kernel.container.xml;

import java.io.IOException;
import java.net.URL;

import org.jibx.runtime.IMarshallingContext;

public class ConfigurationMarshallerUtil {

	/**
	 * This method adds the given {@link URL} as comment to XML content.
	 */
	static void addURLToContent(URL source, IMarshallingContext ictx) {
		try {
			ictx.getXmlWriter().writeComment(" Loaded from '" + source + "' ");
		} catch (IOException e) {
			// log.warn("Could not add the source into the XML document", e);
		}
	}
}
