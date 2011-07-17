package org.etk.model.plugins.common;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import org.etk.orm.api.BuilderException;

public class ObjectInstantiator {

  /**
   * Intantiates the object with the specified class name. The object class must have a public no argument constructor.
   * 
   * @param className the class name
   * @param expectedClass the expected class
   * @param <T> the type of the class name
   * @return the object instance
   * @throws BuilderException if anything goes wrong
   */
  public static <T> T newInstance(String className, Class<T> expectedClass) throws BuilderException {
    if (className == null) {
      throw new NullPointerException("No null class name expected");
    }
    if (expectedClass == null) {
      throw new NullPointerException("No null expected class provided");
    }
    try {
      ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
      Class<?> loadedClass = classLoader.loadClass(className);
      if (expectedClass.isAssignableFrom(loadedClass)) {
        Class<? extends T> expectedSubclass = loadedClass.asSubclass(expectedClass);
        return newInstance(expectedSubclass);
      } else {
        throw new BuilderException("Class " + className + " does not implement the " + expectedClass.getName() + " interface");
      }
    }
    catch (ClassNotFoundException e) {
      throw new BuilderException("Could not load class " + className, e);
    }
  }

  /**
   * Intantiates the object with the specified class. The object class must have a public no argument constructor.
   * 
   * @param objectClass the objct class
   * @param <T> the type of the class name
   * @return the object instance
   * @throws BuilderException if anything goes wrong
   */
  public static <T> T newInstance(Class<T> objectClass) throws BuilderException {
    if (objectClass == null) {
      throw new NullPointerException("No null object class provided");
    }
    try {
      if (!Modifier.isPublic(objectClass.getModifiers())) {
        throw new BuilderException("The class " + objectClass.getName() + " must be public");
      }
      if (Modifier.isAbstract(objectClass.getModifiers())) {
        throw new BuilderException("The class " + objectClass.getName() + " must not be abstract");
      }
      Constructor<? extends T> ctor = objectClass.getConstructor();
      if (!Modifier.isPublic(ctor.getModifiers())) {
        throw new BuilderException("The class " + objectClass.getName() + " no arg constructor is not public");
      }
      return ctor.newInstance();
    }
    catch (InstantiationException e) {
      throw new BuilderException("Could not instanciate class " + objectClass.getName(), e);
    }
    catch (IllegalAccessException e) {
      throw new BuilderException("Could not instanciate class " + objectClass.getName(), e);
    }
    catch (NoSuchMethodException e) {
      throw new BuilderException("The class " + objectClass.getName() + " does not have a no argument constructor", e);
    }
    catch (InvocationTargetException e) {
      throw new BuilderException("The class " + objectClass.getName() + " construction threw an exception", e.getCause());
    }
  }
}