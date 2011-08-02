/*
 * Copyright (C) 2003-2011 eXo Platform SAS.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.etk.kernel.container.management;

import org.picocontainer.ComponentAdapter;
import org.picocontainer.Parameter;
import org.picocontainer.PicoIntrospectionException;
import org.picocontainer.defaults.AssignabilityRegistrationException;
import org.picocontainer.defaults.ComponentAdapterFactory;
import org.picocontainer.defaults.NotConcreteRegistrationException;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Jul 28, 2011  
 */
public class ManageableComponentAdapterFactory implements ComponentAdapterFactory {

   /** . */
   private ComponentAdapterFactory delegate;

   /** . */
   ManageableContainer container;

  public ManageableComponentAdapterFactory(ComponentAdapterFactory delegate) {
    this.delegate = delegate;
  }

  public ComponentAdapter createComponentAdapter(Object componentKey,
                                                 Class componentImplementation,
                                                 Parameter[] parameters) throws PicoIntrospectionException,
                                                                        AssignabilityRegistrationException,
                                                                        NotConcreteRegistrationException
   {
      ComponentAdapter adapter = delegate.createComponentAdapter(componentKey, componentImplementation, parameters);
      return new ManageableComponentAdapter(container, adapter);
   }
}

