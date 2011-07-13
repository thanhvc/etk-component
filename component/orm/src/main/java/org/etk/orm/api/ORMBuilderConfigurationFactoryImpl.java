package org.etk.orm.api;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.etk.orm.api.ORMBuilder.Configuration;
import org.etk.orm.api.format.DefaultObjectFormatter;


public class ORMBuilderConfigurationFactoryImpl extends ORMBuilder.Configuration.Factory {

  
  /**
   * Options configurable via system properties.
   */
  private final static Set<ORMBuilder.Option> SYSTEM_OPTIONS = Collections.unmodifiableSet(new HashSet<ORMBuilder.Option>(Arrays.asList(
    ORMBuilder.PROPERTY_CACHE_ENABLED,
    ORMBuilder.PROPERTY_READ_AHEAD_ENABLED,
    ORMBuilder.JCR_OPTIMIZE_ENABLED,
    ORMBuilder.JCR_OPTIMIZE_HAS_PROPERTY_ENABLED,
    ORMBuilder.JCR_OPTIMIZE_HAS_NODE_ENABLED,
    ORMBuilder.SESSION_LIFECYCLE_CLASSNAME
  )));

  /** . */
  private static final ORMBuilder.Configuration DEFAULT_CONFIG;

  static {

    //
    ORMBuilder.Configuration config = new ORMBuilder.Configuration();

    // Configure system properties options
    if (!Boolean.FALSE.equals(config.getOptionValue(ORMBuilder.USE_SYSTEM_PROPERTIES))) {
      for (ORMBuilder.Option<?> option : SYSTEM_OPTIONS) {
        String value = System.getProperty(option.getName());
        if (value != null) {
          _set(config, option, value, false);
        }
      }
    }

    config.setOptionValue(ORMBuilder.INSTRUMENTOR_CLASSNAME, "org.etk.orm.apt.InstrumentorImpl", false);
    config.setOptionValue(ORMBuilder.SESSION_LIFECYCLE_CLASSNAME, "org.etk.orm.api.ExoSessionLifeCycle", false);
    config.setOptionValue(ORMBuilder.OBJECT_FORMATTER_CLASSNAME, DefaultObjectFormatter.class.getName(), false);
    config.setOptionValue(ORMBuilder.PROPERTY_CACHE_ENABLED, false, false);
    config.setOptionValue(ORMBuilder.PROPERTY_READ_AHEAD_ENABLED, false, false);
    config.setOptionValue(ORMBuilder.JCR_OPTIMIZE_HAS_PROPERTY_ENABLED, false, false);
    config.setOptionValue(ORMBuilder.JCR_OPTIMIZE_HAS_NODE_ENABLED, false, false);
    config.setOptionValue(ORMBuilder.ROOT_NODE_PATH, "/", false);
    config.setOptionValue(ORMBuilder.CREATE_ROOT_NODE, false, false);
    config.setOptionValue(ORMBuilder.LAZY_CREATE_ROOT_NODE, false, false);

    //
    DEFAULT_CONFIG = config;
  }

  // Just for type safety
  private static <D> void _set(ORMBuilder.Configuration config, ORMBuilder.Option<D> option, String value, boolean overwrite) throws NullPointerException {
    config.setOptionValue(option, option.getType().parse(value), overwrite);
  }

  @Override
  public ORMBuilder.Configuration create() {
    return new ORMBuilder.Configuration(DEFAULT_CONFIG);
  }

}
