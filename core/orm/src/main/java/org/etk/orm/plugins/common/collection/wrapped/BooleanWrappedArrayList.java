package org.etk.orm.plugins.common.collection.wrapped;


class BooleanWrappedArrayList extends PrimitiveWrappedArrayList<Boolean, boolean[]> {

  public BooleanWrappedArrayList(int size) {
    this(new boolean[size]);
  }

  public BooleanWrappedArrayList(boolean[] array) {
    super(array);
  }

  @Override
  protected Boolean get(boolean[] array, int index) {
    return array[index];
  }

  @Override
  protected int size(boolean[] array) {
    return array.length;
  }

  @Override
  protected void set(boolean[] array, int index, Boolean element) {
    array[index] = element;
  }
}
