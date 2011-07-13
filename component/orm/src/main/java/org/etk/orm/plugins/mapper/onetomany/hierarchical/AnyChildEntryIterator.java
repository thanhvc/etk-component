package org.etk.orm.plugins.mapper.onetomany.hierarchical;

import java.util.Iterator;
import java.util.Map;

import org.etk.orm.core.EntityContext;
import org.etk.orm.plugins.common.collection.AbstractFilterIterator;

public class AnyChildEntryIterator<E> extends AbstractFilterIterator<Map.Entry<String, E>, E> {

  /** . */
  private final AnyChildMap map;

  public AnyChildEntryIterator(AnyChildMap<E> map) throws NullPointerException {
    super(map.parentCtx.getChildren(map.relatedClass));

    //
    this.map = map;
  }

  protected Map.Entry<String, E> adapt(final E internal) {
    final EntityContext internalCtx = map.parentCtx.getSession().unwrapEntity(internal);
    return new Map.Entry<String, E>() {

      /** . */
      private final String name = map.parentCtx.getSession().getLocalName(internalCtx);

      public String getKey() {
        return name;
      }

      public E getValue() {
        return internal;
      }

      public E setValue(E value) {
        throw new UnsupportedOperationException();
      }
    };
  }
}

