/*
 * Copyright (C) 2009 eXo Platform SAS.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.etk.component.database.impl;

import java.util.List;

import org.etk.kernel.container.component.BaseComponentPlugin;
import org.etk.kernel.container.xml.InitParams;

/**
 * Created by The eXo Platform SAS Author : Tuan Nguyen
 * tuan08@users.sourceforge.net Jul 26, 2005
 */
public class AddHibernateMappingPlugin extends BaseComponentPlugin {

  List mapping_;

  List<String> annotations;

  public AddHibernateMappingPlugin(InitParams params) {
    if (params.containsKey("hibernate.mapping")) {
      mapping_ = params.getValuesParam("hibernate.mapping").getValues();
    }
    if (params.containsKey("hibernate.annotations")) {
      annotations = params.getValuesParam("hibernate.annotations").getValues();
    }
  }

  public List getMapping() {
    return mapping_;
  }

  public List<String> getAnnotations() {
    return annotations;
  }

}
