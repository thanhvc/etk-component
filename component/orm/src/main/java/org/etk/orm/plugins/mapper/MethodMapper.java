package org.etk.orm.plugins.mapper;

import java.lang.reflect.Method;

import org.etk.orm.core.DomainSession;
import org.etk.orm.core.EntityContext;
import org.etk.orm.core.MethodInvoker;
import org.etk.orm.core.ObjectContext;
import org.etk.reflect.api.ClassTypeInfo;
import org.etk.reflect.api.MethodInfo;


public class MethodMapper<C extends ObjectContext<C>> implements MethodInvoker<C> {

  /** . */
  private final MethodInfo method;

  public MethodMapper(MethodInfo method) {
    this.method = method;
  }

  public Object invoke(C context) throws Throwable {
    throw new UnsupportedOperationException();
  }

  public Object invoke(C context, Object args) throws Throwable {
    throw new UnsupportedOperationException();
  }

  public Object invoke(C context, Object[] args) throws Throwable {
    throw new UnsupportedOperationException();
  }

  public MethodInfo getMethod() {
    return method;
  }

  @Override
  public String toString() {
    return "MethodMapper[" + ((Method)method.unwrap()).getDeclaringClass().getName() + "#" + method.getName() + "]";
  }

  public static class Create<C extends ObjectContext<C>> extends MethodMapper<C> {

    /** . */
    ObjectMapper<C> mapper;

    public Create(MethodInfo method) {
      super(method);
    }

    public Object invoke(C ctx) throws Throwable {
      return invoke(ctx, (Object)null);
    }

    public Object invoke(C ctx, Object arg) throws Throwable {
      String name = null;
      if (arg instanceof String) {
        name = (String)arg;
      }
      EntityContext entityCtx = ctx.getEntity();
      DomainSession session = entityCtx.getSession();
      Class<?> clazz = mapper.getObjectClass();
      ObjectContext created = session.create(clazz, name);
      return created.getObject();
    }

    @Override
    public Object invoke(C ctx, Object[] args) {
      throw new AssertionError();
    }
  }

  public static class FindById<C extends ObjectContext<C>> extends MethodMapper<C> {

    /** . */
    private final Class<?> typeClass;

    public FindById(MethodInfo method, ClassTypeInfo type) throws ClassNotFoundException {
      super(method);

      //
      this.typeClass = (Class<?>)type.unwrap();
    }

    public Object invoke(C ctx) throws Throwable {
      throw new AssertionError();
    }

    public Object invoke(C ctx, Object arg) throws Throwable {
      String id = (String)arg;
      Object o = ctx.getEntity().getSession().findById(Object.class, id);
      if (typeClass.isInstance(o)) {
        return o;
      } else {
        return null;
      }
    }

    @Override
    public Object invoke(C ctx, Object[] args) throws Throwable {
      throw new AssertionError();
    }
  }

  public static class Destroy extends MethodMapper<EntityContext> {

    public Destroy(MethodInfo method) {
      super(method);
    }

    public Object invoke(EntityContext ctx) throws Throwable {
      ctx.getEntity().remove();
      return null;
    }

    public Object invoke(EntityContext ctx, Object arg) throws Throwable {
      throw new AssertionError();
    }

    @Override
    public Object invoke(EntityContext ctx, Object[] args) throws Throwable {
      throw new AssertionError();
    }
  }
}