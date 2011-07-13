package org.etk.orm.core;

import java.util.Iterator;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

import org.etk.orm.plugins.common.collection.AbstractFilterIterator;


final class ChildCollectionIterator<T> extends AbstractFilterIterator<T, Node> {

  /** . */
  private final Class<T> relatedClass;

  /** . */
  private final DomainSession session;

  /** The last iterated object. */
  private Object current;

  public ChildCollectionIterator(DomainSession session, Iterator<Node> iterator, Class<T> relatedClass) throws RepositoryException {
    super(iterator);

    //
    this.session = session;
    this.relatedClass = relatedClass;
  }

  protected T adapt(Node node) {
    Object o = session.findByNode(Object.class, node);
    if (relatedClass.isInstance(o)) {
      current = o;
      return relatedClass.cast(o);
    } else {
      return null;
    }
  }

  @Override
  public void remove() {
    if (current != null) {
      EntityContext tmp = session.unwrapEntity(current); 
      current = null;
      session.remove(tmp);
    } else {
      throw new IllegalStateException("No object to remove");
    }
  }
}

