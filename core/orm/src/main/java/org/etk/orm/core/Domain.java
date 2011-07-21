package org.etk.orm.core;

import java.lang.reflect.UndeclaredThrowableException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

import org.etk.common.collection.Collections;
import org.etk.orm.api.BuilderException;
import org.etk.orm.api.format.ObjectFormatter;
import org.etk.orm.plugins.bean.mapping.BeanMapping;
import org.etk.orm.plugins.common.jcr.Path;
import org.etk.orm.plugins.common.jcr.PathException;
import org.etk.orm.plugins.instrument.Instrumentor;
import org.etk.orm.plugins.instrument.MethodHandler;
import org.etk.orm.plugins.instrument.ProxyType;
import org.etk.orm.plugins.jcr.type.TypeManager;
import org.etk.orm.plugins.mapper.ObjectMapper;
import org.etk.orm.plugins.query.QueryManager;


public class Domain {

  /** . */
  private static final ProxyType<?> NULL_PROXY_TYPE = new ProxyType<Object>() {
    public Object createProxy(MethodHandler handler) {
      throw new UnsupportedOperationException("Cannot create proxy for " + handler);
    }

    public Class<?> getType() {
      throw new UnsupportedOperationException("Cannot get proxy type for NULL_PROXY_TYPE");
    }
  };

  /** . */
  private static final Instrumentor NULL_INSTRUMENTOR = new Instrumentor() {

    // This is OK as the class is *stateless*
    @SuppressWarnings("unchecked")
    public <O> ProxyType<O> getProxyClass(Class<O> clazz) {
      return (ProxyType<O>) NULL_PROXY_TYPE;
    }

    public MethodHandler getInvoker(Object proxy) {
      throw new UnsupportedOperationException();
    }
  };

  /** . */
  public static int LAZY_CREATE_MODE = 0;

  /** . */
  public static int CREATE_MODE = 1;

  /** . */
  public static int NO_CREATE_MODE = 2;

  /** . */
  private static final Set<Integer> CREATE_MODES = Collections.set(LAZY_CREATE_MODE, CREATE_MODE, NO_CREATE_MODE);

  /** . */
  private final Map<String, ObjectMapper> typeMapperByNodeType;

  /** . */
  private final Map<Class<?>, ObjectMapper> typeMapperByClass;

  /** The default instrumentor */
  private final Instrumentor  defaultInstrumentor;

  /** . */
  private final Map<Class<?>, Instrumentor> proxyTypeToInstrumentor;

  /** . */
  private final Map<Class<?>, Instrumentor> ormTypeToInstrumentor;

  /** . */
  final ObjectFormatter objectFormatter;

  /** . */
  final boolean propertyCacheEnabled;

  /** . */
  final boolean propertyReadAheadEnabled;

  /** . */
  final boolean hasPropertyOptimized;
  /** . */

  final boolean hasNodeOptimized;

  /** . */
  final String rootNodePath;

  /** . */
  final List<String> rootNodePathSegments;

  /** . */
  final String rootNodeType;

  /** . */
  final int rootCreateMode;

  /** . */
  final TypeManager nodeInfoManager;

  /** . */
  final QueryManager queryManager;

