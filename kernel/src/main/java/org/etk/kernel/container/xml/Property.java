package org.etk.kernel.container.xml;

public class Property {
	String name;

	String value;

	public Property() {
	}

	public Property(String n, String v) {
		this.name = n;
		this.value = v;
	}

	public String getName() {
		return name;
	}

	public void setName(String s) {
		this.name = s;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String s) {
		this.value = s;
	}
}
