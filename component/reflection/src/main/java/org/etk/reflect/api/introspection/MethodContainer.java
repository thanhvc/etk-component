package org.etk.reflect.api.introspection;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.etk.reflect.api.ClassTypeInfo;
import org.etk.reflect.api.MethodInfo;
import org.etk.reflect.api.MethodSignature;


class MethodContainer implements Iterable<MethodInfo>  {

  /** . */
  private final Map<MethodSignature, MethodInfo> map = new LinkedHashMap<MethodSignature, MethodInfo>();

  /** . */
  private final ClassTypeInfo context;

  MethodContainer(ClassTypeInfo context) {
    this.context = context;
  }

  MethodContainer() {
    this.context = null;
  }

  public void addAll(Iterable<MethodInfo> methods) {
    for (MethodInfo method : methods) {
      add(method);
    }
  }

  public boolean add(MethodInfo method) {
    MethodSignature key;
    if (context != null) {
      key = method.getSignature(context);
    } else {
      key = method.getSignature();
    }

    //
    MethodInfo existing = map.get(key);

    //
    if (existing != null) {
      if (method.getReturnType().isSubType(existing.getReturnType())) {
        if (existing.getOwner().isAssignableFrom(method.getOwner())) {
          map.put(key, method);
          return true;
        }
      }
    } else {
      map.put(key, method);
      return true;
    }

    //
    return false;
  }

  public Set<MethodInfo> toCollection() {
    return new HashSet<MethodInfo>(map.values());
  }

  public Iterator<MethodInfo> iterator() {
    return map.values().iterator();
  }
}
