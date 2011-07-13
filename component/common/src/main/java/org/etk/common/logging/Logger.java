package org.etk.common.logging;

import java.util.concurrent.ConcurrentHashMap;
import org.etk.common.collection.Collections;

/**
 * This class will be wrapper the SLF4J
 * @author thanh_vucong
 *
 */
public class Logger {

  private static final ConcurrentHashMap<String, Logger> loggers = new ConcurrentHashMap<String, Logger>();
 
  /**
   * Constructor to initialize the <code>Logger</code>
   * 
   * @param name The name of the class which needs to create the Logger.
   * 
   * @return
   * @throws NullPointerException
   */
  public static Logger getLogger(Class<?> name) throws NullPointerException {
    if (name == null) {
      throw new NullPointerException();
    }
    
    return getLogger(name.getName());
  }
  /**
   * MethodFactory to make the logger, first to check in the cache,
   *  and then to create if it is null.
   *  
   * @param name Class name.
   * @return
   * @throws NullPointerException
   */
  public static Logger getLogger(String name) throws NullPointerException {
    if (name == null) {
      throw new NullPointerException();
    }
    
    Logger logger = loggers.get(name);
    if (logger == null) {
      logger = Collections.putIfAbsent(loggers, name, new Logger(name));
      
    }
    return logger;
  }
  
  private final org.slf4j.Logger logger;
  
  public Logger(String name) {
    this.logger = org.slf4j.LoggerFactory.getLogger(name);
  }
  
  public boolean isTraceEnabled() {
    return logger.isTraceEnabled();
  }
  public void trace(String msg) {
    logger.trace(msg);
  }
  
  public void trace(String msg, Object o) {
    logger.trace(msg, o);
  }
  
  public void trace(String s, Object... objects) {
    logger.trace(s, objects);
  }

  public void trace(String s, Throwable throwable) {
    logger.trace(s, throwable);
  }

  public void debug(String s) {
    logger.debug(s);
  }

  public void debug(String s, Object o) {
    logger.debug(s, o);
  }

  public void debug(String s, Object o, Object o1) {
    logger.debug(s, o, o1);
  }

  public void debug(String s, Object... objects) {
    logger.debug(s, objects);
  }

  public void debug(String s, Throwable throwable) {
    logger.debug(s, throwable);
  }

  public boolean isInfoEnabled() {
    return logger.isInfoEnabled();
  }

  public void info(String s) {
    logger.info(s);
  }

  public void info(String s, Object o) {
    logger.info(s, o);
  }

  public void info(String s, Object o, Object o1) {
    logger.info(s, o, o1);
  }

  public void info(String s, Object... objects) {
    logger.info(s, objects);
  }

  public void info(String s, Throwable throwable) {
    logger.info(s, throwable);
  }

  public boolean isWarnEnabled() {
    return logger.isWarnEnabled();
  }

  public boolean isDebugEnabled() {
    return logger.isDebugEnabled();
  }

  public void warn(String s) {
    logger.warn(s);
  }

  public void warn(String s, Object o) {
    logger.warn(s, o);
  }

  public void warn(String s, Object... objects) {
    logger.warn(s, objects);
  }

  public void warn(String s, Object o, Object o1) {
    logger.warn(s, o, o1);
  }

  public void warn(String s, Throwable throwable) {
    logger.warn(s, throwable);
  }

  public boolean isErrorEnabled() {
    return logger.isErrorEnabled();
  }

  public void error(String s) {
    logger.error(s);
  }

  public void error(String s, Object o) {
    logger.error(s, o);
  }

  public void error(String s, Object o, Object o1) {
    logger.error(s, o, o1);
  }

  public void error(String s, Object... objects) {
    logger.error(s, objects);
  }

  public void error(String s, Throwable throwable) {
    logger.error(s, throwable);
  }
  
  
}
