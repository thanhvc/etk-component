package org.etk.kernel.container.xml.object;

public class XMLEntry {

	private XMLBaseObject key;

	private XMLBaseObject value;

	public XMLEntry() {
	}

	public XMLEntry(Object key, Object val) throws Exception {
		key = new XMLKey(null, key);
		value = new XMLValue(null, val);
	}

	public XMLBaseObject getKey() {
		return key;
	}

	public void setKey(XMLBaseObject key) {
		this.key = key;
	}

	public XMLBaseObject getValue() {
		return value;
	}

	public void setValue(XMLBaseObject value) {
		this.value = value;
	}
}
