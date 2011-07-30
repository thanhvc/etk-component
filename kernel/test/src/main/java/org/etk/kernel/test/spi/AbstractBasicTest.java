package org.etk.kernel.test.spi;

import java.util.Map;

import junit.framework.TestCase;

import org.etk.common.logging.Logger;


public class AbstractBasicTest extends TestCase {
  /** . */
  protected final Logger log = Logger.getLogger(getClass());

  protected AbstractBasicTest() {
  }

  protected AbstractBasicTest(String name) {
    super(name);
  }

  protected void beforeRunBare() throws Exception {
    //
  }

  /**
   * After the run base, it should not throw anything as it is executed in a
   * finally clause.
   */
  protected void afterRunBare() {
    //
  }

  @Override
  public final void runBare() throws Throwable {
    // Patch a bug with maven that does not pass properly the system property
    // with an empty value
    if ("org.hsqldb.jdbcDriver".equals(System.getProperty("etk.test.datasource.driver"))) {
      System.setProperty("etk.test.datasource.password", "");
    }

    //
    log.info("Running unit test:" + getName());
    for (Map.Entry<?, ?> entry : System.getProperties().entrySet()) {
      if (entry.getKey() instanceof String) {
        String key = (String) entry.getKey();
        log.debug(key + "=" + entry.getValue());
      }
    }

    //
    beforeRunBare();

    //
    try {
      super.runBare();
      log.info("Unit test " + getName() + " completed");
    } catch (Throwable throwable) {
      log.error("Unit test " + getName() + " did not complete", throwable);

      //
      throw throwable;
    } finally {
      afterRunBare();
    }
  }
}
