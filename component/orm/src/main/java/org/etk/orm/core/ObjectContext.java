package org.etk.orm.core;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;

import javax.jcr.RepositoryException;

import org.etk.orm.api.ORMIOException;
import org.etk.orm.api.Status;
import org.etk.orm.plugins.common.CloneableInputStream;
import org.etk.orm.plugins.common.jcr.Path;
import org.etk.orm.plugins.instrument.MethodHandler;
import org.etk.orm.plugins.jcr.NodeTypeInfo;
import org.etk.orm.plugins.mapper.ObjectMapper;
import org.etk.orm.plugins.vt2.ValueDefinition;


public abstract class ObjectContext<O extends ObjectContext<O>> implements MethodHandler {

  public abstract ObjectMapper<O> getMapper();

  public abstract Object getObject();

  public abstract EntityContext getEntity();

  /**
   * Returns the type info associated with the context. Null is returned when the context is in transient
   * state, otherwise the type info of the corresponding node is returned.
   *
   * @return the type info
   */
  public abstract NodeTypeInfo getTypeInfo();

  public abstract Status getStatus();

  public abstract DomainSession getSession();

  public final Object invoke(Object o, Method method) throws Throwable {
    MethodInvoker<O> invoker = getMapper().getInvoker(method);
    if (invoker != null) {
      return invoker.invoke((O)this);
    } else {
      throw createCannotInvokeError(method);
    }
  }

  public final Object invoke(Object o, Method method, Object arg) throws Throwable {
    MethodInvoker<O> invoker = getMapper().getInvoker(method);
    if (invoker != null) {
      return invoker.invoke((O)this, arg);
    } else {
      throw createCannotInvokeError(method, arg);
    }
  }

  public final Object invoke(Object o, Method method, Object[] args) throws Throwable {
    MethodInvoker<O> invoker = getMapper().getInvoker(method);
    if (invoker != null) {
      switch (args.length) {
        case 0:
          return invoker.invoke((O)this);
        case 1:
          return invoker.invoke((O)this, args[0]);
        default:
          return invoker.invoke((O)this, args);
      }
    } else {
      throw createCannotInvokeError(method, (Object[])args);
    }
  }

  private AssertionError createCannotInvokeError(Method method, Object... args) {
    StringBuilder msg = new StringBuilder("Cannot invoke method ").append(method.getName()).append("(");
    Class[] parameterTypes = method.getParameterTypes();
    for (int i = 0;i < parameterTypes.length;i++) {
      if (i > 0) {
        msg.append(',');
      }
      msg.append(parameterTypes[i].getName());
    }
    msg.append(") with arguments (");
    for (int i = 0;i < args.length;i++) {
      if (i > 0) {
        msg.append(',');
      }
      msg.append(String.valueOf(args[i]));
    }
    msg.append(")");
    return new AssertionError(msg);
  }

  public final <V> V getPropertyValue(String propertyName, ValueDefinition<?, V> type) throws RepositoryException {
    EntityContext ctx = getEntity();
    EntityContextState state = ctx.state;

    //
    propertyName = state.getSession().domain.encodeName(ctx, propertyName, NameKind.PROPERTY);
    Path.validateName(propertyName);

    //
    NodeTypeInfo typeInfo = getTypeInfo();
    return state.getPropertyValue(typeInfo, propertyName, type);
  }

  public final <V> List<V> getPropertyValues(String propertyName, ValueDefinition<?, V> simpleType, ListType listType) throws RepositoryException {
    EntityContext ctx = getEntity();
    EntityContextState state = ctx.state;

    //
    propertyName = state.getSession().domain.encodeName(ctx, propertyName, NameKind.PROPERTY);
    Path.validateName(propertyName);

    //
    NodeTypeInfo typeInfo = getTypeInfo();
    return state.getPropertyValues(typeInfo, propertyName, simpleType, listType);
  }

  public final <V> void setPropertyValue(String propertyName, ValueDefinition<?, V> type, V o) throws RepositoryException {
    EntityContext ctx = getEntity();
    EntityContextState state = ctx.state;

    //
    propertyName = state.getSession().domain.encodeName(ctx, propertyName, NameKind.PROPERTY);
    Path.validateName(propertyName);

    //
    Object object = getObject();

    //
    EventBroadcaster broadcaster = state.getSession().broadcaster;

    //
    NodeTypeInfo typeInfo = getTypeInfo();

    //
    if (o instanceof InputStream && broadcaster.hasStateChangeListeners()) {
      CloneableInputStream in;
      try {
        in = new CloneableInputStream((InputStream)o);
      }
      catch (IOException e) {
        throw new ORMIOException("Could not read stream", e);
      }
      @SuppressWarnings("unchecked") V v = (V)in;
      state.setPropertyValue(typeInfo, propertyName, type, v);
      broadcaster.propertyChanged(state.getId(), object, propertyName, in.clone());
    } else {
      state.setPropertyValue(typeInfo, propertyName, type, o);
      broadcaster.propertyChanged(state.getId(), object, propertyName, o);
    }
  }

  public final <V> void setPropertyValues(String propertyName, ValueDefinition<?, V> type, ListType listType, List<V> objects) throws RepositoryException {
    EntityContext ctx = getEntity();
    EntityContextState state = ctx.state;

    //
    propertyName = state.getSession().domain.encodeName(ctx, propertyName, NameKind.PROPERTY);
    Path.validateName(propertyName);

    //
    NodeTypeInfo typeInfo = getTypeInfo();

    //
    state.setPropertyValues(typeInfo, propertyName, type, listType, objects);
  }

  public final void removeChild(String prefix, String localName) {
    if (getStatus() != Status.PERSISTENT) {
      throw new IllegalStateException("Can only insert/remove a child of a persistent object");
    }

    //
    getSession().removeChild(this, prefix, localName);
  }

  public final void orderBefore(EntityContext srcCtx, EntityContext dstCtx) {
    getSession().orderBefore(this, srcCtx, dstCtx);
  }

  public final <T1 extends Throwable, T2 extends Throwable> void addChild(
      ThrowableFactory<T1> thisStateTF,
      ThrowableFactory<T2> childStateTF,
      String prefix,
      EntityContext childCtx) throws T1, T2 {
    String localName = childCtx.getLocalName();
    addChild(thisStateTF, childStateTF, prefix, localName, childCtx);
  }

  public final <T1 extends Throwable, T2 extends Throwable> void addChild(
      ThrowableFactory<T1> thisStateTF,
      ThrowableFactory<T2> childStateTF,
      String prefix,
      String localName,
      EntityContext childCtx) throws T1, T2 {
    if (childCtx.getStatus() == Status.PERSISTENT) {
      getSession().move(childStateTF, thisStateTF, childCtx, this, prefix, localName);
    } else {
      getSession().persist(thisStateTF, childStateTF, ThrowableFactory.NPE, this, childCtx, prefix, localName);
    }
  }

  public final EntityContext getChild(String prefix, String localName) {
    return getSession().getChild(this, prefix, localName);
  }

  public final <T> Iterator<T> getChildren(Class<T> filterClass) {
    return getSession().getChildren(this, filterClass);
  }
}
