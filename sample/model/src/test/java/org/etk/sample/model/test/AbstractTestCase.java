package org.etk.sample.model.test;

import junit.framework.TestCase;

import org.etk.model.api.EntityBuilder;
import org.etk.model.core.EntitySession;

public abstract class AbstractTestCase extends TestCase {

  /** . */
  protected EntityBuilder builder;

  /** . */
  protected EntitySession entitySession;


  @Override
  protected void setUp() throws Exception {
   

    /*
    RepositoryBootstrap bootstrap = new RepositoryBootstrap();
    bootstrap.bootstrap();
    repo = bootstrap.getRepository();
    */
    
    //
    builder = EntityBuilder.create();

    //
    createDomain();

   
    //
    entitySession = builder.build();

    
  }

  @Override
  protected void tearDown() throws Exception {
    builder = null;
    entitySession = null;
  }

  

  protected final EntityBuilder getBuilder() {
    return builder;
  }



  protected final void addClass(Class<?> clazz) {
    builder.add(clazz);
  }

  

  protected abstract void createDomain();

 
  
}
