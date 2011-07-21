package org.etk.kernel.container.xml;

import org.etk.kernel.container.xml.object.XMLObject;

public class ObjectParameter extends Parameter {

	private Object object;

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}
	
	public XMLObject getXMLObject() throws Exception {
		return object == null ? null : new XMLObject(object);
	}
	
	public void setXMLObject(XMLObject xmlObject) throws Exception {
		if (xmlObject == null) object = null;
		
		try {
			object = xmlObject.toObject();
		} catch (Exception ex) {
			throw ex;
		}
	}
	
	public String toString() {
		return object.toString();
	}
}
