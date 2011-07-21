package org.etk.reflect.api.introspection;

import java.beans.Introspector;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.etk.reflect.api.ClassTypeInfo;
import org.etk.reflect.api.MethodInfo;
import org.etk.reflect.api.ParameterizedTypeInfo;
import org.etk.reflect.api.TypeInfo;
import org.etk.reflect.api.visit.HierarchyScope;
import org.etk.reflect.api.visit.HierarchyVisitor;
import org.etk.reflect.api.visit.HierarchyVisitorStrategy;
import org.etk.reflect.core.AnnotationType;

public class MethodIntrospector {

  /** . */
  private final HierarchyVisitorStrategy strategy;

  /** . */
  private final boolean removeOverrides;

  /**
   * Creates an introspector with the specified hierarchy visitor strategy and the specified removeOverrides parameter.
   *
   * @param strategy the hierarchy visitor strategy
   * @param removeOverrides the remove overrides
   * @throws NullPointerException if the hierarchy scope is null
   */
  public MethodIntrospector(HierarchyVisitorStrategy strategy, boolean removeOverrides) throws NullPointerException {
    if (strategy == null) {
      throw new NullPointerException();
    }

    // OK I think
    this.strategy = strategy;
    this.removeOverrides = removeOverrides;
  }

  /**
   * Creates an introspector with the specified hierarchy scope and the specified removeOverrides parameter.
   *
   * @param hierarchyScope the hierarchy scope
   * @param removeOverrides the remove overrides
   * @throws NullPointerException if the hierarchy scope is null
   */
  public MethodIntrospector(HierarchyScope hierarchyScope, boolean removeOverrides) throws NullPointerException {
    if (hierarchyScope == null) {
      throw new NullPointerException();
    }

    //
    this.strategy = hierarchyScope.<AbstractScopedHierarchyVisitor>get();
    this.removeOverrides = removeOverrides;
  }

  /**
   * Builds a new introspector with the specified hierarchy scope and a removeOverrides parameter set to false.
   *
   * @param hierarchyScope the hierarchy scope
   * @throws NullPointerException if the hierarchy scope is null
   */
  public MethodIntrospector(HierarchyScope hierarchyScope) throws NullPointerException {
    this(hierarchyScope, false);
  }

  /**
   * Builds a new introspector with the specified hierarchy visitor strategy and a removeOverrides parameter set to false.
   *
   * @param strategy the hierarchy visitor strategy
   * @throws NullPointerException if the hierarchy scope is null
   */
  public MethodIntrospector(HierarchyVisitorStrategy strategy) throws NullPointerException {
    this(strategy, false);
  }

  public <A> Collection<AnnotationTarget<MethodInfo, A>> resolveMethods(ClassTypeInfo cti, AnnotationType<A, ?> annotationClass) {
    ArrayList<AnnotationTarget<MethodInfo, A>> methods = new ArrayList<AnnotationTarget<MethodInfo, A>>();
    AnnotationIntrospector<A> introspector = new AnnotationIntrospector<A>(annotationClass);
    for (MethodInfo method : getMethods(cti)) {
      AnnotationTarget<MethodInfo, A> annotation = introspector.resolve(method);
      if (annotation != null) {
        methods.add(annotation);
      }
    }
    return methods;
  }

  /**
   * Returns a map of all method info getters on the specified class type info.
   *
   * @param classTypeInfo the class type info
   * @return an iterable of the method info getters
   * @throws NullPointerException if the specified class type info is null
   */
  public Map<String, MethodInfo> getGetterMap(ClassTypeInfo classTypeInfo) throws NullPointerException {
    if (classTypeInfo == null) {
      throw new NullPointerException();
    }
    Map<String, MethodInfo> getterMap = new LinkedHashMap<String, MethodInfo>();
    for (MethodInfo getter : getGetters(classTypeInfo)) {
      String getterName = getter.getName();
      String name;
      if (getterName.startsWith("get")) {
        name = Introspector.decapitalize(getterName.substring(3));
      } else {
        name = Introspector.decapitalize(getterName.substring(2));
      }
      getterMap.put(name, getter);
    }
    return getterMap;
  }

