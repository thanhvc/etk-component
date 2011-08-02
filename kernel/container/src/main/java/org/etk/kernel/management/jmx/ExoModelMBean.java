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
package org.etk.kernel.management.jmx;

import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanRegistration;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.management.RuntimeOperationsException;
import javax.management.modelmbean.InvalidTargetObjectTypeException;
import javax.management.modelmbean.ModelMBeanInfo;
import javax.management.modelmbean.RequiredModelMBean;

import org.etk.kernel.management.ManagementAware;
import org.etk.kernel.management.jmx.annotations.NamingContext;
import org.etk.kernel.management.spi.ManagedResource;


/**
 *  A convenient subclass of {@link RequiredModelMBean) that routes the invocation of the interface
 * {@link MBeanRegistration} to the managed resource when it implements the method.
 * 
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Jul 28, 2011  
 */
public class ExoModelMBean extends RequiredModelMBean {

   /** . */
   private Object mr;

   /** . */
   private final ManagedResource context;

  public ExoModelMBean(ManagedResource context, Object mr, ModelMBeanInfo mbi) throws MBeanException,
      RuntimeOperationsException,
      InstanceNotFoundException,
      InvalidTargetObjectTypeException {
    super(mbi);

    //
    this.context = context;
    this.mr = mr;

    //
    setManagedResource(mr, "ObjectReference");
  }

  @Override
  public Object invoke(String opName, Object[] opArgs, String[] sig) throws MBeanException,
                                                                    ReflectionException {
    context.beforeInvoke(mr);
    try {
      return super.invoke(opName, opArgs, sig);
    } finally {
      context.afterInvoke(mr);
    }
  }

  @Override
  public ObjectName preRegister(MBeanServer server, ObjectName name) throws Exception {
    name = super.preRegister(server, name);

    //
    if (mr instanceof MBeanRegistration) {
      ((MBeanRegistration) mr).preRegister(server, name);
    }

    //
    return name;
  }

  @Override
  public void postRegister(Boolean registrationDone) {
    super.postRegister(registrationDone);

    //
    PropertiesInfo info = PropertiesInfo.resolve(mr.getClass(), NamingContext.class);

    //
    MBeanScopingData scopingData = info != null ? info.resolve(mr) : new MBeanScopingData();

    //
    context.setScopingData(MBeanScopingData.class, scopingData);

    //
    if (mr instanceof MBeanRegistration) {
      ((MBeanRegistration) mr).postRegister(registrationDone);
    }
  }

   @Override
  public void preDeregister() throws Exception {
    if (mr instanceof MBeanRegistration) {
      ((MBeanRegistration) mr).preDeregister();
    }

    //
    if (mr instanceof ManagementAware) {
      ((ManagementAware) mr).setContext(null);
    }

    //
    super.preDeregister();
  }

  @Override
  public void postDeregister() {
    if (mr instanceof MBeanRegistration) {
      ((MBeanRegistration) mr).postDeregister();
    }

    //
    super.postDeregister();
  }
}
