package org.etk.kernel.container.xml.object;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.jibx.runtime.BindingDirectory;
import org.jibx.runtime.IBindingFactory;
import org.jibx.runtime.IMarshallingContext;
import org.jibx.runtime.IUnmarshallingContext;


public class XMLCollection {

	private ArrayList list = new ArrayList();

	private String type;

	public XMLCollection() {
	}

	public XMLCollection(Collection list) throws Exception {
		Iterator i = list.iterator();
		while (i.hasNext()) {
			Object value = i.next();
			if (value != null) {
				list.add(new XMLValue(null, value));
			}
		}
		this.type = list.getClass().getName();
	}

	public String getType() {
		return type;
	}

	public void setType(String s) {
		type = s;
	}

	public Collection getCollection() throws Exception {
		Class clazz = Class.forName(type);
		Collection collection = (Collection) clazz.newInstance();
		for (int i = 0; i < list.size(); i++) {
			XMLValue value = (XMLValue) list.get(i);
			collection.add(value.getObjectValue());
		}
		return collection;
	}

	public Iterator getIterator() {
		return list.iterator();
	}

	public String toXML(String encoding) throws Exception {
		return new String(toByteArray(encoding), encoding);
	}

	public byte[] toByteArray(String encoding) throws Exception {
		IBindingFactory bfact = BindingDirectory.getFactory(XMLObject.class);
		IMarshallingContext mctx = bfact.createMarshallingContext();
		mctx.setIndent(2);
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		mctx.marshalDocument(this, encoding, null, os);
		return os.toByteArray();
	}

	static public XMLCollection getXMLCollection(InputStream is)
			throws Exception {
		IBindingFactory bfact = BindingDirectory.getFactory(XMLObject.class);
		IUnmarshallingContext uctx = bfact.createUnmarshallingContext();
		return (XMLCollection) uctx.unmarshalDocument(is, null);
	}

	static public Collection getCollection(InputStream is) throws Exception {
		return getXMLCollection(is).getCollection();
	}
}
