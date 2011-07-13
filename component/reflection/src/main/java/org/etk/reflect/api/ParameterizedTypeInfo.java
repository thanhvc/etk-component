package org.etk.reflect.api;

import java.util.List;

public interface ParameterizedTypeInfo extends TypeInfo {
  TypeInfo getRawType();
  List<TypeInfo> getTypeArguments();
  
  TypeInfo getOwnerType();

}
