package org.etk.orm.plugins.common.collection.wrapped;

import java.lang.reflect.Array;

class GenericWrappedArrayList<E> extends WrappedArrayList<E, E[]> {

  public GenericWrappedArrayList(Class<E> elementType, int size) {
    this((E[])Array.newInstance(elementType, size));
  }

  public GenericWrappedArrayList(E[] array) {
    super(array);
  }

  @Override
  protected E get(E[] array, int index) {
    return array[index];
  }

  @Override
  protected int size(E[] array) {
    return array.length;
  }

  @Override
  protected void set(E[] array, int index, E element) {
    array[index] = element;
  }
}

