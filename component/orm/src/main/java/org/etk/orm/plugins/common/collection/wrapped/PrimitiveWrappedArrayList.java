package org.etk.orm.plugins.common.collection.wrapped;


abstract class PrimitiveWrappedArrayList<E, A> extends WrappedArrayList<E, A> {

  protected PrimitiveWrappedArrayList(A array) {
    super(array);
  }
}
