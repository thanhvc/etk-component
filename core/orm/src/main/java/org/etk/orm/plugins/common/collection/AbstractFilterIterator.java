package org.etk.orm.plugins.common.collection;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * An iterator that filter the elements of a delegate iterator allowing to skip some elements and
 * to perform type conversion.
 *
 */
public abstract class AbstractFilterIterator<E, I> implements Iterator<E> {

  /** . */
  private Iterator<I> iterator;

  /** . */
  private E next;

  /**
   * Create a new filter iterator.
   *
   * @param iterator the iterator
   * @throws NullPointerException if the iterator is null
   */
  public AbstractFilterIterator(Iterator<I> iterator) throws NullPointerException {
    if (iterator == null) {
      throw new NullPointerException();
    }

    //
    this.iterator = iterator;
  }

  public final boolean hasNext() {
    if (next == null) {
      bilto:
      if (iterator != null) {
        while (iterator.hasNext()) {
          I internal = iterator.next();
          E external = adapt(internal);
          if (external != null) {
            next = external;
            break bilto;
          }
        }
        iterator = null;
      }
    }

    //
    return next != null;
  }

  public final E next() {
    if (!hasNext()) {
      throw new NoSuchElementException();
    }

    //
    E tmp = next;
    next = null;

    //
    return tmp;
  }

  public void remove() {
    iterator.remove();
  }

  /**
   * Adapts the internal element as an external element. Returning a null external element means that
   * the element must be skipped and not considered by the iterator.
   *
   * @param internal the internal element
   * @return the external element
   */
  protected abstract E adapt(I internal);
}