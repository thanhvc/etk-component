package org.etk.kernel.container.xml.object;

public class XMLNativeArray {

	private Object array;

	private String type;

	public XMLNativeArray() {
	}

	public XMLNativeArray(Object o) throws Exception {
		if (!isNativeArray(o)) {
			throw new Exception(o.getClass().getName() + " is not a native array");
		}
		array = o;
		setType(o);
	}

	public String getType() {
		return type;
	}

	public void setType(String s) {
		type = s;
	}

	public void setType(Object o) {
		if (o instanceof int[])
			type = XMLBaseObject.INT;
		else if (o instanceof long[])
			type = XMLBaseObject.LONG;
		else if (o instanceof float[])
			type = XMLBaseObject.FLOAT;
		else if (o instanceof double[])
			type = XMLBaseObject.DOUBLE;
		else if (o instanceof boolean[])
			type = XMLBaseObject.BOOLEAN;
	}

	public Object getValue() {
		return array;
	}

	public String getArray() {
		if (type.equals(XMLBaseObject.INT))
			return encodeIntArray((int[]) array);
		else if (type.equals(XMLBaseObject.LONG))
			return encodeLongArray((long[]) array);
		else if (type.equals(XMLBaseObject.DOUBLE))
			return encodeDoubleArray((double[]) array);
		else
			throw new RuntimeException("Unknown array type: " + type);
	}

	public void setArray(String text) {
		if (type.equals(XMLBaseObject.INT))
			array = decodeIntArray(text);
		else if (type.equals(XMLBaseObject.LONG))
			array = decodeLongArray(text);
		else if (type.equals(XMLBaseObject.DOUBLE))
			array = decodeDoubleArray(text);
	}

	static public int[] decodeIntArray(String text) {
		String temp[] = text.split(",");
		int[] iarray = new int[temp.length];
		for (int i = 0; i < temp.length; i++) {
			temp[i] = temp[i].trim();
			iarray[i] = Integer.parseInt(temp[i]);
		}
		return iarray;
	}

	static public String encodeIntArray(int[] array) {
		StringBuffer b = new StringBuffer();
		for (int i = 0; i < array.length; i++) {
			b.append(array[i]);
			if (i != array.length - 1)
				b.append(", ");
		}
		return b.toString();
	}

	static public long[] decodeLongArray(String text) {
		String temp[] = text.split(",");
		long[] array = new long[temp.length];
		for (int i = 0; i < temp.length; i++) {
			temp[i] = temp[i].trim();
			array[i] = Long.parseLong(temp[i]);
		}
		return array;
	}

	static public String encodeLongArray(long[] array) {
		StringBuffer b = new StringBuffer();
		for (int i = 0; i < array.length; i++) {
			b.append(array[i]);
			if (i != array.length - 1)
				b.append(", ");
		}
		return b.toString();
	}

	static public double[] decodeDoubleArray(String text) {
		String temp[] = text.split(",");
		double[] array = new double[temp.length];
		for (int i = 0; i < temp.length; i++) {
			temp[i] = temp[i].trim();
			array[i] = Double.parseDouble(temp[i]);
		}
		return array;
	}

	static public String encodeDoubleArray(double[] array) {
		StringBuffer b = new StringBuffer();
		for (int i = 0; i < array.length; i++) {
			b.append(array[i]);
			if (i != array.length - 1)
				b.append(", ");
		}
		return b.toString();
	}

	static public boolean isNativeArray(Object o) {
		if (o instanceof int[] || o instanceof long[] || o instanceof float[]
				|| o instanceof double[] || o instanceof boolean[])
			return true;
		return false;
	}

	static public boolean isNativeArray(Class clazz) {
		if (clazz.equals(int[].class) || clazz.equals(long[].class)
				|| clazz.equals(float[].class) || clazz.equals(double[].class)
				|| clazz.equals(boolean[].class))
			return true;
		return false;
	}
}
