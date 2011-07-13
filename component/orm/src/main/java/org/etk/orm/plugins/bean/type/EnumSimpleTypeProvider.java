package org.etk.orm.plugins.bean.type;


class EnumSimpleTypeProvider<E extends Enum<E>> extends SimpleTypeProvider.STRING<E> {

  /** . */
  private final Class<E> externalType;

  public EnumSimpleTypeProvider(Class<E> externalType) {
    this.externalType = externalType;
  }

  @Override
  public String getInternal(E e) throws TypeConversionException {
    return e.name();
  }

  @Override
  public E getExternal(String s) throws TypeConversionException {
    try {
      return Enum.valueOf(externalType, s);
    }
    catch (IllegalArgumentException e) {
      throw new IllegalStateException("Enum value cannot be determined from the stored value", e);
    }
  }

  @Override
  public E fromString(String s) throws TypeConversionException {
    return getExternal(s);
  }

  @Override
  public String toString(E e) throws TypeConversionException {
    return getInternal(e);
  }

  @Override
  public Class<E> getExternalType() {
    return externalType;
  }
}

