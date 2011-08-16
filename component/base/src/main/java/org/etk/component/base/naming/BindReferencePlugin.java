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
package org.etk.component.base.naming;

import java.util.Iterator;
import java.util.Map.Entry;

import javax.naming.Reference;
import javax.naming.StringRefAddr;

import org.etk.kernel.container.component.BaseComponentPlugin;
import org.etk.kernel.container.configuration.ConfigurationException;
import org.etk.kernel.container.xml.InitParams;
import org.etk.kernel.container.xml.PropertiesParam;
import org.etk.kernel.container.xml.ValueParam;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          thanhvucong.78@google.com
 * Aug 6, 2011  
 */
public class BindReferencePlugin extends BaseComponentPlugin {

  private String    bindName;

  private Reference reference;

  /**
   * @param params Mandatory: bind-name (value param) - name of binding
   *          reference class-name (value param) - type of binding reference
   *          factory (value param) - object factory type Optional:
   *          ref-addresses (properties param)
   * @throws ConfigurationException
   */
  public BindReferencePlugin(InitParams params) throws ConfigurationException {

    ValueParam bnParam = params.getValueParam("bind-name");
    if (bnParam == null) {
      throw new ConfigurationException("No 'bind-name' parameter found");
    }
    ValueParam cnParam = params.getValueParam("class-name");
    if (cnParam == null) {
      throw new ConfigurationException("No 'class-name' parameter found");
    }
    ValueParam factoryParam = params.getValueParam("factory");
    if (factoryParam == null) {
      throw new ConfigurationException("No 'factory' parameter found");
    }
    ValueParam flParam = params.getValueParam("factory-location");
    String factoryLocation;
    if (flParam != null)
      factoryLocation = flParam.getValue();
    else
      factoryLocation = null;

    bindName = bnParam.getValue();
    reference = new Reference(cnParam.getValue(), factoryParam.getValue(), factoryLocation);

    PropertiesParam addrsParam = params.getPropertiesParam("ref-addresses");
    if (addrsParam != null) {
      for (Iterator it = addrsParam.getProperties().entrySet().iterator(); it.hasNext();) {
        Entry entry = (Entry) it.next();
        reference.add(new StringRefAddr((String) entry.getKey(), (String) entry.getValue()));
      }
    }
  }

  /**
   * @return reference bound
   */
  public Reference getReference() {
    return reference;
  }

  /**
   * @return name
   */
  public String getBindName() {
    return bindName;
  }
}
