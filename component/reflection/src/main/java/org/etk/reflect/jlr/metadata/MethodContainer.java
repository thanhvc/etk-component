package org.etk.reflect.jlr.metadata;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

class MethodContainer implements Iterable<Method>  {

  /** . */
  private final Map<MethodSignature, Method> map = new LinkedHashMap<MethodSignature, Method>();

  public void addAll(Iterable<Method> methods) {
    for (Method method : methods) {
      add(method);
    }
  }

  public boolean add(Method method) {
    MethodSignature key = new MethodSignature(method);
    Method existing = map.get(key);
    if (existing != null) {
      if (existing.getReturnType().isAssignableFrom(method.getReturnType())) {
        if (existing.getDeclaringClass().isAssignableFrom(method.getDeclaringClass())) {
          map.put(key, method);
          return true;
        }
      }
    } else {
      map.put(key, method);
      return true;
    }
    return false;
  }

  public Iterator<Method> iterator() {
    return map.values().iterator();
  }
}
