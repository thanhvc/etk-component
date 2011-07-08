package org.etk.kernel.container.xml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;


//import org.exoplatform.container.xml.ObjectParameter;
//import org.exoplatform.container.xml.Parameter;
//import org.exoplatform.container.xml.PropertiesParam;
//import org.exoplatform.container.xml.ValueParam;
//import org.exoplatform.container.xml.ValuesParam;

public class InitParams extends HashMap<String, Object> {

	public InitParams() {

	}

	public ValuesParam getValuesParam(String name) {
		return (ValuesParam) get(name);
	}

	public ValueParam getValueParam(String name) {
		return (ValueParam) get(name);
	}

	public PropertiesParam getPropertiesParam(String name) {
		return (PropertiesParam) get(name);
	}

	public ObjectParameter getObjectParam(String name) {
		return (ObjectParameter) get(name);
	}

	public <T> List<T> getObjectParamValues(Class<T> type) {
		List<T> list = new ArrayList<T>();
		for (Object o : values()) {
			if (o instanceof ObjectParameter) {
				ObjectParameter param = (ObjectParameter) o;
				Object paramValue = param.getObject();
				if (type.isInstance(paramValue)) {
					T t = type.cast(paramValue);
					list.add(t);
				}
			}
		}
		return list;
	}

	public Parameter getParameter(String name) {
		return (Parameter) get(name);
	}

	public void addParameter(Parameter param) {
		put(param.getName(), param);
	}

	public Parameter removeParameter(String name) {
		return (Parameter) remove(name);
	}

	// --------------xml binding---------------------------------

	public void addParam(Object o) {
		Parameter param = (Parameter) o;
		put(param.getName(), param);
	}

	public Iterator<ValueParam> getValueParamIterator() {
		return getValueIterator(ValueParam.class);
	}

	public Iterator<ValuesParam> getValuesParamIterator() {
		return getValueIterator(ValuesParam.class);
	}

	public Iterator getPropertiesParamIterator() {
		return getValueIterator(PropertiesParam.class);
	}

	public Iterator getObjectParamIterator() {
		return getValueIterator(ObjectParameter.class);
	}

	private <T> Iterator<T> getValueIterator(Class<T> type) {
		List<T> list = new ArrayList<T>();
		for (Object o : values()) {
			if (type.isInstance(o)) {
				T t = type.cast(o);
				list.add(t);
			}
		}
		return list.iterator();
	}

}
