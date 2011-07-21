package org.etk.orm.plugins.bean;

import java.util.List;

import org.etk.orm.plugins.bean.mapping.JLOTypeInfo;
import org.etk.reflect.api.ClassTypeInfo;
import org.etk.reflect.api.TypeInfo;
import org.etk.reflect.api.TypeVariableInfo;
import org.etk.reflect.api.WildcardTypeInfo;

class Utils {

  /*
   * todo: defines clearly what this does. 
   */
  static ClassTypeInfo resolveToClassType(ClassTypeInfo baseType, TypeInfo type) {
    TypeInfo resolvedType = baseType.resolve(type);

    //
    if (resolvedType instanceof ClassTypeInfo) {
      return (ClassTypeInfo)resolvedType;
    } else if (resolvedType instanceof TypeVariableInfo) {
      return resolveToClassType(baseType, ((TypeVariableInfo)resolvedType).getBounds().get(0));
    } else if (resolvedType instanceof WildcardTypeInfo) {
      WildcardTypeInfo wti = (WildcardTypeInfo) resolvedType;
      List<TypeInfo> bounds = wti.getUpperBounds();
      if (bounds.size() == 0) {
        bounds = wti.getLowerBounds();
      }
      if (bounds.size() == 0) {
        return JLOTypeInfo.get();
      } else {
        return resolveToClassType(baseType, bounds.get(0));
      }
    } else {
      return null;
    }
  }
}

