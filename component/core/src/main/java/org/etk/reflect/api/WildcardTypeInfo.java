package org.etk.reflect.api;

import java.util.List;


public interface WildcardTypeInfo extends TypeInfo {

	  List<TypeInfo> getUpperBounds();

	  List<TypeInfo> getLowerBounds();

	}