  /**
   * Find all method info getters on the specified class type info.
   *
   * @param classTypeInfo the class type info
   * @return an iterable of the method info getters
   * @throws NullPointerException if the specified class type info is null
   */
  public Iterable<MethodInfo> getGetters(ClassTypeInfo classTypeInfo) throws NullPointerException {
    if (classTypeInfo == null) {
      throw new NullPointerException();
    }
    final MethodContainer getters = new MethodContainer();
    HierarchyVisitor visitor = new AbstractScopedHierarchyVisitor(classTypeInfo) {
      public void leave(ClassTypeInfo type) {
        if (!type.getName().equals(Object.class.getName())) {
          for (MethodInfo method : type.getDeclaredMethods()) {
            String methodName = method.getName();
            if (
              ((
                methodName.startsWith("get") && methodName.length() > 3 && Character.isUpperCase(methodName.charAt(3))) ||
                (methodName.startsWith("is") && methodName.length() > 2 && Character.isUpperCase(methodName.charAt(2)))) &&
              method.getParameterTypes().size() == 0) {
              getters.add(method);
            }
          }
        }
      }
    };

    //
    classTypeInfo.accept(strategy, visitor);

    //
    return getters;
  }

  /**
   * Returns a map of all method info setters on the specified class type info.
   *
   * @param classTypeInfo the class type info
   * @return an iterable of the method info setters
   * @throws NullPointerException if the specified class type info is null
   */
  public Map<String, Set<MethodInfo>> getSetterMap(ClassTypeInfo classTypeInfo) throws NullPointerException {
    Map<String, Set<MethodInfo>> setterMap = new LinkedHashMap<String, Set<MethodInfo>>();
    for (MethodInfo setter : getSetters(classTypeInfo)) {
      String name = Introspector.decapitalize(setter.getName().substring(3));
      Set<MethodInfo> setters = setterMap.get(name);
      if (setters == null) {
        setters = new LinkedHashSet<MethodInfo>();
        setterMap.put(name, setters);
      }
      setters.add(setter);
    }
    return setterMap;
  }

  /**
   * Find all method info setters on the specified class type info.
   *
   * @param classTypeInfo the class type info
   * @return an iterable of the method info setters
   * @throws NullPointerException if the specified class type info is null
   */
  public Iterable<MethodInfo> getSetters(ClassTypeInfo classTypeInfo) {
    if (classTypeInfo == null) {
      throw new NullPointerException();
    }
    final MethodContainer setters = new MethodContainer();
    HierarchyVisitor visitor = new AbstractScopedHierarchyVisitor(classTypeInfo) {
      public void leave(ClassTypeInfo type) {
        if (!type.getName().equals(Object.class.getName())) {
          for (MethodInfo method : type.getDeclaredMethods()) {
            String methodName = method.getName();
            if (
              methodName.startsWith("set") &&
              methodName.length() > 3 &&
              Character.isUpperCase(methodName.charAt(3)) &&
              method.getParameterTypes().size() == 1) {
              setters.add(method);
            }
          }
        }
      }
    };

    //
    classTypeInfo.accept(strategy, visitor);

    //
    return setters;
  }

  /**
   * Returns all method on the specified type info.
   *
   * @param typeInfo the type info
   * @return all the methods
   * @throws NullPointerException if the specified type info is null
   */
  public Set<MethodInfo> getMethods(TypeInfo typeInfo) throws NullPointerException {
    if (typeInfo == null) {
      throw new NullPointerException();
    }
    MethodContainer container;
    if (removeOverrides) {
      container = new MethodContainer((ClassTypeInfo)typeInfo);
    } else {
      container = new MethodContainer();
    }
    findMethods(typeInfo, container);
    return container.toCollection();
  }

  private void findMethods(TypeInfo ti, final MethodContainer container) {
    if (ti instanceof ClassTypeInfo) {
      strategy.visit(ti, new HierarchyVisitor() {
        public boolean enter(ClassTypeInfo type) {
          for (MethodInfo declaredMethod : type.getDeclaredMethods()) {
            container.add(declaredMethod);
          }
          return true;
        }

        public void leave(ClassTypeInfo type) {

        }
      });
    } else if (ti instanceof ParameterizedTypeInfo) {
      findMethods(((ParameterizedTypeInfo)ti).getRawType(), container);
    } else {
      throw new UnsupportedOperationException("Cannot get methods from type " + ti);
    }
  }
}