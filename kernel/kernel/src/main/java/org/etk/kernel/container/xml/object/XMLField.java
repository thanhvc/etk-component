package org.etk.kernel.container.xml.object;

public class XMLField extends XMLBaseObject {

	private String name;

	public XMLField() {

	}

	public XMLField(String fieldName, Class type, Object val) throws Exception {
		super(type, val);
	    this.name = fieldName;
	}

	public String getName() {
		return name;
	}

	public void setName(String s) {
		name = s;
	}

	public String toString() {
		return "{" + name + ", " + type + ", " + value + "}";
	}
}
