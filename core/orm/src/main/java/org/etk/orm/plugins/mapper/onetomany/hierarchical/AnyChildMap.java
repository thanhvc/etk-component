package org.etk.orm.plugins.mapper.onetomany.hierarchical;

import java.util.AbstractMap;
import java.util.Set;
import java.util.Map.Entry;

import org.etk.orm.core.EntityContext;
import org.etk.orm.core.ThrowableFactory;

public class AnyChildMap<E> extends AbstractMap<String, E> {

  /** . */
  final EntityContext parentCtx;

  /** . */
  final String prefix;

  /** . */
  final Class<E> relatedClass;

  /** . */
  private final AnyChildEntrySet<E> entries;

  public AnyChildMap(
    EntityContext parentCtx,
    String prefix,
    Class<E> relatedClass) {
    this.relatedClass = relatedClass;
    this.prefix = prefix;
    this.entries = new AnyChildEntrySet<E>(this);
    this.parentCtx = parentCtx;
  }

  @Override
  public E get(Object key) {
    if (key instanceof String) {
      String name = (String)key;
      EntityContext childCtx = parentCtx.getChild(prefix, name);
      if (childCtx != null) {
        Object child = childCtx.getObject();
        if (relatedClass.isInstance(child)) {
          return relatedClass.cast(child);
        }
      }
    }
    return null;
  }

  @Override
  public E remove(Object key) {
    if (key instanceof String) {
      return put((String)key, null);
    } else {
      return null;
    }
  }

  @Override
  public E put(String key, E value) {
    EntityContext childCtx = parentCtx.getChild(prefix, key);

    //
    if (value == null) {
      if (childCtx != null) {
        parentCtx.getSession().remove(childCtx);
      }
    } else if (relatedClass.isInstance(value)) {
      EntityContext valueCtx = parentCtx.getSession().unwrapEntity(value);
      parentCtx.addChild(ThrowableFactory.ISE, ThrowableFactory.IAE, prefix, key, valueCtx);
    } else {
      throw new ClassCastException("Cannot put " + value + " with in map containing values of type " + relatedClass);
    }

    //
    if (childCtx != null) {
      Object child = childCtx.getObject();
      if (relatedClass.isInstance(child)) {
        return relatedClass.cast(child);
      }
    }

    // julien todo : unit test that
    return null;
  }

  public Set<Entry<String, E>> entrySet() {
    return entries;
  }
}

