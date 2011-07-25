/*
 * Copyright (C) 2003-2007 eXo Platform SAS.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see<http://www.gnu.org/licenses/>.
 */
package org.exoplatform.services.cache;

import java.util.ArrayList;
import java.util.List;

import org.exoplatform.container.component.BaseComponentPlugin;
import org.exoplatform.container.xml.InitParams;

/**
 * Created by The eXo Platform SAS Author : Tuan Nguyen
 * tuan08@users.sourceforge.net Jan 6, 2006
 */
public class ExoCacheConfigPlugin extends BaseComponentPlugin {
  private List<ExoCacheConfig> configs_;

  public ExoCacheConfigPlugin(InitParams params) {
    configs_ = new ArrayList<ExoCacheConfig>();
    List configs = params.getObjectParamValues(ExoCacheConfig.class);
    for (int i = 0; i < configs.size(); i++) {
      ExoCacheConfig config = (ExoCacheConfig) configs.get(i);
      configs_.add(config);
    }
  }

  public List<ExoCacheConfig> getConfigs() {
    return configs_;
  }
}
