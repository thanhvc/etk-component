package org.etk.orm.plugins.mapper.onetomany.hierarchical;

import java.util.AbstractList;
import java.util.Iterator;

import org.etk.orm.core.DomainSession;
import org.etk.orm.core.EntityContext;
import org.etk.orm.core.ThrowableFactory;

class AnyChildList<E> extends AbstractList<E> {

  /** . */
  private final EntityContext parentCtx;

  /** . */
  private final String prefix;

  /** . */
  private final Class<E> relatedClass;

  public AnyChildList(EntityContext parentCtx, String prefix, Class<E> relatedClass) {
    this.relatedClass = relatedClass;
    this.prefix = prefix;
    this.parentCtx = parentCtx;
  }

  @Override
  public void add(int index, E addedElement) {
    if (index < 0) {
      throw new IndexOutOfBoundsException();
    }
    if (addedElement == null) {
      throw new NullPointerException("No null element can be inserted");
    }
    if (!relatedClass.isInstance(addedElement)) {
      throw new ClassCastException("Cannot cast object with class " + addedElement.getClass().getName() + " as child expected class " + relatedClass.getName());
    }

    // Get the element that will be the next element of the inserted element
    E nextElement;
    Iterator<E> iterator = iterator();
    while (true) {
      if (index == 0) {
        if (iterator.hasNext()) {
          nextElement = iterator.next();
        } else {
          nextElement = null;
        }
        break;
      } else {
        if (iterator.hasNext()) {
          iterator.next();
          index--;
        } else {
          throw new IndexOutOfBoundsException();
        }
      }
    }

    // Get the session
    DomainSession session = parentCtx.getSession();

    // Get the added context
    EntityContext addedCtx = session.unwrapEntity(addedElement);

    //
    parentCtx.addChild(ThrowableFactory.ISE, ThrowableFactory.IAE, prefix, addedCtx);

    //
    if (nextElement == null) {
      parentCtx.orderBefore(addedCtx, null);
    } else {
      EntityContext nextCtx = session.unwrapEntity(nextElement);
      parentCtx.orderBefore(addedCtx, nextCtx);
    }
  }

  @Override
  public E set(int index, E addedElement) {
    if (addedElement == null) {
      throw new NullPointerException("No null element can be inserted");
    }
    if (!relatedClass.isInstance(addedElement)) {
      throw new ClassCastException("Cannot cast object with class " + addedElement.getClass().getName() + " as child expected class " + relatedClass.getName());
    }

    // Get the removed element
    E removedElement = get(index);

    // Get the session
    DomainSession session = parentCtx.getSession();

    // Unwrap the removed element
    EntityContext removedCtx = session.unwrapEntity(removedElement);

    // Unwrap the added element
    EntityContext addedCtx = session.unwrapEntity(addedElement);

    //
    parentCtx.addChild(ThrowableFactory.ASSERT, ThrowableFactory.IAE, prefix, addedCtx);

    // Order before the removed element
    parentCtx.orderBefore(addedCtx, removedCtx);

    // Remove the element
    session.remove(removedCtx);

    //
    return removedElement;
  }

  @Override
  public E remove(int index) {

    // Get the removed element
    E removedElement = get(index);

    // Get the session
    DomainSession session = parentCtx.getSession();

    // Unwrap the removed element
    EntityContext removedCtx = session.unwrapEntity(removedElement);

    // Remove the element
    session.remove(removedCtx);

    //
    return removedElement;
  }

  public E get(int index) {
    if (index < 0) {
      throw new IndexOutOfBoundsException();
    }
    Iterator<E> iterator = iterator();
    while (true) {
      if (iterator.hasNext()) {
        E o = iterator.next();
        if (index == 0) {
          return o;
        } else {
          index--;
        }
      } else {
        throw new IndexOutOfBoundsException();
      }
    }
  }

  public Iterator<E> iterator() {
    return parentCtx.getChildren(relatedClass);
  }

  public int size() {
    int size = 0;
    Iterator<E> iterator = iterator();
    while (iterator.hasNext()) {
      iterator.next();
      size++;
    }
    return size;
  }
}