  public Domain(Collection<ObjectMapper<?>> mappers,
                Instrumentor defaultInstrumentor,
                ObjectFormatter objectFormatter,
                boolean propertyCacheEnabled,
                boolean propertyReadAheadEnabled,
                boolean hasPropertyOptimized,
                boolean hasNodeOptimized,
                String rootNodePath,
                int rootCreateMode,
                String rootNodeType) {

    //
    if (!CREATE_MODES.contains(rootCreateMode)) {
      throw new IllegalArgumentException("Invalid create mode " + rootCreateMode);
    }
    
    //
    Map<Class<?>, Instrumentor> proxyTypeToInstrumentor = new HashMap<Class<?>, Instrumentor>();
    Map<Class<?>, Instrumentor> ormTypeToInstrumentor = new HashMap<Class<?>, Instrumentor>();
    
    for (ObjectMapper<?> mapper : mappers) {
      BeanMapping beanMapping = mapper.getMapping();
      Class<?> clazz = (Class<?>) beanMapping.getBean().getClassType().unwrap();
 
      if (Object.class.equals(clazz)) {
        proxyTypeToInstrumentor.put(clazz, defaultInstrumentor);
        ormTypeToInstrumentor.put(clazz, defaultInstrumentor);
      } else {
        //wrapper the clazz in the ProxyTypeImpl which supports 
        //to get the MethodHandler to invoke the method in clazz.
        proxyTypeToInstrumentor.put(defaultInstrumentor.getProxyClass(clazz).getType(), defaultInstrumentor);
        ormTypeToInstrumentor.put(clazz, defaultInstrumentor);
      }
    }

    //
    Map<String, ObjectMapper> typeMapperByNodeType = new HashMap<String, ObjectMapper>();
    Map<Class<?>, ObjectMapper> typeMapperByClass = new HashMap<Class<?>, ObjectMapper>();
    
    for (ObjectMapper typeMapper : mappers) {
      if (typeMapperByNodeType.containsKey(typeMapper.getNodeTypeName())) {
        throw new IllegalStateException("Duplicate node type name " + typeMapper);
      }
      typeMapperByNodeType.put(typeMapper.getNodeTypeName(), typeMapper);
      typeMapperByClass.put(typeMapper.getObjectClass(), typeMapper);
    }

    //
    final List<String> rootNodePathSegments;
    try {
      rootNodePathSegments = Path.splitAbsolutePath(Path.normalizeAbsolutePath(rootNodePath));
    } catch (PathException e) {
      throw new BuilderException("Root node path must be valid");
    }

    //
    this.typeMapperByClass = typeMapperByClass;
    this.typeMapperByNodeType = typeMapperByNodeType;
    this.defaultInstrumentor = defaultInstrumentor;
    this.objectFormatter = objectFormatter;
    this.propertyCacheEnabled = propertyCacheEnabled;
    this.propertyReadAheadEnabled = propertyReadAheadEnabled;
    this.hasPropertyOptimized = hasPropertyOptimized;
    this.hasNodeOptimized = hasNodeOptimized;
    this.rootNodePath = rootNodePath;
    this.rootNodePathSegments = rootNodePathSegments;
    this.nodeInfoManager = new TypeManager();
    this.queryManager = new QueryManager(rootNodePath);
    this.rootCreateMode = rootCreateMode;
    this.rootNodeType = rootNodeType;
    this.proxyTypeToInstrumentor = proxyTypeToInstrumentor;
    this.ormTypeToInstrumentor = ormTypeToInstrumentor;
  }

  public boolean isHasPropertyOptimized() {
    return hasPropertyOptimized;
  }

  public boolean isHasNodeOptimized() {
    return hasNodeOptimized;
  }

  public MethodHandler getHandler(Object o) {
    Instrumentor instrumentor = proxyTypeToInstrumentor.get(o.getClass());
    return instrumentor != null ? instrumentor.getInvoker(o) : null;
  }

  public <O> ProxyType<O> getProxyType(Class<O> type) {
    Instrumentor instrumentor = ormTypeToInstrumentor.get(type);
    return instrumentor.getProxyClass(type);
  }

  public ObjectMapper getTypeMapper(String nodeTypeName) {
    return typeMapperByNodeType.get(nodeTypeName);
  }

  public ObjectMapper getTypeMapper(Class<?> clazz) {
    return typeMapperByClass.get(clazz);
  }

  public QueryManager getQueryManager() {
    return queryManager;
  }

  /**
   * 
   * 
   * @param ownerNode
   * @param internal
   * @param nameKind
   * @return
   * @throws NullPointerException
   * @throws UndeclaredThrowableException
   * @throws IllegalStateException
   * @throws RepositoryException
   */
  String decodeName(Node ownerNode, String internal, NameKind nameKind) throws NullPointerException,
                                                                       UndeclaredThrowableException,
                                                                       IllegalStateException,
                                                                       RepositoryException {
    if (ownerNode == null) {
      throw new NullPointerException();
    }
    String nodeTypeName = ownerNode.getPrimaryNodeType().getName();
    ObjectMapper ownerMapper = getTypeMapper(nodeTypeName);
    ObjectFormatter formatter = null;
    if (ownerMapper != null) {
      formatter = ownerMapper.getFormatter();
    }
    return decodeName(formatter, internal, nameKind);
  }

