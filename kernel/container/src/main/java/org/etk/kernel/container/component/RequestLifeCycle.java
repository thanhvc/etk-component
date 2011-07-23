package org.etk.kernel.container.component;

import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

import org.etk.kernel.container.KernelContainer;

/**
 * <p>
 * The request life cycle object allows a client to demarcate the life cycle of
 * the various components associated with containers. It allows container
 * stacking and guarantees that the life cycle of the components will never be
 * called twice in the same stack.
 * </p>
 */
public class RequestLifeCycle {

  /** The current stack. */
  private static ThreadLocal<RequestLifeCycleStack> current = new ThreadLocal<RequestLifeCycleStack>();

  /** The components of this life cycle. */
  private List<ComponentRequestLifecycle>           components;

  /** The container of this life cycle. */
  private final KernelContainer                     container;

  public RequestLifeCycle(KernelContainer container, List<ComponentRequestLifecycle> components) {
    this.container = container;
    this.components = components;
  }

  void doBegin() {
    for (ComponentRequestLifecycle component : components) {
      component.startRequest(container);
    }
  }

  IdentityHashMap<Object, Throwable> doEnd() {
    IdentityHashMap<Object, Throwable> result = new IdentityHashMap<Object, Throwable>();

    //
    for (ComponentRequestLifecycle componentRLF : components) {
      Throwable t = null;
      try {
        componentRLF.endRequest(container);
      } catch (Throwable throwable) {
        t = throwable;
      } finally {
        result.put(componentRLF, t);
      }
    }

    //
    return result;
  }

  /**
   * Starts the life cycle of the provided container and add it to the life
   * cycle stack. Only the components of the container that have not been
   * previously enrolled in a life cycle are begun.
   * 
   * @param container the container to use
   * @param local will only trigger life cycle for the container and not its
   *          ancestors
   */
  public static void begin(KernelContainer container, boolean local) {
    if (container == null) {
      throw new NullPointerException();
    }
    RequestLifeCycleStack lf = current.get();
    if (lf == null) {
      lf = new RequestLifeCycleStack();
      current.set(lf);
    }
    lf.begin(container, local);
  }

  /**
   * Starts the life cycle of the provided life cycle and add it to the life
   * cycle stack. If the life cycle has already been triggered before then no
   * operation will be really performed. When the life cycle is called, the
   * argument container will be null.
   * 
   * @param lifeCycle the life cycle
   */
  public static void begin(ComponentRequestLifecycle lifeCycle) {
    if (lifeCycle == null) {
      throw new NullPointerException();
    }
    RequestLifeCycleStack lf = current.get();
    if (lf == null) {
      lf = new RequestLifeCycleStack();
      current.set(lf);
    }
    lf.begin(lifeCycle);
  }

  /**
   * Starts the life cycle of the provided container and add it to the life
   * cycle stack. Only the components of the container that have not been
   * previously enrolled in a life cycle are begun.
   * 
   * @param container the container to use
   */
  public static void begin(KernelContainer container) {
    begin(container, false);
  }

  /**
   * <p>
   * Ends the life cycle of the most recent container started. Only the
   * components of the container that have not been previously enrolled in a
   * life cycle are ended.
   * </p>
   * <p>
   * The result map returned has for keys the components whose the life cycle
   * ended during this method call and the associated value are the potential
   * throwable that were thrown by those components. It is usefull when writing
   * unit test to be aware of the throwable of the various components involved
   * in a request life cycle.
   * </p>
   * 
   * @throws IllegalStateException if no container life cycle is associated with
   *           this thread
   * @return the result map
   */
  public static Map<Object, Throwable> end() throws IllegalStateException {
    RequestLifeCycleStack lf = current.get();
    if (lf == null) {
      throw new IllegalStateException();
    }
    Map<Object, Throwable> result = lf.end();
    if (lf.isEmpty()) {
      current.set(null);
    }
    return result;
  }
}
