package org.etk.kernel.test.spi;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.Set;

import org.etk.kernel.container.ApplicationContainer;
import org.etk.kernel.container.component.RequestLifeCycle;
import org.etk.kernel.test.annotations.ConfigurationUnit;
import org.etk.kernel.test.annotations.ConfiguredBy;
import org.etk.kernel.test.annotations.ContainerScope;

public class AbstractContainerTest extends AbstractBasicTest {
  /** The system property for etk tmp dir. */
  private static final String TMP_DIR = "etk.test.tmp.dir";

  /** . */
  private ApplicationContainer container;

  /** . */
  private ClassLoader realClassLoader;

  protected AbstractContainerTest() {
    super();
  }

  protected AbstractContainerTest(String name) {
    super(name);
  }

  public ApplicationContainer getContainer() {
    return container;
  }

  protected void begin() {
    RequestLifeCycle.begin(container);
  }

  protected void end() {
    RequestLifeCycle.end();
  }

  @Override
  protected void beforeRunBare() throws Exception {
    realClassLoader = Thread.currentThread().getContextClassLoader();

    //
    Set<String> rootConfigPaths = new HashSet<String>();
    rootConfigPaths.add("conf/root-configuration.xml");

    //
    Set<String> applicationConfigPaths = new HashSet<String>();
    applicationConfigPaths.add("conf/application/application-configuration.xml");

    //
    EnumMap<ContainerScope, Set<String>> configs = new EnumMap<ContainerScope, Set<String>>(ContainerScope.class);
    configs.put(ContainerScope.ROOT, rootConfigPaths);
    configs.put(ContainerScope.APPLICATION, applicationConfigPaths);

    //gets the annotations for testing
    ConfiguredBy cfBy = getClass().getAnnotation(ConfiguredBy.class);
    if (cfBy != null) {
      for (ConfigurationUnit src : cfBy.value()) {
        configs.get(src.scope()).add(src.path());
      }
    }
    /*
    // Take care of creating tmp directory for unit test
    if (System.getProperty(TMP_DIR) == null) {
      // Get base dir set by maven or die
      File targetDir = new File(new File(System.getProperty("basedir")), "target");
      if (!targetDir.exists()) {
        throw new AssertionFailedError("Target dir for unit test does not exist");
      }
      if (!targetDir.isDirectory()) {
        throw new AssertionFailedError("Target dir is not a directory");
      }
      if (!targetDir.canWrite()) {
        throw new AssertionFailedError("Target dir is not writable");
      }

      //
      Set<String> fileNames = new HashSet<String>();
      for (File child : targetDir.listFiles(new FilenameFilter() {
        public boolean accept(File dir, String name) {
          return name.startsWith("gateintest-");
        }
      })) {
        fileNames.add(child.getName());
      }

      //
      String fileName;
      int count = 0;
      while (true) {
        fileName = "gateintest-" + count;
        if (!fileNames.contains(fileName)) {
          break;
        }
        count++;
      }

      //
      File tmp = new File(targetDir, fileName);
      if (!tmp.mkdirs()) {
        throw new AssertionFailedError("Could not create directory " + tmp.getCanonicalPath());
      }

      //
      System.setProperty(TMP_DIR, tmp.getCanonicalPath());
    }*/
    //
    ClassLoader testClassLoader = new BaseTestClassLoader(realClassLoader,
                                                          rootConfigPaths,
                                                          applicationConfigPaths);
    Thread.currentThread().setContextClassLoader(testClassLoader);

    // Boot the container
    container = ApplicationContainer.getInstance();

  }

  @Override
  protected void afterRunBare() {
    container = null;

    Thread.currentThread().setContextClassLoader(realClassLoader);
  }
}

