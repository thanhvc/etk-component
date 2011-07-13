package org.etk.orm.plugins.jcr;

import java.util.Iterator;

import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.Property;
import javax.jcr.PropertyType;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Value;
import javax.jcr.ValueFactory;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;

import org.etk.orm.api.UndeclaredRepositoryException;
import org.etk.orm.plugins.common.collection.AbstractFilterIterator;

public class PathLinkManager extends AbstractLinkManager {

  public PathLinkManager(Session session) {
    super(session);
  }

  protected Node _getReferenced(Property property) throws RepositoryException {
    int type = property.getType();
    if (type == PropertyType.PATH) {
      String path = property.getString();
      try {
        return (Node)session.getItem(path);
      }
      catch (PathNotFoundException e) {
        // The node has been transiently removed or concurrently removed
        return null;
      }
    } else {
      // throw new MappingException("Property " + name + " is not mapped to a path type");
      // maybe issue a warn
      return null;
    }
  }

  protected void _setReferenced(Node referent, String propertyName, Node referenced) throws RepositoryException {
    if (referenced != null) {
      String path = referenced.getPath();
      ValueFactory valueFactory = session.getValueFactory();
      Value value = valueFactory.createValue(path, PropertyType.PATH);
      referent.setProperty(propertyName, value);
    } else {
      referent.setProperty(propertyName, (String)null);
    }
  }

  protected Iterator<Node> _getReferents(Node referenced, String propertyName) throws RepositoryException {
    String path = referenced.getPath();
    QueryManager queryMgr = session.getWorkspace().getQueryManager();
    Query query = queryMgr.createQuery("SELECT * FROM nt:base WHERE " + propertyName + "='" + path + "'", Query.SQL);
    QueryResult result = query.execute();
    @SuppressWarnings("unchecked") Iterator<Node> nodes = result.getNodes();
    return new AbstractFilterIterator<Node, Node>(nodes) {
      private Node current;
      protected Node adapt(Node node) {
        current = node;
        return node;
      }
      @Override
      public void remove() {
        if (current == null) {
          throw new IllegalStateException();
        }
        try {
          current.remove();
        }
        catch (RepositoryException e) {
          throw new UndeclaredRepositoryException(e);
        }
      }
    };
  }
}

