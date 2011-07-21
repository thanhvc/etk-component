package org.etk.orm.plugins.common.collection.wrapped;


class IntWrappedArrayList extends PrimitiveWrappedArrayList<Integer, int[]> {

  public IntWrappedArrayList(int size) {
    this(new int[size]);
  }

  public IntWrappedArrayList(int[] array) {
    super(array);
  }

  @Override
  protected Integer get(int[] array, int index) {
    return array[index];
  }

  @Override
  protected int size(int[] array) {
    return array.length;
  }

  @Override
  protected void set(int[] array, int index, Integer element) {
    array[index] = element;
  }
}