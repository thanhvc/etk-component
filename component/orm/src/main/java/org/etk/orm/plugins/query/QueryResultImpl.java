package org.etk.orm.plugins.query;

import javax.jcr.Node;
import javax.jcr.NodeIterator;

import org.etk.orm.core.DomainSession;
import org.etk.orm.plugins.common.JCR;
import org.etk.orm.plugins.common.collection.AbstractFilterIterator;


public class QueryResultImpl<O> extends AbstractFilterIterator<O, Node> implements QueryResult<O> {

  /** . */
  private final Class<O> clazz;

  /** . */
  private final NodeIterator iterator;

  /** . */
  private final int hits;

  /** . */
  private final DomainSession session;

  QueryResultImpl(DomainSession session, NodeIterator iterator, int hits, Class<O> clazz) throws NullPointerException {
    super(JCR.adapt(iterator));

    //
    this.session = session;
    this.iterator = iterator;
    this.hits = hits;
    this.clazz = clazz;
  }

  protected O adapt(Node internal) {
    Object o = session.findByNode(Object.class, internal);
    if (clazz.isInstance(o)) {
      return clazz.cast(o);
    }
    else {
      return null;
    }
  }

  public int size() {
    return (int)iterator.getSize();
  }

  public int hits() {
    return hits;
  }
}