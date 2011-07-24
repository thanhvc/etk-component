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
package org.exoplatform.mocks;

import org.exoplatform.container.component.ComponentPlugin;
import org.exoplatform.container.xml.InitParams;

/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Apr 18, 2005
 * @version $Id: MockServicePlugin.java 5799 2006-05-28 17:55:42Z geaz $
 */
public class MockServicePlugin implements ComponentPlugin {
  String name;

  String description;

  public MockServicePlugin(InitParams params) {
    System.out.println("plugin init params: " + params);
  }

  public String getName() {
    return name;
  }

  public void setName(String s) {
    name = s;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String s) {
    description = s;
  }

}
