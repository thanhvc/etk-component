package org.etk.orm.core;


public interface MethodInvoker<O extends ObjectContext> {

  Object invoke(O ctx) throws Throwable;

  Object invoke(O ctx, Object arg) throws Throwable;

  Object invoke(O ctx, Object[] args) throws Throwable;

}

