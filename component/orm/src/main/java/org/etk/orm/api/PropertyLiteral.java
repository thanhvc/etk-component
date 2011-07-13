package org.etk.orm.api;

public class PropertyLiteral<O, P> {

  /** . */
  private final Class<O> owner;

  /** . */
  private final String name;

  /** . */
  private final Class<P> javaType;

  /**
   * Build a new property literal.
   *
   * @param owner the property owner
   * @param name the property name
   * @param javaType the property java type
   * @throws NullPointerException if any argument is null
   */
  public PropertyLiteral(
    Class<O> owner,
    String name,
    Class<P> javaType) throws NullPointerException {
    if (owner == null) {
      throw new NullPointerException("No null owner type accepted");
    }
    if (name == null) {
      throw new NullPointerException("No null name accepted");
    }
    if (javaType == null) {
      throw new NullPointerException("No null java type accepted");
    }

    //
    this.owner = owner;
    this.name = name;
    this.javaType = javaType;
  }

  public Class<O> getOwner() {
    return owner;
  }

  public String getName() {
    return name;
  }

  public Class<P> getJavaType() {
    return javaType;
  }

  @Override
  public String toString() {
    return "PropertyLiteral[owner=" + owner.getName() + ",name=" + name + ",type=" + javaType.getName() + "]";
  }
}
