package org.etk.orm.plugins.common.collection.wrapped;


class DoubleWrappedArrayList extends PrimitiveWrappedArrayList<Double, double[]> {

  public DoubleWrappedArrayList(int size) {
    this(new double[size]);
  }

  public DoubleWrappedArrayList(double[] array) {
    super(array);
  }

  @Override
  protected Double get(double[] array, int index) {
    return array[index];
  }

  @Override
  protected int size(double[] array) {
    return array.length;
  }

  @Override
  protected void set(double[] array, int index, Double element) {
    array[index] = element;
  }
}