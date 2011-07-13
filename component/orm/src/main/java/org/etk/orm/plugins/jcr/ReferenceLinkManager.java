package org.etk.orm.plugins.jcr;

import java.util.Iterator;

import javax.jcr.ItemNotFoundException;
import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.PropertyIterator;
import javax.jcr.PropertyType;
import javax.jcr.RepositoryException;
import javax.jcr.Session;


import org.etk.orm.api.UndeclaredRepositoryException;
import org.etk.orm.plugins.common.collection.AbstractFilterIterator;

/**
 * <p>The reference manager takes care of managing references between nodes. The main reason is that
 * JCR reference management is a bit weird about the usage of <tt>Node#getReferences()</tt>. The goal
 * of this class is to manage one to many relationships between nodes and their consistency.</p>
 *
 * <p>The life time of this object is valid from the beginning of the session until the session
 * or a portion of the session is saved. When a session is saved, the clear operation will reset
 * the state of the reference manager.</p>
 *
 */
public class ReferenceLinkManager extends AbstractLinkManager {

  public ReferenceLinkManager(Session session) {
    super(session);
  }

  protected Node _getReferenced(Property property) throws RepositoryException {
    if (property.getType() == PropertyType.REFERENCE) {
      try {
        return property.getNode();
      }
      catch (ItemNotFoundException e) {
        // The node has been transiently removed or concurrently removed
        return null;
      }
    } else {
      // throw new MappingException("Property " + name + " is not mapped to a reference type");
      // maybe issue a warn
      return null;
    }
  }

  protected void _setReferenced(Node referent, String propertyName, Node referenced) throws RepositoryException {
    referent.setProperty(propertyName, referenced);
  }

  @SuppressWarnings("unchecked")
  protected Iterator<Node> _getReferents(Node referenced, String propertyName) throws RepositoryException {
    PropertyIterator bilto = referenced.getReferences();
    return new AbstractFilterIterator<Node, Property>(bilto) {
      protected Node adapt(Property property) {
        try {
          String propertyName = property.getName();
          if (propertyName.equals(propertyName)) {
            return property.getParent();
          }
          return null;
        }
        catch (RepositoryException e) {
          throw new UndeclaredRepositoryException(e);
        }
      }
    };
  }
}
