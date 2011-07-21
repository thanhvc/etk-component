package org.etk.orm.plugins.mapper.onetomany.hierarchical;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Map;

public class AnyChildEntrySet<E> extends AbstractSet<Map.Entry<String, E>> {

  /** . */
  private final AnyChildMap<E> map;

  public AnyChildEntrySet(AnyChildMap<E> map) {
    this.map = map;
  }

  public Iterator<Map.Entry<String, E>> iterator() {
    return new AnyChildEntryIterator<E>(map);
  }

  public int size() {
    int size = 0;
    Iterator<E> iterator = map.parentCtx.getChildren(map.relatedClass);
    while (iterator.hasNext()) {
      iterator.next();
      size++;
    }
    return size;
  }
}

