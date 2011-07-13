package org.etk.orm.plugins.bean;


public abstract class ValueKind {

  private ValueKind() {
  }

  @Override
  public String toString() {
    return getClass().getSimpleName() + "[]";
  }

  public static class Single extends ValueKind {
    private Single() {
    }
  }

  public static class Multi extends ValueKind {
    private Multi() {
    }
  }

  public static class Array extends Multi {
    private Array() {
    }
  }

  public static class Collection extends Multi {
    private Collection() {
    }
  }

  public static class List extends Multi {
    private List() {
    }
  }

  public static class Map extends Multi {
    private Map() {
    }
  }

  /** . */
  public static final Single SINGLE = new Single();

  /** . */
  public static final Array ARRAY = new Array();

  /** . */
  public static final Collection COLLECTION = new Collection();

  /** . */
  public static final List LIST = new List();

  /** . */
  public static final Map MAP = new Map();

}
