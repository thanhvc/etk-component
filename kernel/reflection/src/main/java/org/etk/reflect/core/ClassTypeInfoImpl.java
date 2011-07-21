package org.etk.reflect.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.etk.reflect.api.ClassTypeInfo;
import org.etk.reflect.api.FieldInfo;
import org.etk.reflect.api.MethodInfo;
import org.etk.reflect.api.MethodSignature;
import org.etk.reflect.api.ParameterizedTypeInfo;
import org.etk.reflect.api.TypeInfo;
import org.etk.reflect.api.TypeVariableInfo;
import org.etk.reflect.api.definition.ClassKind;


class ClassTypeInfoImpl<T, M, A, P, F> extends AbstractTypeInfo<T, M, A, P, F> implements ClassTypeInfo {

  private final T classType;
  
  private final String className;
  
  private List<TypeInfo> interfaces;
  
  private TypeInfo superType;
  
  private boolean superClassResolved;
  
  private List<TypeVariableInfo> typeParameters;
  /**
   * 
   */
  private List<MethodInfo> methods;
  /**
   * 
   */
  private LinkedHashMap<String, FieldInfo> fields;
  /**
   * 
   */
  private final String simpleName;
  
  /**
   * 
   */
  private final String packageName;
  /**
   * 
   */
  private final ClassKind kind;
  /**
   * 
   */
  private ClassTypeInfo enclosing;
  
  /**
   * 
   */
  private boolean enclosingResolved;
  
  
  private AnnotatedDelegate<T,M,A,P,F,T> annotatedDelegate;
  
  public ClassTypeInfoImpl(TypeResolverImpl<T, M, A, P, F> domain, T classType) {
    super(domain);
    String className = domain.typeMetadata.getClassName(classType);
    String simpleName;
    String packageName;
    int index = className.lastIndexOf('.');
    if (index == -1) {
      simpleName = className;
      packageName ="";
    } else {
      simpleName = className.substring(index + 1);
      packageName = className.substring(0, index);
    }
    //Getting the ClassKind via the domain.typeModel.getClassKind();
    ClassKind kind = domain.typeMetadata.getClassKind(classType);
    this.className = className;
    this.classType = classType;
    this.interfaces = null;
    this.superType = null;
    this.superClassResolved = false;
    this.simpleName = simpleName;
    this.packageName = packageName;
    this.kind = kind;
    this.annotatedDelegate = null;
    this.methods = null;
    this.fields = null;
    this.enclosing = null;
    this.enclosingResolved = false;
    
  }
  
  public String getName() {
    return className;
  }
  
  public boolean isReified() {
    return true;
  }
  
  public String getPackageName() {
    return packageName;
  }

  public ClassTypeInfo getEnclosing() {
    if (!enclosingResolved) {
      T enclosingType = domain.typeMetadata.getEnclosing(classType);
      if (enclosingType != null) {
        enclosing = (ClassTypeInfo)domain.resolve(enclosingType);
      }
      enclosingResolved = true;
    }
    return enclosing;
  }

  public String getSimpleName() {
    return simpleName;
  }

  public ClassKind getKind() {
    return kind;
  }
  
  public List<TypeVariableInfo> getTypeParameters() {
    if (typeParameters == null) {
      ArrayList<TypeVariableInfo> typeParameters = new ArrayList<TypeVariableInfo>();
      for(T tv : domain.typeMetadata.getTypeParameters(classType)) {
        TypeVariableInfoImpl<T, M, A, P, F> typeParameter = (TypeVariableInfoImpl<T, M, A, P, F>) domain.getType(tv);
        typeParameters.add(typeParameter);
      }
      
      this.typeParameters = typeParameters;
    }
    return typeParameters;
  }
  

  public Iterable<TypeInfo> getInterfaces() {
    if (this.interfaces == null) {
      ArrayList<TypeInfo> interfaces = new ArrayList<TypeInfo>();
      for (T interfaceType : domain.typeMetadata.getInterfaces(classType)) {
        TypeInfo itf = domain.resolve(interfaceType);
        interfaces.add(itf);
      }
      this.interfaces = interfaces;
    }
    
    return this.interfaces;
  }
  
