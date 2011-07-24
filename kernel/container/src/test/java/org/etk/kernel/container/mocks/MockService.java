/**
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/

/**
 * Created by The eXo Platform SAS
 * Author : Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: Aug 7, 2003
 * Time: 11:39:25 PM
 */
package org.etk.kernel.container.mocks;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.etk.kernel.container.component.ComponentPlugin;
import org.etk.kernel.container.xml.InitParams;


public class MockService {
  Map plugins_   = new HashMap();

  Map listeners_ = new HashMap();

  public MockService(InitParams params) {
    System.out.println("MockService constructor, init params: " + params);
  }

  public String hello() {
    return "HELLO WORLD SERVICE";
  }

  public String getTest() {
    return "heh";
  }

  public void addPlugin(ComponentPlugin plugin) {
    System.out.println("add plugin === >" + plugin.getName());
    plugins_.put(plugin.getName(), plugin);
  }

  public ComponentPlugin removePlugin(String name) {
    return (ComponentPlugin) plugins_.remove(name);
  }

  public Collection getPlugins() {
    return plugins_.values();
  }

}
