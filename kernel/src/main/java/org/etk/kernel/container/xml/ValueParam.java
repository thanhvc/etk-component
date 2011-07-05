package org.etk.kernel.container.xml;

public class ValueParam extends Parameter {
	private String value;

	public String getValue() {
		return value;
	}

	public void setValue(String s) {
		value = s;
	}

	public String toString() {
		return value;
	}
}
