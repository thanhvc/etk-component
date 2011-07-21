package org.etk.orm.plugins.jcr.type;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.jcr.nodetype.NodeType;

import org.etk.orm.plugins.jcr.NodeTypeInfo;

/**
 * <p>Meta information about a primary node type.</p>
 *
 * <p>This object does not hold a reference to an existing node type object.</p>
 *
 */
public class PrimaryTypeInfo extends NodeTypeInfo {

  /** . */
  private Set<String>                     mixinNames;

  /** . */
  private final Set<NodeTypeInfo>         superTypes;

  /** . */
  private final Map<String, NodeTypeInfo> superTypesMap;

  public PrimaryTypeInfo(NodeType nodeType, Set<NodeTypeInfo> superTypes) {
    super(nodeType);

    //
    if (nodeType.isMixin()) {
      throw new IllegalArgumentException();
    }

    //
    Set<String> mixinNames = new HashSet<String>();
    for (NodeType superType : nodeType.getSupertypes()) {
      if (superType.isMixin()) {
        mixinNames.add(superType.getName());
      }
    }

    //
    Map<String, NodeTypeInfo> superTypesMap = new HashMap<String, NodeTypeInfo>();
    for (NodeTypeInfo superType : superTypes) {
      superTypesMap.put(superType.getName(), superType);
    }

    //
    this.mixinNames = Collections.unmodifiableSet(mixinNames);
    this.superTypes = superTypes;
    this.superTypesMap = Collections.unmodifiableMap(superTypesMap);
  }

  public Set<String> getSuperTypeNames() {
    return superTypesMap.keySet();
  }

  public NodeTypeInfo getSuperType(String name) {
    return superTypesMap.get(name);
  }

  public Set<NodeTypeInfo> getSuperTypes() {
    return superTypes;
  }

  public Set<String> getMixinNames() {
    return mixinNames;
  }
}
