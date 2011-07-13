package org.etk.orm.plugins.bean.type;

import java.io.InputStream;
import java.util.Calendar;


/**
 * <p>The base class for the simple type Service Provider Interface. A provider performs a bidiectional conversion
 * between an internal type and an external type, in addition a provider must be able to convert a string
 * value to an external representation and vice-versa.</p>
 *
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 * @param <I> the generic internal type
 * @param <E> the generic external type
 */
public abstract class SimpleTypeProvider<I, E> {

  private SimpleTypeProvider() {
  }

  /**
   * Returns the internal class type.
   *
   * @return the internal class type
   */
  public abstract Class<I> getInternalType();

  /**
   * Returns the external class type.
   *
   * @return the external class type
   */
  public abstract Class<E> getExternalType();

  /**
   * Provide an internal representation of an external value.
   *
   * @param e the external value
   * @return the internal value
   * @throws TypeConversionException anything that would prevent the conversion
   */
  public abstract I getInternal(E e) throws TypeConversionException;

  /**
   * Provide an external representation of an internal value.
   *
   * @param i the internal value
   * @return the external value
   * @throws TypeConversionException anything that would prevent the conversion
   */
  public abstract E getExternal(I i) throws TypeConversionException;

  /**
   * Provide an external representation of a string value.
   *
   * @param s the string value
   * @return the external value
   * @throws TypeConversionException anything that would prevent the conversion
   */
  public abstract E fromString(String s) throws TypeConversionException;

  /**
   * Provide a string representation of an external value.
   *
   * @param e the external value
   * @return the string value
   * @throws TypeConversionException anything that would prevent the conversion
   */
  public abstract String toString(E e) throws TypeConversionException;

  public abstract static class STRING<E> extends SimpleTypeProvider<String, E> {
    protected STRING() {
    }
    @Override
    public final Class<String> getInternalType() {
      return String.class;
    }
  }

  public abstract static class BINARY<E> extends SimpleTypeProvider<InputStream, E> {
    protected BINARY() {
    }
    @Override
    public final Class<InputStream> getInternalType() {
      return InputStream.class;
    }
  }

  public abstract static class LONG<E> extends SimpleTypeProvider<Long, E> {
    protected LONG() {
    }
    @Override
    public final Class<Long> getInternalType() {
      return Long.class;
    }
  }

  public abstract static class DOUBLE<E> extends SimpleTypeProvider<Double, E> {
    protected DOUBLE() {
    }
    @Override
    public final Class<Double> getInternalType() {
      return Double.class;
    }
  }

  public abstract static class DATE<E> extends SimpleTypeProvider<Calendar, E> {
    protected DATE() {
    }
    @Override
    public final Class<Calendar> getInternalType() {
      return Calendar.class;
    }
  }

  public abstract static class BOOLEAN<E> extends SimpleTypeProvider<Boolean, E> {
    protected BOOLEAN() {
    }
    @Override
    public final Class<Boolean> getInternalType() {
      return Boolean.class;
    }
  }

  public abstract static class NAME<E> extends SimpleTypeProvider<String, E> {
    protected NAME() {
    }
    @Override
    public final Class<String> getInternalType() {
      return String.class;
    }
  }

  public abstract static class PATH<E> extends SimpleTypeProvider<String, E> {
    protected PATH() {
    }
    @Override
    public final Class<String> getInternalType() {
      return String.class;
    }
  }
}

