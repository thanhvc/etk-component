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
public interface ComponentLifecycle {
  
  /**
   * Initialize the component. This performs initialization required by the component
   * but does not make it ready to process messages. This method is called once for each life cycle of the component
   *  
   * @param container
   * @throws Exception if the component is unable to initialize
   */
	public void initComponent(KernelContainer container) throws Exception;

	/**
	 * Start the component, This makes the component ready to process messages.
	 * This method is called after {@link #initComponent(KernelContainer))} method, 
	 * both when the component is being started for the first time and when 
	 * the component is being restarted after a previous call to {@link #startComponent(KernelContainer))}
	 * 
	 * @param container
	 * @throws Exception if the component is unable to start
	 */
	public void startComponent(KernelContainer container) throws Exception;

	/**
	 * Stop the component, This makes the component stop accepting messages for processing.
	 * After a call to this method, {@link #startComponent(KernelContainer()} may be called 
	 * again without first calling {@link #initComponent(KernelContainer)}.
	 * 
	 * 
	 * @param container
	 * @throws Exception if the component is unable to stop
	 */
	public void stopComponent(KernelContainer container) throws Exception;

	/**
	 * Shutdown the component. This performs clean-up, releasing all run-time resources used by the component.
	 * Once this method has been called, {@link #initComponent(KernelContainer))} method must be called before 
	 * the component can be started again with a call to  {@link #startComponent(KernelContainer))}
	 *  
	 * @param container
	 * @throws Exception if the component is unable to destroy
	 */
	public void destroyComponent(KernelContainer container) throws Exception;
}