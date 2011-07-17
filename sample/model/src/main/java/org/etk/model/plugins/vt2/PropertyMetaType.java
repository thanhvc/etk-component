package org.etk.model.plugins.vt2;

import java.util.Calendar;

import javax.jcr.PropertyType;
import javax.jcr.RepositoryException;
import javax.jcr.Value;
import javax.jcr.ValueFactory;
import javax.jcr.ValueFormatException;


/**
 * A property meta type is a representation of the JCR property types defined by {@link javax.jcr.PropertyType}.
 *
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 * @param <V> the java type modeling the type
 */
public abstract class PropertyMetaType<V> {

  /** . */
  public static final PropertyMetaType<String> STRING = new PropertyMetaType<String>(String.class, PropertyType.STRING) {
    @Override
    public Value getValue(ValueFactory factory, String s) throws ValueFormatException {
      return factory.createValue(s, PropertyType.STRING);
    }
    @Override
    public String getValue(Object value) throws RepositoryException {
      return (String) value;
    }
  };

   /** . */
  public static final PropertyMetaType<String> NAME = new PropertyMetaType<String>(String.class, PropertyType.NAME) {
    @Override
    public Value getValue(ValueFactory factory, String s) throws ValueFormatException {
      return factory.createValue(s, PropertyType.NAME);
    }
    @Override
    public String getValue(Object value) throws RepositoryException {
      return (String) value;
    }
  };

  /** . */
  public static final PropertyMetaType<Long> LONG = new PropertyMetaType<Long>(Long.class, PropertyType.LONG) {
    @Override
    public Value getValue(ValueFactory factory, Long aLong) throws ValueFormatException {
      return factory.createValue(aLong);
    }
    @Override
    public Long getValue(Object value) throws RepositoryException {
      return (Long) value;
    }
  };

  /** . */
  public static final PropertyMetaType<Double> DOUBLE = new PropertyMetaType<Double>(Double.class, PropertyType.DOUBLE) {
    @Override
    public Value getValue(ValueFactory factory, Double aDouble) throws ValueFormatException {
      return factory.createValue(aDouble);
    }
    @Override
    public Double getValue(Object value) throws RepositoryException {
      return (Double)value;
    }
  };

  /** . */
  public static final PropertyMetaType<Boolean> BOOLEAN = new PropertyMetaType<Boolean>(Boolean.class, PropertyType.BOOLEAN) {
    @Override
    public Value getValue(ValueFactory factory, Boolean aBoolean) throws ValueFormatException {
      return factory.createValue(aBoolean);
    }
    @Override
    public Boolean getValue(Object value) throws RepositoryException {
      return (Boolean) value;
    }
  };

  /** . */
  public static final PropertyMetaType<Calendar> DATE = new PropertyMetaType<Calendar>(Calendar.class, PropertyType.DATE) {
    @Override
    public Value getValue(ValueFactory factory, Calendar date) throws ValueFormatException {
      return factory.createValue(date);
    }
    @Override
    public Calendar getValue(Object value) throws RepositoryException {
      return (Calendar) value;
    }
  };

  /** . */
  private static final PropertyMetaType<?>[] ALL = {
    STRING,
    NAME,
    LONG,
    DOUBLE,
    BOOLEAN,
    DATE
  };

  public static PropertyMetaType<?> get(int code) {
    for (PropertyMetaType<?> pt : ALL) {
      if (pt.code == code) {
        return pt;
      }
    }
    return null;
  }

  /** The java type associated with the type. */
  private final Class<V> javaValueType;

  /**
   * The JCR type code among the values:
   * <ul>
   *   <li>{@link PropertyType#STRING}</li>
   *   <li>{@link PropertyType#BINARY}</li>
   *   <li>{@link PropertyType#LONG}</li>
   *   <li>{@link PropertyType#DOUBLE}</li>
   *   <li>{@link PropertyType#DATE}</li>
   *   <li>{@link PropertyType#BOOLEAN}</li>
   *   <li>{@link PropertyType#NAME}</li>
   *   <li>{@link PropertyType#PATH}</li>
   *   <li>{@link PropertyType#REFERENCE}</li>
   * </ul>
   */
  private final int code;

  private PropertyMetaType(Class<V> javaValueType, int code) {
    this.javaValueType = javaValueType;
    this.code = code;
  }

  /**
   * Converts the Java value to the {@link Value}.
   *
   * @param factory the JCR value factory required to create the value
   * @param v the Java value
   * @return the JCR value
   * @throws ValueFormatException thrown by the factory
   */
  public abstract Value getValue(ValueFactory factory, V v) throws ValueFormatException;

  /**
   * Converts the {@link Value} to the java value.
   *
   * @param value the JCR value
   * @return the Java value
   * @throws RepositoryException thrown by the conversion
   */
  public abstract V getValue(Object value) throws RepositoryException;

  /**
   * Returns the Java value type modelling the property type.
   *
   * @return the Java value type modelling the property type
   */
  public Class<V> getJavaValueType() {
    return javaValueType;
  }

  /**
   * Returns the JCR property type as defined by {@link PropertyType}
   *
   * @return the JCR property type
   */
  public int getCode() {
    return code;
  }
}

