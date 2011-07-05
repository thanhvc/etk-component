package org.etk.kernel.container.xml;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class PropertiesParam extends Parameter {
	private ExoProperties properties = new ExoProperties();

	public ExoProperties getProperties() {
		return properties;
	}

	public String getProperty(String name) {
		return properties.getProperty(name);
	}

	public void setProperty(String name, String value) {
		properties.setProperty(name, value);
	}

	public void addProperty(Object value) {
		Property property = (Property) value;
		properties.put(property.getName(), property.getValue());
	}

	public Iterator<Property> getPropertyIterator() {
		List<Property> list = new ArrayList<Property>();
		Iterator<Map.Entry<String, String>> i = properties.entrySet()
				.iterator();
		while (i.hasNext()) {
			Map.Entry<String, String> entry = (Map.Entry<String, String>) i
					.next();
			list.add(new Property((String) entry.getKey(), (String) entry
					.getValue()));
		}
		return list.iterator();
	}

	public String toString() {
		return properties.toString();
	}
}
