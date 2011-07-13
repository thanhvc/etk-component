package org.etk.orm.plugins.common;

import java.util.Iterator;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Property;
import javax.jcr.PropertyIterator;
import javax.jcr.RepositoryException;
import javax.jcr.UnsupportedRepositoryOperationException;
import javax.jcr.nodetype.NodeType;
import javax.jcr.nodetype.PropertyDefinition;

public class JCR {

  @SuppressWarnings("unchecked")
  public static Iterator<Property> adapt(final PropertyIterator iterator) {
    return (Iterator<Property>)iterator;
  }

  @SuppressWarnings("unchecked")
  public static Iterator<Node> adapt(final NodeIterator iterator) {
    return (Iterator<Node>)iterator;
  }

  /**
   * Return true if the two nodes are equals with the meaning:
   * <ul>
   *   <li>two null nodes are equals</li>
   *   <li>two non null nodes are equals if
   *     <li>the are originated from the same session</li>
   *     <li>they have the same id when they are referenceable otherwise they have the same path</li>
   *   </li>
   * </ul>
   *
   * @param a the node a
   * @param b the node b
   * @return true if the two nodes are equals
   * @throws RepositoryException any repository exception
   */
  public static boolean equals(Node a, Node b) throws RepositoryException {
    boolean equals;
    if (a == b) {
      equals = true;
    } else if (a == null || b == null) {
      return false;
    } else {
      if (a.getSession() == b.getSession()) {
        try {
          String aId = a.getUUID();
          String bId = b.getUUID();
          equals = aId.equals(bId);
        }
        catch (UnsupportedRepositoryOperationException e) {
          // Compare path
          String aPath = a.getPath();
          String bPath = b.getPath();
          equals = aPath.equals(bPath);
        }
      } else {
        equals = false;
      }
    }
    return equals;
  }

  public static boolean hasMixin(Node node, String mixinTypeName) throws RepositoryException {
    if (node == null) {
      throw new NullPointerException();
    }
    if (mixinTypeName == null) {
      throw new NullPointerException();
    }
    for (NodeType nodeType : node.getMixinNodeTypes()) {
      if (nodeType.getName().equals(mixinTypeName)) {
        return true;
      }
    }
    return false;
  }

  public static PropertyDefinition getPropertyDefinition(NodeType nodeType, String propertyName) throws RepositoryException {
    for (PropertyDefinition def : nodeType.getPropertyDefinitions()) {
      if (def.getName().equals(propertyName)) {
        return def;
      }
    }
    return null;
  }

  public static PropertyDefinition getPropertyDefinition(Node node, String propertyName) throws RepositoryException {
    if (node.hasProperty(propertyName)) {
      return node.getProperty(propertyName).getDefinition();
    } else {
      NodeType primaryNodeType = node.getPrimaryNodeType();
      PropertyDefinition def = getPropertyDefinition(primaryNodeType, propertyName);
      if (def == null) {
        for (NodeType mixinNodeType : node.getMixinNodeTypes()) {
          def = getPropertyDefinition(mixinNodeType, propertyName);
          if (def != null) {
            break;
          }
        }
      }
      return def;
    }
  }

  public static String qualify(String prefix, String localName) {
    if (localName == null) {
      return null;
    } else {
      if (prefix != null && prefix.length() > 0) {
        return prefix + ':' + localName;
      } else {
        return localName;
      }
    }
  }
}
