package org.etk.orm.apt;

import java.util.Map;

import org.etk.reflect.api.ClassTypeInfo;

class PackageMetaData {

  /** . */
  final String packageName;

  /** . */
  final Map<String, String> prefixMappings;

  /** . */
  final boolean deep;

  public PackageMetaData(
    String packageName,
    Map<String, String> prefixMappings,
    boolean deep) {
    this.packageName = packageName;
    this.prefixMappings = prefixMappings;
    this.deep = deep;
  }

  int distance(ClassTypeInfo cti) {
    int distance;
    if (deep) {
      if (cti.getPackageName().startsWith(packageName)) {
        distance = 0;
        for (String packageName : PackageNameIterator.with(cti.getPackageName())) {
          if (cti.getPackageName().equals(packageName)) {
            break;
          } else {
            distance++;
          }
        }
      } else {
        distance = -1;
      }
    } else {
      distance = cti.getPackageName().equals(packageName) ? 0 : -1;
    }
    return distance;
  }
}
