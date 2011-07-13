package org.etk.orm.plugins.common.collection;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class CompoundIterator<E> implements Iterator<E> {

  /** . */
  private Iterator<Iterator<E>> iteratorIterator;

  /** . */
  private Iterator<E> current;

  public CompoundIterator(Iterator<Iterator<E>> iteratorIterator) {
    this.iteratorIterator = iteratorIterator;
  }

  public CompoundIterator(List<Iterator<E>> iterators) {
    this(iterators.iterator());
  }

  public CompoundIterator(Iterator<E>... iterators) {
    this(Arrays.asList(iterators));
  }

  public boolean hasNext() {
    if (iteratorIterator == null) {
      return false;
    }
    while (current == null || !current.hasNext()) {
      if (iteratorIterator.hasNext()) {
        current = iteratorIterator.next();
      } else {
        iteratorIterator = null;
        return false;
      }
    }
    return true;
  }

  public E next() {
    if (!hasNext()) {
      throw new NoSuchElementException();
    }
    return current.next();
  }

  public void remove() {
    current.remove();
  }
}
