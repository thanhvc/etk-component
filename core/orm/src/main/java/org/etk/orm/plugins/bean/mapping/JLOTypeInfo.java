package org.etk.orm.plugins.bean.mapping;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.etk.reflect.api.ClassTypeInfo;
import org.etk.reflect.api.FieldInfo;
import org.etk.reflect.api.MethodInfo;
import org.etk.reflect.api.MethodSignature;
import org.etk.reflect.api.TypeInfo;
import org.etk.reflect.api.TypeVariableInfo;
import org.etk.reflect.api.Visitor;
import org.etk.reflect.api.VisitorStrategy;
import org.etk.reflect.api.definition.ClassKind;
import org.etk.reflect.api.relationship.TypeRelationship;
import org.etk.reflect.core.AnnotationType;


public class JLOTypeInfo implements ClassTypeInfo {

  /** . */
  private static final JLOTypeInfo instance = new JLOTypeInfo();

  private JLOTypeInfo() {
  }

  public static JLOTypeInfo get() {
    return instance;
  }

  public Collection<FieldInfo> getDeclaredFields() {
    return Collections.emptyList();
  }

  public FieldInfo getDeclaredField(String fieldName) {
    return null;
  }

  public boolean isReified() {
    return true;
  }

  public ClassTypeInfo getEnclosing() {
    return null;
  }

  public String getName() {
    return Object.class.getName();
  }

  public String getSimpleName() {
    return Object.class.getSimpleName();
  }

  public String getPackageName() {
    return Object.class.getPackage().getName();
  }

  public ClassKind getKind() {
    return ClassKind.CLASS;
  }

  public Iterable<TypeInfo> getInterfaces() {
    return Collections.emptyList();
  }

  public ClassTypeInfo getSuperClass() {
    return null;
  }

  public TypeInfo getSuperType() {
    return null;
  }

  public TypeInfo resolve(TypeInfo type) {
    throw new UnsupportedOperationException();
  }

  public List<MethodInfo> getDeclaredMethods() {
    return Collections.emptyList();
  }

  public MethodInfo getDeclaredMethod(MethodSignature methodSignature) {
    return null;
  }

  public <A> A getDeclaredAnnotation(AnnotationType<A, ?> annotationType) {
    return null;
  }

  public boolean isAssignableFrom(ClassTypeInfo that) {
    return false;
  }

  public Object unwrap() {
    return Object.class;
  }

  public List<TypeVariableInfo> getTypeParameters() {
    return Collections.emptyList();
  }

  public <V extends Visitor<V, S>, S extends VisitorStrategy<V, S>> void accept(S strategy, V visitor) {
    strategy.visit(this, visitor);
  }

  public boolean isSubType(TypeInfo typeInfo) {
    return TypeRelationship.SUB_TYPE.isSatisfied(this, typeInfo);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj instanceof ClassTypeInfo) {
      ClassTypeInfo that = (ClassTypeInfo)obj;
      String thatName = that.getName();
      return Object.class.getName().equals(thatName);
    }
    return false;
  }

  @Override
  public int hashCode() {
    return Object.class.getName().hashCode();
  }
}