  public TypeInfo getSuperType() {
    if (!superClassResolved) {
      T superClassType = domain.typeMetadata.getSuperClass(classType);
      if (superClassType != null) {
        superType = domain.resolve(superClassType);
      }
      superClassResolved = true;
    }
    return superType;
  }
  
  public TypeInfo resolve(TypeInfo type) {
    return Utils.resolve(this, type);
  }
  

  public List<MethodInfo> getDeclaredMethods() {
    if (methods == null) {
      List<MethodInfo> methods = new ArrayList<MethodInfo>();
      
      for (M method : domain.methodMetadata.getDeclaredMethods(classType)) {
        MethodInfo mi = new MethodInfoImpl<T, M, A, P, F>(this, domain, method);
        methods.add(mi);
      }
      
      this.methods = methods;
    }
    
    return this.methods;
  }
  

  public MethodInfo getDeclaredMethod(MethodSignature methodSignature) {
    for(MethodInfo methodInfo : getDeclaredMethods()) {
      if (methodInfo.getSignature().equals(methodSignature)) {
        return methodInfo;
      }
    }
    
    return null;
  }
  
  private Map<String, FieldInfo> getDeclaredFieldMap() {
    if (fields == null) {
      LinkedHashMap<String, FieldInfo> fields = new LinkedHashMap<String, FieldInfo>();
      for(F field : domain.fieldMetadata.getDeclaredFields(classType)) {
        FieldInfo fi = new FieldInfoImpl<T, M, A, P, F>(this, domain, field);
        fields.put(fi.getName(), fi);
      }
      
      this.fields = fields;
    }
    
    return this.fields;
  }
  
  public Collection<FieldInfo> getDeclaredFields() {
    return getDeclaredFieldMap().values();
  }
  
  public FieldInfo getDeclaredField(String fieldName) {
    return getDeclaredFieldMap().get(fieldName);
  }
  
  public ClassTypeInfo getSuperClass() {
    TypeInfo superType = getSuperType();
    
    if (superType == null) {
      return null;
    } else if (superType instanceof ClassTypeInfo) {
      return (ClassTypeInfo) superType;
    } else if (superType instanceof ParameterizedTypeInfo) {
      TypeInfo rawType = ((ParameterizedTypeInfo)superType).getRawType();
      if (rawType instanceof ClassTypeInfo) {
        return (ClassTypeInfo) rawType;
      } else {
        throw new AssertionError("Cannot cast raw type" + rawType + " to class type.");
      }
    } else {
      throw new AssertionError("Cannot cast type " + superType + " to class type.");
    }
    
  }
  
  private boolean isAssignableFrom(TypeInfo that) {
    if (that instanceof ClassTypeInfo) {
      return isAssignableFrom((ClassTypeInfo)that);
    } else if (that instanceof ParameterizedTypeInfo) {
      return isAssignableFrom(((ParameterizedTypeInfo)that).getRawType());
    } else if (that instanceof TypeVariableInfo) {
      return isAssignableFrom(((TypeVariableInfo)that).getBounds().get(0));
    } else {
      return false;
    }
  }
  
  public boolean isAssignableFrom(ClassTypeInfo that) {
    if (className.equals("java.lang.Object")) {
      return true;
    }
    if (className.equals(that.getName())) {
      return true;
    }
    TypeInfo superType = that.getSuperType();
    if (superType != null && isAssignableFrom(superType)) {
      return true;
    }
    for (TypeInfo itf : that.getInterfaces()) {
      if (isAssignableFrom(itf)) {
        return true;
      }
    }
    return false;
  }

  public T unwrap() {
    return classType;
  }
  
  public <AT> AT getDeclaredAnnotation(AnnotationType<AT, ?> annotationType) {
    if (annotatedDelegate == null) {
      annotatedDelegate = new AnnotatedDelegate<T, M, A, P, F, T>(domain, domain.typeAnnotationMetadata, classType);
    }
    
    return annotatedDelegate.getDeclaredAnnotation(classType, annotationType);
  }
  
  public int hashCode() {
    return className.hashCode();
  }
  
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    
    if (obj instanceof ClassTypeInfo) {
      ClassTypeInfo that = (ClassTypeInfo) obj;
      String thatName = that.getName();
      return className.equals(thatName);
    }
    
    return false;
      
  }
  
  public String toString() {
    return "ClassTypeInfo[className=" + className + "]";
  }
}
