package org.etk.kernel.container.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Properties;

public class PropertiesLoader {

	private static class LinkedProperties extends Properties {

		/** A list that contains each element at most once. */
		private LinkedHashMap<String, String> list = new LinkedHashMap<String, String>();

		@Override
		public Object put(Object key, Object value) {
			if (list.containsKey(key)) {
				list.remove(key);
			}
			list.put((String) key, (String) value);
			return super.put(key, value);
		}

		@Override
		public Object remove(Object key) {
			list.remove(key);
			return super.remove(key);
		}

	}

	public static LinkedHashMap<String, String> load(InputStream in)
			throws IOException {
		LinkedProperties props = new LinkedProperties();
		props.load(in);
		return props.list;
	}

	public static LinkedHashMap<String, String> loadFromXML(InputStream in)
			throws IOException {
		LinkedProperties props = new LinkedProperties();
		props.loadFromXML(in);
		return props.list;
	}
}