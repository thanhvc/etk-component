package org.etk.kernel.container.xml;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class ExoProperties extends LinkedHashMap<String, String> {
	public ExoProperties() {
	}

	public ExoProperties(int size) {
		super(size);
	}

	public String getProperty(String key) {
		return (String) get(key);
	}

	public void setProperty(String key, String value) {
		put(key, value);
	}

	public void addPropertiesFromText(String text) {
		String[] temp = text.split("\n");
		for (int i = 0; i < temp.length; i++) {
			temp[i] = temp[i].trim();
			if (temp[i].length() > 0) {
				String[] value = temp[i].split("=");
				if (value.length == 2)
					put(value[0].trim(), value[1].trim());
			}
		}
	}

	public String toText() {
		StringBuffer b = new StringBuffer();
		Set<Map.Entry<String, String>> set = entrySet();
		Iterator<Map.Entry<String, String>> i = set.iterator();
		while (i.hasNext()) {
			Map.Entry<String, String> entry = (Map.Entry<String, String>) i
					.next();
			b.append(entry.getKey()).append("=").append(entry.getValue())
					.append("\n");
		}
		return b.toString();
	}

	public String toXml() {
		StringBuffer b = new StringBuffer();
		b.append("<properties>");
		Set<Map.Entry<String, String>> set = entrySet();
		Iterator<Map.Entry<String, String>> i = set.iterator();
		while (i.hasNext()) {
			Map.Entry<String, String> entry = (Map.Entry<String, String>) i
					.next();
			b.append("<property key=\"").append(entry.getKey())
					.append("\" value=\"").append(entry.getValue())
					.append("\"/>");
		}
		b.append("</properties>");
		return b.toString();
	}
}
