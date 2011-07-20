package org.etk.reflection.test;

import java.util.Set;

public abstract class UnitTestPlugin {
  protected abstract void execute(ReflectUnitTest unitTest, Set<Class<?>> classes) throws Exception;

}
