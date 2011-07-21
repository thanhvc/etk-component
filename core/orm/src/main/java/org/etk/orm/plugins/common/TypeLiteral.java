package org.etk.orm.plugins.common;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class TypeLiteral {

  public static <T> Class<T> get(Class<?> clazz, int index) {
    Type type = clazz.getGenericSuperclass();
    ParameterizedType parameterizedType = (ParameterizedType)type;
    return (Class<T>)parameterizedType.getActualTypeArguments()[index];
  }
}