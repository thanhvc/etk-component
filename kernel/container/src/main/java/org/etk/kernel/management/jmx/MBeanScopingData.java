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

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Jul 28, 2011  
 */
public class MBeanScopingData extends LinkedHashMap<String, String> {
  public MBeanScopingData(int initialCapacity, float loadFactor) {
    super(initialCapacity, loadFactor);
  }

  public MBeanScopingData(int initialCapacity) {
    super(initialCapacity);
  }

  public MBeanScopingData() {
  }

  public MBeanScopingData(Map<? extends String, ? extends String> m) {
    super(m);
  }
}
