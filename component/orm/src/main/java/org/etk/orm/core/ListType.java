package org.etk.orm.core;

import java.util.ArrayList;
import java.util.List;

import org.etk.orm.plugins.common.collection.wrapped.WrappedArrayList;
import org.etk.orm.plugins.vt2.ValueDefinition;


public abstract class ListType {

  public abstract <E> List<E> create(ValueDefinition<?, E> elementType, int size);

  public abstract <E> Object unwrap(ValueDefinition<?, E> elementType, List<E> list);

  public abstract <E> List<E> wrap(ValueDefinition<?, E> elementType, Object array);

  public static final ListType ARRAY = new ListType() {

    @Override
    public <E> List<E> create(ValueDefinition<?, E> elementType, int size) {
      return WrappedArrayList.create(
        elementType.getObjectType(),
        elementType.getRealType(),
        size);
    }

    @Override
    public <E> List<E> wrap(ValueDefinition<?, E> elementType, Object array) {
      return WrappedArrayList.wrap(elementType.getObjectType(), array);
    }

    @Override
    public <E> Object unwrap(ValueDefinition<?, E> elementType, List<E> list) {
      return ((WrappedArrayList)list).getArray();
    }
  };

  public static final ListType LIST = new ListType() {

    @Override
    public <E> List<E> create(ValueDefinition<?, E> elementType, int size) {
      ArrayList<E> list = new ArrayList<E>(size);
      for (int i = 0;i < size;i++) {
        list.add(null);
      }
      return list;
    }

    @Override
    public <E> List<E> wrap(ValueDefinition<?, E> elementType, Object array) {
      return (List<E>)array;
    }

    @Override
    public <E> Object unwrap(ValueDefinition<?, E> elementType, List<E> list) {
      return list;
    }
  };
}

