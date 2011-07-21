package org.etk.java5.reflection.test;

import java.util.Set;

public abstract class UnitTestPlugin {
  protected abstract void execute(ReflectUnitTest unitTest, Set<Class<?>> classes) throws Exception;

}