  /**
   * 
   * 
   * @param ownerCtx
   * @param internal
   * @param nameKind
   * @return
   * @throws NullPointerException
   * @throws UndeclaredThrowableException
   * @throws IllegalStateException
   * @throws RepositoryException
   */
  String decodeName(ObjectContext ownerCtx, String internal, NameKind nameKind) throws NullPointerException,
                                                                               UndeclaredThrowableException,
                                                                               IllegalStateException,
                                                                               RepositoryException {
    if (ownerCtx == null) {
      throw new NullPointerException();
    }
    return decodeName(ownerCtx.getMapper().getFormatter(), internal, nameKind);
  }

  private String decodeName(ObjectFormatter formatter, String internal, NameKind nameKind) throws UndeclaredThrowableException,
                                                                                          IllegalStateException,
                                                                                          RepositoryException {
    if (nameKind == NameKind.PROPERTY) {
      return internal;
    }
    if (formatter == null) {
      formatter = objectFormatter;
    }

    //
    String external;
    try {
      if (nameKind == NameKind.OBJECT) {
        external = formatter.decodeNodeName(null, internal);
      } else {
        // external = formatter.decodePropertyName(null, internal);
        throw new UnsupportedOperationException();
      }
    } catch (Exception e) {
      if (e instanceof IllegalStateException) {
        throw (IllegalStateException) e;
      }
      throw new UndeclaredThrowableException(e);
    }
    if (external == null) {
      if (nameKind == NameKind.OBJECT) {
        throw new IllegalStateException();
      }
    }
    return external;
  }

  /**
   * Encodes the name for the specified context.
   *
   * @param ownerNode the node
   * @param externalName the external name
   * @param nameKind the name kind
   * @return the encoded name
   * @throws NullPointerException if the owner context argument is null
   * @throws UndeclaredThrowableException when the formatter throws an exception
   * @throws RepositoryException any repository exception
   */
  String encodeName(Node ownerNode, String externalName, NameKind nameKind) throws NullPointerException,
                                                                           UndeclaredThrowableException,
                                                                           RepositoryException {
    if (ownerNode == null) {
      throw new NullPointerException();
    }
    String nodeTypeName = ownerNode.getPrimaryNodeType().getName();
    ObjectMapper ownerMapper = getTypeMapper(nodeTypeName);
    ObjectFormatter formatter = null;
    if (ownerMapper != null) {
      formatter = ownerMapper.getFormatter();
    }
    return encodeName(formatter, externalName, nameKind);
  }

  /**
   * Encodes the name for the specified context.
   *
   * @param ownerCtx the context
   * @param externalName the external name
   * @param nameKind the name kind
   * @return the encoded name
   * @throws NullPointerException if any argument is null
   * @throws UndeclaredThrowableException when the formatter throws an exception
   * @throws RepositoryException any repository exception
   */
  String encodeName(ObjectContext ownerCtx, String externalName, NameKind nameKind) throws NullPointerException,
                                                                                   UndeclaredThrowableException,
                                                                                   RepositoryException {
    if (ownerCtx == null) {
      throw new NullPointerException("No null owner node accepted");
    }
    return encodeName(ownerCtx.getMapper().getFormatter(), externalName, nameKind);
  }

  /**
   * 
   * @param formatter
   * @param externalName
   * @param nameKind
   * @return
   * @throws UndeclaredThrowableException
   * @throws NullPointerException
   * @throws RepositoryException
   */
  private String encodeName(ObjectFormatter formatter, String externalName, NameKind nameKind) throws UndeclaredThrowableException,
                                                                                              NullPointerException,
                                                                                              RepositoryException {
    if (externalName == null) {
      throw new NullPointerException("No null name accepted");
    }
    if (nameKind == null) {
      throw new NullPointerException("No null name kind accepted");
    }
    if (nameKind == NameKind.PROPERTY) {
      return externalName;
    }
    if (formatter == null) {
      formatter = objectFormatter;
    }

    //
    String internal;
    try {
      if (nameKind == NameKind.OBJECT) {
        internal = formatter.encodeNodeName(null, externalName);
      } else {
        // internal = formatter.encodePropertyName(null, external);
        throw new UnsupportedOperationException();
      }
    } catch (Exception e) {
      if (e instanceof NullPointerException) {
        throw (NullPointerException) e;
      }
      if (e instanceof IllegalArgumentException) {
        throw (IllegalArgumentException) e;
      }
      throw new UndeclaredThrowableException(e);
    }
    if (internal == null) {
      throw new IllegalArgumentException("Name " + externalName + " was converted to null");
    }
    Path.validateLocalName(internal);
    return internal;
  }
}