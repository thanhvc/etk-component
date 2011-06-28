package org.etk.kernel.container.jmx;

import java.io.Serializable;

import org.picocontainer.ComponentAdapter;
import org.picocontainer.Parameter;
import org.picocontainer.PicoIntrospectionException;
import org.picocontainer.defaults.AssignabilityRegistrationException;
import org.picocontainer.defaults.ComponentAdapterFactory;
import org.picocontainer.defaults.NotConcreteRegistrationException;

public class MX4JComponentAdapterFactory implements ComponentAdapterFactory, Serializable
{
   public ComponentAdapter createComponentAdapter(Object key, Class impl, Parameter[] params)
      throws PicoIntrospectionException, AssignabilityRegistrationException, NotConcreteRegistrationException
   {
      return new MX4JComponentAdapter(key, impl);
   }
}
