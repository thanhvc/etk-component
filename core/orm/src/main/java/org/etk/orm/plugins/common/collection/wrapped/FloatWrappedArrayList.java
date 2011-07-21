package org.etk.orm.plugins.common.collection.wrapped;

class FloatWrappedArrayList extends PrimitiveWrappedArrayList<Float, float[]> {

  public FloatWrappedArrayList(int size) {
    this(new float[size]);
  }

  public FloatWrappedArrayList(float[] array) {
    super(array);
  }

  @Override
  protected Float get(float[] array, int index) {
    return array[index];
  }

  @Override
  protected int size(float[] array) {
    return array.length;
  }

  @Override
  protected void set(float[] array, int index, Float element) {
    array[index] = element;
  }
}
