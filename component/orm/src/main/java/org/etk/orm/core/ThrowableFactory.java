package org.etk.orm.core;

import org.etk.orm.plugins.common.TypeLiteral;


public abstract class ThrowableFactory<E extends Throwable> {

  public E newThrowable() {
    return newThrowable(null, null);
  }

  public E newThrowable(String msg) {
    return newThrowable(null, msg);
  }

  public E newThrowable(Throwable cause) {
    return newThrowable(cause, null);
  }

  public String toString() {
    return "ThrowableFactory[" + TypeLiteral.get(getClass(), 0).getSimpleName() + "]";
  }

  public abstract E newThrowable(Throwable cause, String msg);

  public static ThrowableFactory<NullPointerException> NPE = new ThrowableFactory<NullPointerException>() {
    @Override
    public NullPointerException newThrowable(Throwable cause, String msg) {
      NullPointerException iae = new NullPointerException(msg);
      iae.initCause(cause);
      return iae;
    }
  };

  public static ThrowableFactory<IllegalArgumentException> IAE = new ThrowableFactory<IllegalArgumentException>() {
    @Override
    public IllegalArgumentException newThrowable(Throwable cause, String msg) {
      IllegalArgumentException iae = new IllegalArgumentException(msg);
      iae.initCause(cause);
      return iae;
    }
  };

  public static ThrowableFactory<IllegalStateException> ISE = new ThrowableFactory<IllegalStateException>() {
    @Override
    public IllegalStateException newThrowable(Throwable cause, String msg) {
      IllegalStateException ise = new IllegalStateException(msg);
      ise.initCause(cause);
      return ise;
    }
  };

  public static ThrowableFactory<AssertionError> ASSERT = new ThrowableFactory<AssertionError>() {
    @Override
    public AssertionError newThrowable(Throwable cause, String msg) {
      AssertionError ae = new AssertionError(msg);
      ae.initCause(cause);
      return ae;
    }
  };

  public static ThrowableFactory<UnsupportedOperationException> UNSUPPORTED = new ThrowableFactory<UnsupportedOperationException>() {
    @Override
    public UnsupportedOperationException newThrowable(Throwable cause, String msg) {
      UnsupportedOperationException ae = new UnsupportedOperationException(msg);
      ae.initCause(cause);
      return ae;
    }
  };

  public static ThrowableFactory<UnsupportedOperationException> TODO = new ThrowableFactory<UnsupportedOperationException>() {
    @Override
    public UnsupportedOperationException newThrowable(Throwable cause, String msg) {
      UnsupportedOperationException ae = new UnsupportedOperationException(msg != null ? ("todo :" + msg) : "todo");
      ae.initCause(cause);
      return ae;
    }
  };
}