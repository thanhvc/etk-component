package org.etk.sandbox.service;

import org.etk.common.logging.Logger;
import org.etk.service.foo.FooListenerPlugin;
import org.etk.service.foo.MockRepositoryService;
import org.etk.service.foo.spi.FooLifeCycleEvent;

public class MockFooBarPublisher extends FooListenerPlugin {

  /**
   * The Logger.
   */
  private static final Logger log = Logger.getLogger(MockFooBarPublisher.class);
  
  @Override
  public void fooCreated(FooLifeCycleEvent event) {
    log.debug("CurrentRepository ::" + MockRepositoryService.getCurrentRepo());
    log.debug("fooCreated::Event Type = " + event.getType());
  }

  @Override
  public void fooRemoved(FooLifeCycleEvent event) {
    log.debug("CurrentRepository ::" + MockRepositoryService.getCurrentRepo());
    log.debug("fooRemoved::Event Type = " + event.getType());
  }

  @Override
  public void fooUpdated(FooLifeCycleEvent event) {
    log.debug("CurrentRepository ::" + MockRepositoryService.getCurrentRepo());
    log.debug("fooUpdated::Event Type = " + event.getType());
  }

}
