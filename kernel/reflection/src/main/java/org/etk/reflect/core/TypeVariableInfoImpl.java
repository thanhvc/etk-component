package org.etk.reflect.core;

import java.util.ArrayList;
import java.util.List;

import org.etk.reflect.api.ClassTypeInfo;
import org.etk.reflect.api.GenericDeclarationInfo;
import org.etk.reflect.api.MethodInfo;
import org.etk.reflect.api.TypeInfo;
import org.etk.reflect.api.TypeVariableInfo;
import org.etk.reflect.api.metadata.GenericDeclarationKind;


class TypeVariableInfoImpl<T, M, A, P, F> extends AbstractTypeInfo<T, M, A, P, F> implements TypeVariableInfo {
  private final T type;
  
  private final String name;
  
  private GenericDeclarationInfo genericDeclaration;
  
  private List<TypeInfo> bounds;
  
  public TypeVariableInfoImpl(TypeResolverImpl<T, M, A, P, F> domain, T type) {
    super(domain);
    
    this.name = domain.typeMetadata.getName(type);
    this.type = type;
    this.genericDeclaration = null;
    this.bounds = null;
  }
  
  public boolean isReified() {
    return false;
  }

  public String getName() {
    List<TypeInfo> bounds = getBounds();
    if (bounds.isEmpty()) {
      return Object.class.getName();
    } else {
      return bounds.get(0).getName();
    }
  }

  public T unwrap() {
    return type;
  }

  public List<TypeInfo> getBounds() {
    if (bounds == null) {
      ArrayList<TypeInfo> bounds = new ArrayList<TypeInfo>();
      for (T b : domain.typeMetadata.getBounds(type)) {
        AbstractTypeInfo<T, M, A, P, F> bound = domain.getType(b);
        bounds.add(bound);
      }
      this.bounds = bounds;
    }
    return bounds;
  }

  public GenericDeclarationInfo getGenericDeclaration() {
    if (genericDeclaration == null) {
      GenericDeclarationKind kind = domain.typeMetadata.getGenericDeclarationKind(type);
      switch (kind) {
        case TYPE:
          T gd = domain.typeMetadata.getGenericDeclaration(type);
          genericDeclaration = (ClassTypeInfo)domain.resolve(gd);
          break;
        case METHOD:
          M mgd = domain.methodMetadata.getGenericDeclaration(type);
          T omgd = domain.methodMetadata.getOwner(mgd);
          ClassTypeInfo tmp = (ClassTypeInfo)domain.resolve(omgd);
          for (MethodInfo mi : tmp.getDeclaredMethods()) {
            if (mi.unwrap().equals(mgd)) {
              genericDeclaration = mi;
            }
          }
          if (genericDeclaration == null) {
            throw new AssertionError("Need to handle that case which could happen due to covariant return types...");
          }
          break;
        default:
          throw new UnsupportedOperationException();
      }
    }
    return genericDeclaration;
  }

  public String getVariableName() {
    return name;
  }

  public int hashCode() {
    return name.hashCode() ^ getGenericDeclaration().hashCode();
  }

  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj instanceof TypeVariableInfo) {
      TypeVariableInfo that = (TypeVariableInfo)obj;
      GenericDeclarationInfo generidDeclaration = getGenericDeclaration();
      GenericDeclarationInfo thatGenericDeclaration = that.getGenericDeclaration();
      String thatName = that.getVariableName();
      return name.equals(thatName) && generidDeclaration.equals(thatGenericDeclaration);
    }
    return false;
  }

  @Override
  public String toString() {
    return "TypeVariableInfo[name=" + name + "]";
  }

}
