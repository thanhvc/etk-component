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
package org.etk.kernel.management.spi;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.etk.kernel.management.annotations.ImpactType;

/**
 * Meta data that describes a managed method.
 * 
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Jul 28, 2011  
 */
public class ManagedMethodMetaData extends ManagedMetaData {

  /** . */
  private final Method                                       method;

  /** . */
  private final Map<Integer, ManagedMethodParameterMetaData> parameters;

  /** . */
  private final ImpactType                                   impact;

   /**
    * Build a new instance.
    *
    * @param method the method
    * @param impactType the access mode
    * @throws NullPointerException if the method is null or the impact is null
    */
  public ManagedMethodMetaData(Method method, ImpactType impactType) throws NullPointerException {
    if (method == null) {
      throw new NullPointerException();
    }
    if (impactType == null) {
      throw new NullPointerException();
    }

    //
    this.method = method;
    this.impact = impactType;
    this.parameters = new HashMap<Integer, ManagedMethodParameterMetaData>();
  }

  public String getName() {
    return method.getName();
  }

  public ImpactType getImpact() {
    return impact;
  }

  public Method getMethod() {
    return method;
  }

  public void addParameter(ManagedMethodParameterMetaData parameter) {
    if (parameter == null) {
      throw new NullPointerException("No null parameter accepted");
    }
    parameters.put(parameter.getIndex(), parameter);
  }

  public Collection<ManagedMethodParameterMetaData> getParameters() {
    return Collections.unmodifiableCollection(parameters.values());
  }
}

