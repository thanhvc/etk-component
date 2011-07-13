package org.etk.orm.plugins.common.collection.wrapped;


class LongWrappedArrayList extends PrimitiveWrappedArrayList<Long, long[]> {

  public LongWrappedArrayList(int size) {
    this(new long[size]);
  }

  public LongWrappedArrayList(long[] array) {
    super(array);
  }

  @Override
  protected Long get(long[] array, int index) {
    return array[index];
  }

  @Override
  protected int size(long[] array) {
    return array.length;
  }

  @Override
  protected void set(long[] array, int index, Long element) {
    array[index] = element;
  }
}