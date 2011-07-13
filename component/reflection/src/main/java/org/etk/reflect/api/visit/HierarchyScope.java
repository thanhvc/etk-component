package org.etk.reflect.api.visit;

import org.etk.reflect.api.ClassTypeInfo;
import org.etk.reflect.api.definition.ClassKind;

public enum HierarchyScope {

  CLASS() {
    @Override
    public <V extends HierarchyVisitor<V>> HierarchyVisitorStrategy<V> get() {
      return new HierarchyVisitorStrategy<V>() {
        boolean done = false;
        @Override
        protected boolean accept(ClassTypeInfo type) {
          if (done) {
            return false;
          } else {
            done = true;
            return true;
          }
        }
        @Override
        protected void leave(ClassTypeInfo type) {
          done = false;
        }
      };
    }},

  ANCESTORS() {
    @Override
    public <V extends HierarchyVisitor<V>> HierarchyVisitorStrategy<V> get() {
      @SuppressWarnings("unchecked")
      HierarchyVisitorStrategy<V> tmp = ancestors;
      return tmp;
    }},

  ALL() {
    @Override
    public <V extends HierarchyVisitor<V>> HierarchyVisitorStrategy<V> get() {
      @SuppressWarnings("unchecked")
      HierarchyVisitorStrategy tmp = all;
      return tmp;
    }},

  NOT_OBJECT() {
    @Override
    public <V extends HierarchyVisitor<V>> HierarchyVisitorStrategy<V> get() {
      @SuppressWarnings("unchecked")
      HierarchyVisitorStrategy tmp = notObject;
      return tmp;
    }}

  ;

  /** . */
  private static final HierarchyVisitorStrategy ancestors = new HierarchyVisitorStrategy() {
    @Override
    protected boolean accept(ClassTypeInfo type) {
      return type.getKind() == ClassKind.CLASS;
    }
  };

  /** . */
  private static final HierarchyVisitorStrategy all = new HierarchyVisitorStrategy() {
    @Override
    protected boolean accept(ClassTypeInfo type) {
      return true;
    }
  };

  /** . */
  private static final HierarchyVisitorStrategy notObject = new HierarchyVisitorStrategy() {
    @Override
    protected boolean accept(ClassTypeInfo type) {
      return !type.getName().equals(Object.class.getName());
    }
  };


  public abstract <V extends HierarchyVisitor<V>> HierarchyVisitorStrategy<V> get();

}
