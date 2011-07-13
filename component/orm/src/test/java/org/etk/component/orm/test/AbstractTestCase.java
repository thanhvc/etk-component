package org.etk.component.orm.test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.jcr.Repository;

import junit.framework.AssertionFailedError;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestListener;
import junit.framework.TestResult;

import org.etk.orm.api.ORM;
import org.etk.orm.api.ORMBuilder;
import org.etk.orm.api.ORMSession;
import org.etk.orm.api.ORMSessionImpl;
import org.etk.orm.api.RepositoryBootstrap;
import org.etk.orm.api.annotations.MixinType;
import org.etk.orm.api.annotations.PrimaryType;

public abstract class AbstractTestCase extends TestCase {

  /** . */
  private Repository repo;
  /** . */
  public static final String ORM_TEST_MODE = "orm.test.mode";

  /** . */
  public static final String MODE_CGLIB = "cglib";

  /** . */
  public static final String MODE_APT = "apt";

  /** . */
  public static final String MODE_CACHE = "cache";

  /** . */
  public static final String MODE_HAS_NODE = "has_node";

  /** . */
  public static final String MODE_HAS_PROPERTY = "has_property";

  /** . */
  public static final String MODE_ALL = "all";

  /** . */
  private static final String APT_INSTRUMENTOR = "org.etk.orm.apt.InstrumentorImpl";

  /** . */
  //private static final String CGLIB_INSTRUMENTOR = CGLibInstrumentor.class.getName();

  /** . */
  private ORMBuilder builder;

  /** . */
  private ORM orm;

  /** . */
  private Config config;

  /** . */
  private String testName;

  /** . */
  private String rootNodePath;

  /** . */
  private final TestListener listener = new TestListener() {
    public void addError(Test test, Throwable throwable) {
    }
    public void addFailure(Test test, AssertionFailedError assertionFailedError) {
    }
    public void endTest(Test test) {
      testName = null;
    }
    public void startTest(Test test) {
      testName = ((AbstractTestCase)test).getName();
    }
  };

  public Config getConfig() {
    return config;
  }

  @Override
  protected void setUp() throws Exception {
    String p1 = getClass().getName().replace('.', '_');
    String p2 = config.propertyCacheEnabled ? "propertycached" : "propertynotcached";
    String p3 = config.optimizeHasNodeEnabled ? "hasnodeoptimized" : "hasnodenotoptimized";
    String p4 = config.optimizeHasPropertyEnabled ? "haspropertyoptimized" : "haspropertynotoptimized";
    String p5 = config.instrumentorClassName.lastIndexOf('.') == -1 ?
      config.instrumentorClassName :
      config.instrumentorClassName.substring(config.instrumentorClassName.lastIndexOf('.') + 1);
    String p6 = testName;

    //
    rootNodePath = "/" + p1 + "/" + p2 + "/" + p3 + "/" + p4  + "/" + p5 + "/" + p6;

    /*
    RepositoryBootstrap bootstrap = new RepositoryBootstrap();
    bootstrap.bootstrap();
    repo = bootstrap.getRepository();
    */
    
    //
    builder = ORMBuilder.create();

    //
    createDomain();

    //
    boolean pingRootNode = pingRootNode();

    //
    builder.setOptionValue(ORMBuilder.ROOT_NODE_PATH, rootNodePath);
    builder.setOptionValue(ORMBuilder.ROOT_NODE_TYPE, "nt:unstructured");
    builder.setOptionValue(ORMBuilder.PROPERTY_CACHE_ENABLED, config.propertyCacheEnabled);
    builder.setOptionValue(ORMBuilder.INSTRUMENTOR_CLASSNAME, config.instrumentorClassName);
    builder.setOptionValue(ORMBuilder.JCR_OPTIMIZE_HAS_PROPERTY_ENABLED, config.optimizeHasPropertyEnabled);
    builder.setOptionValue(ORMBuilder.JCR_OPTIMIZE_HAS_NODE_ENABLED, config.optimizeHasNodeEnabled);

    //
    if (pingRootNode) {
      builder.setOptionValue(ORMBuilder.CREATE_ROOT_NODE, true);
      builder.setOptionValue(ORMBuilder.LAZY_CREATE_ROOT_NODE, false);
    }

    //
    orm = builder.build();

    // Create virtual root node if required
    if (pingRootNode) {
      ORMSessionImpl sess = login();
      sess.getRoot();
      sess.save();
    }
  }

  @Override
  protected void tearDown() throws Exception {
    builder = null;
    orm = null;
  }

