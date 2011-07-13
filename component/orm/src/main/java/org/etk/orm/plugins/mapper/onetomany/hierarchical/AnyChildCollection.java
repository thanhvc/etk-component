package org.etk.orm.plugins.mapper.onetomany.hierarchical;

import java.util.AbstractCollection;
import java.util.Iterator;

import org.etk.orm.core.EntityContext;
import org.etk.orm.core.ThrowableFactory;


class AnyChildCollection<E> extends AbstractCollection<E> {

  /** . */
  private final EntityContext parentCtx;

  /** . */
  private final String prefix;

  /** . */
  private final Class<E> relatedClass;

  public AnyChildCollection(EntityContext parentCtx, String prefix, Class<E> relatedClass) {
    this.relatedClass = relatedClass;
    this.prefix = prefix;
    this.parentCtx = parentCtx;
  }

  @Override
  public boolean add(Object child) {
    if (child == null) {
      throw new NullPointerException();
    }
    if (!relatedClass.isInstance(child)) {
      throw new ClassCastException("Cannot cast object with class " + child.getClass().getName() + " as child expected class " + relatedClass.getName());
    }

    //
    EntityContext childCtx = parentCtx.getSession().unwrapEntity(child);

    //
    parentCtx.addChild(ThrowableFactory.ISE, ThrowableFactory.IAE, prefix, childCtx);

    //
    return true;
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

