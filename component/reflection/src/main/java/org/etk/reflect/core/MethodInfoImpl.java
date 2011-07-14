package org.etk.reflect.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.etk.reflect.api.ClassTypeInfo;
import org.etk.reflect.api.MethodInfo;
import org.etk.reflect.api.MethodSignature;
import org.etk.reflect.api.TypeInfo;
import org.etk.reflect.api.TypeVariableInfo;
import org.etk.reflect.api.definition.MethodKind;

public class MethodInfoImpl<T, M, A, P, F>extends ReflectedObject<T, M, A, P, F> implements MethodInfo {
  private static final List<String> NO_NAMES_AVAILABLE = new ArrayList<String>();
  private final M method;
  private final String name;
  private TypeInfo returnType;
  private List<TypeInfo> parameterTypes;
  
  private final AccessScope access;
  
  private final boolean _final;
  
  private final boolean _static;
  
  private final MethodKind type;
  private final ClassTypeInfo owner;
  private MethodSignature signature;
  private List<TypeVariableInfo> typeParameters;
  private List<String> parameterNames;
  
  private AnnotatedDelegate<T, M, A, P, F, M> annotatedDelegate;
  
  private List<ClassTypeInfo> thrownTypes;
  
  public MethodInfoImpl(ClassTypeInfo owner, TypeResolverImpl<T, M, A, P, F> domain, M method) {
    super(domain);
    
    MethodKind type;
    
    if (domain.methodMetadata.isAbstract(method)) {
      type = MethodKind.ABSTRACT;
    } else if (domain.methodMetadata.isNative(method)) {
      type = MethodKind.NATIVE;
    } else {
      type = MethodKind.CONCRETE;
    }
    
    this.method = method;
    this.name = domain.methodMetadata.getName(method);
    this.access = domain.methodMetadata.getAccess(method);
    this._final = domain.methodMetadata.isFinal(method);
    this._static = domain.methodMetadata.isStatic(method);
    this.type = type;
    this.owner = owner;
    this.signature = null;
    this.typeParameters = null;
    this.parameterNames = null;
    this.annotatedDelegate = null;
    this.thrownTypes = null;
    
  }
  
  public M unwrap() {
    return method;
    
  }
  
  public TypeInfo getReturnType() {
    if (returnType == null) {
      T rt = domain.methodMetadata.getReturnType(method);
      returnType = domain.resolve(rt);
    }
    return returnType;
  }
  
  public List<String> getParameterNames() {
    if (parameterNames == null) {
      Iterable<String> names = domain.methodMetadata.getParameterNames(method);
      if (names != null) {
        List<String> parameterNames = new ArrayList<String>();
        for(String parameterName : names) {
          parameterNames.add(parameterName);
        }
        
        this.parameterNames = Collections.unmodifiableList(parameterNames);
      } else {
        this.parameterNames = NO_NAMES_AVAILABLE;
      }
    }
    return parameterNames == NO_NAMES_AVAILABLE ? null : parameterNames;
  }
  
  public List<TypeInfo> getParameterTypes() {
    if (parameterTypes == null) {
      List<TypeInfo> parameterTypes = new ArrayList<TypeInfo>();
      for (T parameterType : domain.methodMetadata.getParameterTypes(method)) {
        parameterTypes.add(domain.resolve(parameterType));
      }
      this.parameterTypes = Collections.unmodifiableList(parameterTypes);
    }
    return parameterTypes;
  }

  public AccessScope getAccess() {
    return access;
  }

  public boolean isAbstract() {
    return type == MethodKind.ABSTRACT;
  }

  public boolean isNative() {
    return type == MethodKind.NATIVE;
  }

  public boolean isConcrete() {
    return type == MethodKind.CONCRETE;
  }

  public boolean isStatic() {
    return _static;
  }

  public boolean isFinal() {
    return _final;
  }

  public MethodKind getType() {
    return type;
  }

  public String getName() {
    return name;
  }

  public ClassTypeInfo getOwner() {
    return owner;
  }

  public List<TypeVariableInfo> getTypeParameters() {
    if (typeParameters == null) {
      ArrayList<TypeVariableInfo> typeParameters = new ArrayList<TypeVariableInfo>();
      for (T tv : domain.methodMetadata.getTypeParameters(method)) {
        TypeVariableInfoImpl<T, M, A, P, F> typeParameter = (TypeVariableInfoImpl<T, M, A, P, F>)domain.getType(tv);
        typeParameters.add(typeParameter);
      }
      this.typeParameters = typeParameters;
    }
    return typeParameters;
  }

  public MethodSignature getSignature() {
    if (signature == null) {
      signature = new MethodSignature(name, getParameterTypes());
    }
    return signature;
  }

  public MethodSignature getSignature(ClassTypeInfo context) {
    List<TypeInfo> parameterTypes = getParameterTypes();
    List<TypeInfo> resolvedParameterTypes = new ArrayList<TypeInfo>(parameterTypes.size());
    for (TypeInfo parameterTI : parameterTypes) {
      TypeInfo resolvedParameterTI = context.resolve(parameterTI);
      resolvedParameterTypes.add(resolvedParameterTI);
    }
    return new MethodSignature(name, resolvedParameterTypes);
  }

  @Override
  public int hashCode() {
    return name.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj instanceof MethodInfo) {
      MethodInfo that = (MethodInfo)obj;
      String thatName = that.getName();
      MethodSignature signature = getSignature();
      MethodSignature thatSignature = that.getSignature();
      return owner.equals(getOwner()) && name.equals(thatName) && signature.equals(thatSignature);
    }
    return false;
  }

  public <AT> AT getDeclaredAnnotation(AnnotationType<AT, ?> annotationType) {
    if (annotatedDelegate == null) {
      annotatedDelegate = new AnnotatedDelegate<T, M, A, P, F, M>(domain, domain.methodAnnotationMetadata, method);
    }
    return annotatedDelegate.getDeclaredAnnotation(method, annotationType);
  }

  public List<ClassTypeInfo> getThrownTypes() {
    if (thrownTypes == null) {
      List<ClassTypeInfo> thrownTypes = Collections.emptyList();
      for (T thrownType : domain.methodMetadata.getThrownTypes(method)) {
        if (thrownTypes.isEmpty()) {
          thrownTypes = new ArrayList<ClassTypeInfo>();
        }
        ClassTypeInfo thrownCTI = (ClassTypeInfo)domain.resolve(thrownType);
        thrownTypes.add(thrownCTI);
      }
      this.thrownTypes = thrownTypes;
    }
    return thrownTypes;
  }

  @Override
  public String toString() {
    return "MethodInfo[name=" + name + ",owner=" + owner + "]";
  }
  

}
