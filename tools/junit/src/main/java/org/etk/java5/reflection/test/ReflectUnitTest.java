package org.etk.java5.reflection.test;
import junit.framework.Assert;

public abstract class ReflectUnitTest extends Assert {

  private String phase;
  
  private TypeDomain domain;
  
  public final void run(String phase, TypeDomain domain) throws Exception {
    this.phase = phase;
    this.domain = domain;
    execute();
  }
  
  protected abstract void execute() throws Exception;
  
}
