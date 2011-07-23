package org.etk.kernel.container.component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.etk.kernel.container.KernelContainer;
import org.picocontainer.PicoContainer;

/**
 * 
 *
 */
class RequestLifeCycleStack extends LinkedList<RequestLifeCycle> {

  /** . */
  private final Set<ComponentRequestLifecycle> allComponents = new HashSet<ComponentRequestLifecycle>();

  RequestLifeCycleStack() {
  }

  void begin(ComponentRequestLifecycle lifeCycle) {
    if (allComponents.contains(lifeCycle)) {
      addLast(new RequestLifeCycle(null, Collections.<ComponentRequestLifecycle> emptyList()));
    } else {
      RequestLifeCycle requestLF = new RequestLifeCycle(null, Collections.singletonList(lifeCycle));
      allComponents.add(lifeCycle);
      addLast(requestLF);
      requestLF.doBegin();
    }
  }

  void begin(KernelContainer container, boolean local) {
    // Need to make a copy as modifying the list is cached by the container
    List<ComponentRequestLifecycle> components = new ArrayList<ComponentRequestLifecycle>((List<ComponentRequestLifecycle>) container.getComponentInstancesOfType(ComponentRequestLifecycle.class));

    //
    if (!local) {
      for (PicoContainer current = container.getParent(); current != null; current = current.getParent()) {
        components.addAll((List<ComponentRequestLifecycle>) current.getComponentInstancesOfType(ComponentRequestLifecycle.class));
      }

    }

    // Remove components that have already started their life cycle
    components.removeAll(allComponents);

    // Contribute to the all component set
    allComponents.addAll(components);

    //
    RequestLifeCycle lifeCycle = new RequestLifeCycle(container, components);

    //
    addLast(lifeCycle);

    //
    lifeCycle.doBegin();
  }

  Map<Object, Throwable> end() {
    RequestLifeCycle lifeCycle = removeLast();

    //
    IdentityHashMap<Object, Throwable> result = lifeCycle.doEnd();

    //
    allComponents.removeAll(result.keySet());

    //
    return result;
  }
}