  @Override
  public final void run(TestResult result) {
    result.addListener(listener);

    //
    List<Config> configs = new LinkedList<Config>();

    //
    boolean aptEnabled = false;
   
    try {
      Thread.currentThread().getContextClassLoader().loadClass(APT_INSTRUMENTOR);
      aptEnabled = true;
    }
    catch (ClassNotFoundException ignore) {
    }

   
    //
    String testMode = System.getProperty(ORM_TEST_MODE);
    if (testMode == null) {
      testMode = MODE_ALL;
    }

    //
    if (MODE_ALL.equals(testMode)) {
      if (aptEnabled) {
        configs.add(new Config(APT_INSTRUMENTOR, false, false, false));
        configs.add(new Config(APT_INSTRUMENTOR, true, false, false));
        configs.add(new Config(APT_INSTRUMENTOR, false, true, false));
        configs.add(new Config(APT_INSTRUMENTOR, false, false, true));
      }
//      configs.add(new Config(CGLIB_INSTRUMENTOR, false, false, false));
//      configs.add(new Config(CGLIB_INSTRUMENTOR, true, false, false));
//      configs.add(new Config(CGLIB_INSTRUMENTOR, false, true, false));
//      configs.add(new Config(CGLIB_INSTRUMENTOR, false, false, true));
    } else if (MODE_APT.equals(testMode)) {
      configs.add(new Config(APT_INSTRUMENTOR, false, false, false));
    } /*else if (MODE_CGLIB.equals(testMode)) {
      configs.add(new Config(CGLIB_INSTRUMENTOR, false, false, false));
    } else if (MODE_CACHE.equals(testMode)) {
      configs.add(new Config(CGLIB_INSTRUMENTOR, true, false, false));
    } else if (MODE_HAS_NODE.equals(testMode)) {
      configs.add(new Config(CGLIB_INSTRUMENTOR, false, false, true));
    } else if (MODE_HAS_PROPERTY.equals(testMode)) {
      configs.add(new Config(CGLIB_INSTRUMENTOR, true, true, false));
    }*/

    //
    for (Config config : configs) {
      this.config = config;
      try {
        super.run(result);
      }
      finally {
        ArrayList<ORMSessionImpl> copy = new ArrayList<ORMSessionImpl>(sessions);
        sessions.clear();
        for (ORMSession session : copy) {
          if (!session.isClosed()) {
            session.close();
          }
        }
      }
    }

    //
    result.removeListener(listener);
  }

  /** The session opened during the test. */
  private List<ORMSessionImpl> sessions = new ArrayList<ORMSessionImpl>();

  public final ORMSessionImpl login() {
    ORMSessionImpl session = (ORMSessionImpl)orm.openSession();
    sessions.add(session);
    return session;
  }

  protected final <D> void setOptionValue(ORMBuilder.Option<D> option, D value) throws NullPointerException {
    builder.setOptionValue(option, value);
  }

  protected final ORMBuilder getBuilder() {
    return builder;
  }

  protected final String getRootNodePath() {
    return rootNodePath;
  }

  protected final void addClass(Class<?> clazz) {
    builder.add(clazz);
  }

  protected final String getNodeTypeName(Class<?> clazz) {
    PrimaryType primaryType = clazz.getAnnotation(PrimaryType.class);
    if (primaryType != null) {
      return primaryType.name();
    } else {
      MixinType mixinType = clazz.getAnnotation(MixinType.class);
      if (mixinType != null) {
        return mixinType.name();
      }
    }
    return null;
  }

  /**
   * Returns true if the root node should be created during the test bootstrap. This method can be overriden
   * by unit test to change the behavior.
   *
   * @return the root node ping
   */
  protected boolean pingRootNode() {
    return true;
  }

  protected abstract void createDomain();

  public static class Config {

    /** . */
    private final String instrumentorClassName;

    /** . */
    private final boolean propertyCacheEnabled;

    /** . */
    private final boolean optimizeHasPropertyEnabled;

    /** . */
    private final boolean optimizeHasNodeEnabled;

    public Config(
      String instrumentorClassName,
      boolean propertyCacheEnabled,
      boolean optimizeHasPropertyEnabled,
      boolean optimizeHasNodeEnabled) {
      this.instrumentorClassName = instrumentorClassName;
      this.propertyCacheEnabled = propertyCacheEnabled;
      this.optimizeHasNodeEnabled = optimizeHasNodeEnabled;
      this.optimizeHasPropertyEnabled = optimizeHasPropertyEnabled;
      
    }

    public String getInstrumentorClassName() {
      return instrumentorClassName;
    }

    public boolean isPropertyCacheEnabled() {
      return propertyCacheEnabled;
    }

    public boolean isStateCacheDisabled() {
      return !propertyCacheEnabled;
    }

    @Override
    public String toString() {
      return "Config[instrumentorClassName=" + instrumentorClassName + ",stateCacheEnaled=" + propertyCacheEnabled + "" +
        ",optimizeHasNodeEnabled=" + optimizeHasNodeEnabled + ",optimizeHasPropertyEnabled=" + optimizeHasPropertyEnabled + "]";
    }
  }
  
}
