package org.etk.orm.api;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.etk.orm.api.format.ObjectFormatter;
import org.etk.orm.core.Domain;
import org.etk.orm.plugins.bean.mapping.BeanMapping;
import org.etk.orm.plugins.bean.mapping.BeanMappingBuilder;
import org.etk.orm.plugins.bean.type.SimpleTypeResolver;
import org.etk.orm.plugins.common.ObjectInstantiator;
import org.etk.orm.plugins.common.jcr.Path;
import org.etk.orm.plugins.common.jcr.PathException;
import org.etk.orm.plugins.instrument.Instrumentor;
import org.etk.orm.plugins.jcr.SessionLifeCycle;
import org.etk.orm.plugins.mapper.MapperBuilder;
import org.etk.orm.plugins.mapper.ObjectMapper;
import org.etk.reflect.api.ClassTypeInfo;
import org.etk.reflect.api.TypeResolver;
import org.etk.reflect.core.TypeResolverImpl;
import org.etk.reflect.jlr.metadata.JLReflectionMetadata;


public class ORMBuilderImpl extends ORMBuilder {

  public ORMBuilderImpl() {
    
  }
  
  private <T> T create(Option.Instance<String> optionInstance, Class<T> expectedClass) {
    String s = optionInstance.getValue();
    return ObjectInstantiator.newInstance(s, expectedClass);
  }
  
  /** The mappers. */
  private Collection<ObjectMapper<?>> mappers;
  
  @Override
  protected void init(Set<Class<?>> classes) throws BuilderException {
    SimpleTypeResolver propertyTypeResolver = new SimpleTypeResolver();
    TypeResolver<Type> typeResolver = TypeResolverImpl.create(JLReflectionMetadata.newInstance());
    
    //Build mappings
    Set<ClassTypeInfo> classTypes = new HashSet<ClassTypeInfo>();
    
    //converts the classes to the set of ClassTypeInfo via the TypeResolver.
    for (Class clazz : classes) {
      ClassTypeInfo typeInfo = (ClassTypeInfo) typeResolver.resolve(clazz);
      classTypes.add(typeInfo);
      
    }
    
    Map<ClassTypeInfo, BeanMapping> beanMappings = new BeanMappingBuilder().build(classTypes);
    
    Collection<BeanMapping> mappings = beanMappings.values();

    // Build mappers
    MapperBuilder builder = new MapperBuilder(propertyTypeResolver);
    Collection<ObjectMapper<?>> mappers = builder.build(mappings);

    //
    this.mappers = mappers;
    
  }

  @Override
  protected ORM boot(Configuration options) throws BuilderException {
    //
    Boolean optimizeJCREnabled = options.getOptionValue(JCR_OPTIMIZE_ENABLED);

    //
    final boolean hasPropertyOptimized;
    if (optimizeJCREnabled != null) {
      hasPropertyOptimized = optimizeJCREnabled;
    } else {
      hasPropertyOptimized = options.getOptionValue(JCR_OPTIMIZE_HAS_PROPERTY_ENABLED);
    }

    //
    final boolean hasNodeOptimized;
    if (optimizeJCREnabled != null) {
      hasNodeOptimized = optimizeJCREnabled;
    } else {
      hasNodeOptimized = options.getOptionValue(JCR_OPTIMIZE_HAS_NODE_ENABLED);
    }

    //
    String rootNodePath;
    try {
      rootNodePath = Path.normalizeAbsolutePath(options.getOptionValue(ROOT_NODE_PATH));
    }
    catch (PathException e) {
      throw new BuilderException("Root node path must be valid");
    }

    //
    int rootCreateMode;
    if (options.getOptionValue(CREATE_ROOT_NODE)) {
      boolean lazyCreateMode = options.getOptionValue(LAZY_CREATE_ROOT_NODE);
      if (lazyCreateMode) {
        rootCreateMode = Domain.LAZY_CREATE_MODE;
      } else {
        rootCreateMode = Domain.CREATE_MODE;
      }
    } else {
      rootCreateMode = Domain.NO_CREATE_MODE;
    }

    //
    String rootNodeType = options.getOptionValue(ROOT_NODE_TYPE);

    //
    boolean propertyCacheEnabled = options.getOptionValue(PROPERTY_CACHE_ENABLED);
    boolean propertyReadAheadEnabled = options.getOptionValue(PROPERTY_READ_AHEAD_ENABLED);

    //
    Instrumentor instrumentor = create(options.getOptionInstance(INSTRUMENTOR_CLASSNAME), Instrumentor.class);
    ObjectFormatter objectFormatter = create(options.getOptionInstance(OBJECT_FORMATTER_CLASSNAME), ObjectFormatter.class);
    SessionLifeCycle sessionLifeCycle = create(options.getOptionInstance(SESSION_LIFECYCLE_CLASSNAME), SessionLifeCycle.class);

    // Build domain
    Domain domain = new Domain(
      mappers,
      instrumentor,
      objectFormatter,
      propertyCacheEnabled,
      propertyReadAheadEnabled,
      hasPropertyOptimized,
      hasNodeOptimized,
      rootNodePath,
      rootCreateMode,
      rootNodeType);

    //
    return new ORMImpl(domain, sessionLifeCycle);
  }

}
