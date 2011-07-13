package org.etk.orm.core;

import java.util.Iterator;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

import org.etk.orm.plugins.common.collection.AbstractFilterIterator;


final class ReferentCollectionIterator<T> extends AbstractFilterIterator<T, Node> {

  /** . */
  private final Class<T> relatedClass;

  /** . */
  private final DomainSession session;

  /** . */
  private final String propertyName;

  public ReferentCollectionIterator(
    DomainSession session,
    Iterator<Node> iterator,
    Class<T> relatedClass,
    String propertyName) throws RepositoryException {
    super(iterator);

    //
    this.session = session;
    this.relatedClass = relatedClass;
    this.propertyName = propertyName;
  }

  Node previous;

  protected T adapt(Node node) {
    EntityContext ctx = session.getEntity(node);
    T t = ctx.adapt(relatedClass);
    if (t != null) {
      previous = node;
      return t;
    } else {
      return null;
    }
  }
}
