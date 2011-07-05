package org.etk.kernel.container.xml.object;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class XMLMap {

	private ArrayList listmap = new ArrayList();

	private String type;

	public XMLMap() {

	}

	public XMLMap(Map map) throws Exception {
		Iterator i = map.entrySet().iterator();
		while (i.hasNext()) {
			Map.Entry entry = (Map.Entry) i.next();
			Object key = entry.getKey();
			Object value = entry.getValue();
			// S ystem.out.println("key: " + key + ", value: " + value) ;
			if (key == null || value == null) {
				throw new RuntimeException("key: " + key + ", value: " + value
						+ " cannot be null");
			}
			listmap.add(new XMLEntry(key, value));
		}
		type = map.getClass().getName();
	}

	public String getType() {
		return type;
	}

	public void setType(String s) {
		type = s;
	}

	public Iterator getEntryIterator() {
		return listmap.iterator();
	}

	public Map getMap() throws Exception {
		Class clazz = Class.forName(type);
		Map map = (Map) clazz.newInstance();
		for (int i = 0; i < listmap.size(); i++) {
			XMLEntry entry = (XMLEntry) listmap.get(i);
			XMLBaseObject key = entry.getKey();
			XMLBaseObject value = entry.getValue();
			map.put(key.getObjectValue(), value.getObjectValue());
		}
		return map;
	}
}
