package org.etk.kernel.container.component;

import org.etk.kernel.container.KernelContainer;

public interface ComponentRequestLifecycle
{
   public void startRequest(KernelContainer container);

   public void endRequest(KernelContainer container);
}
