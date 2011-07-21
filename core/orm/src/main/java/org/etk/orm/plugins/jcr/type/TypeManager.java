package org.etk.orm.plugins.jcr.type;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.nodetype.NodeType;

import org.etk.orm.plugins.jcr.NodeTypeInfo;

/**
 * <p>Manage type related information.</p>
 *
 * <p>This acts actually like the cache of underlying JCR type system.</p>
 *
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public class TypeManager {

  /** . */
  private final Object nodeTypeInfosLock = new Object();

  /** . */
  private volatile Map<String, NodeTypeInfo> nodeTypeInfos = new HashMap<String, NodeTypeInfo>();

  public boolean isReferenceable(Node node) throws RepositoryException {

    //
    for (NodeType nt : node.getMixinNodeTypes()) {
      if (nt.getName().equals("mix:referenceable")) {
        return true;
      }
    }

    //
    PrimaryTypeInfo ntInfo = (PrimaryTypeInfo)getTypeInfo(node.getPrimaryNodeType());

    //
    return ntInfo.getMixinNames().contains("mix:referenceable");
  }

  public PrimaryTypeInfo getPrimaryTypeInfo(NodeType primaryType) throws RepositoryException {
    return (PrimaryTypeInfo)getTypeInfo(primaryType);
  }

  public MixinTypeInfo getMixinTypeInfo(NodeType mixinType) throws RepositoryException {
    return (MixinTypeInfo)getTypeInfo(mixinType);
  }

  private NodeTypeInfo getTypeInfo(NodeType nodeType) {
    String nodeTypeName = nodeType.getName();
    NodeTypeInfo nodeTypeInfo = nodeTypeInfos.get(nodeTypeName);
    if (nodeTypeInfo == null) {

      // Compute
      if (nodeType.isMixin()) {
        nodeTypeInfo = new MixinTypeInfo(nodeType);
      } else {
        Set<NodeTypeInfo> superTypes = new HashSet<NodeTypeInfo>();
        for (NodeType superType : nodeType.getSupertypes()) {
          NodeTypeInfo superTIs = getTypeInfo(superType);
          superTypes.add(superTIs);
        }
        nodeTypeInfo = new PrimaryTypeInfo(nodeType, Collections.unmodifiableSet(superTypes));
      }

      // Add
      synchronized (nodeTypeInfosLock) {
        Map<String, NodeTypeInfo> copy = new HashMap<String, NodeTypeInfo>(nodeTypeInfos);
        copy.put(nodeTypeName, nodeTypeInfo);
        nodeTypeInfos = copy;
      }
    }
    return nodeTypeInfo;
  }
}

