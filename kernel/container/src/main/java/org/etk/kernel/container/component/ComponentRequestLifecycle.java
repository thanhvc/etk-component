package org.etk.kernel.container.component;

import org.etk.kernel.container.KernelContainer;

/**
 * This interface, implemented by component implementation, allows 
 * Component implementation includes:
 * <ul>
 *   <li>The component's life cycle control interface.</li>
 *   <li>The component's service unit manager, for handling when it deployed.</li>
 * </ul> 
 * @author thanh_vucong
 *
 */
public interface ComponentRequestLifecycle {
  
  /**
   * Start the request component, This makes the component start accepting request for processing.
   * After a call to this method, {@link #endRequest(KernelContainer()} may be called to end of request. 
   * 
   * @param container
   * @throws Exception if the component is unable to stop
   */
  public void startRequest(KernelContainer container);
  /**
   * End the request component, This makes the component stop accepting request for processing.
   * And then commit the processing.
   * 
   * After a call to this method, {@link #startRequest(KernelContainer()} may be called 
   * again.
   * 
   * @param container
   * @throws Exception if the component is unable to stop
   */
  public void endRequest(KernelContainer container);
}
