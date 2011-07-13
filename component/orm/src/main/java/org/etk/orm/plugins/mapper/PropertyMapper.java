package org.etk.orm.plugins.mapper;

import org.etk.orm.core.MethodInvoker;
import org.etk.orm.core.ObjectContext;
import org.etk.orm.plugins.bean.PropertyInfo;
import org.etk.orm.plugins.bean.ValueInfo;
import org.etk.orm.plugins.bean.ValueKind;
import org.etk.orm.plugins.bean.mapping.PropertyMapping;


public abstract class PropertyMapper<P extends PropertyInfo<V, K>, V extends ValueInfo, O extends ObjectContext<O>, K extends ValueKind> {

  /** . */
  protected final Class<O> contextType;

  /** . */
  protected final PropertyMapping<P, V, K> info;

  public PropertyMapper(Class<O> contextType, PropertyMapping<P, V, K> info) {
    this.contextType = contextType;
    this.info = info;
  }

  public Class<O> getType() {
    return contextType;
  }

  public PropertyMapping<P, V, K> getInfo() {
    return info;
  }

  public Object get(O context) throws Throwable {
    throw new UnsupportedOperationException();
  }

  public void set(O context, Object value) throws Throwable {
    throw new UnsupportedOperationException();
  }

  public MethodInvoker<O> getGetter() {
    return getter;
  }

  public MethodInvoker<O> getSetter() {
    return setter;
  }

  private final MethodInvoker<O> getter = new MethodInvoker<O>() {
    public Object invoke(O ctx) throws Throwable {
      return get(ctx);
    }
    public Object invoke(O ctx, Object arg) throws Throwable {
      throw new AssertionError();
    }
    public Object invoke(O ctx, Object[] args) throws Throwable {
      throw new AssertionError();
    }
  };

  private final MethodInvoker<O> setter = new MethodInvoker<O>() {
    public Object invoke(O ctx) throws Throwable {
      throw new AssertionError();
    }
    public Object invoke(O ctx, Object arg) throws Throwable {
      set(ctx, arg);
      return null;
    }
    public Object invoke(O ctx, Object[] args) throws Throwable {
      throw new AssertionError();
    }
  };
}
