/**
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/

/**
 * Created by The eXo Platform SAS
 * Author : Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: Sep 15, 2003
 * Time: 2:33:19 PM
 */
package org.etk.kernel.container.mocks;

import org.picocontainer.Startable;

public class MockServiceBImp implements MockServiceB, Startable {

  public MockServiceBImp() {
    System.out.println("MockServiceBImp constructor");
  }

  public void methodServiceB() {
    System.out.println("Method Service B");
  }

  public void start() {
    System.out.println("Start in MockServiceBImp");
  }

  public void stop() {
    System.out.println("Stop in MockServiceBImp");
  }

}
